package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.application.services.jwt.JwtPortService;
import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.RolModel;
import com.auth.wow.libre.domain.model.comunication.MailSenderVars;
import com.auth.wow.libre.domain.model.dto.AccountGameDto;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.dto.AccountsDetailDto;
import com.auth.wow.libre.domain.model.exception.FoundException;
import com.auth.wow.libre.domain.model.exception.GenericErrorException;
import com.auth.wow.libre.domain.model.exception.InternalException;
import com.auth.wow.libre.domain.model.exception.NotFoundException;
import com.auth.wow.libre.domain.model.security.CustomUserDetails;
import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.in.rol.RolPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.domain.ports.out.account.SaveAccountPort;
import com.auth.wow.libre.infrastructure.conf.comunication.EmailSend;
import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    public static final int LIMIT_ACCOUNT = 10;
    private final PasswordEncoder passwordEncoder;
    private final EmailSend emailSend;
    private final JwtPortService jwtPort;
    private final RolPort rolPort;
    private static final String ROL_CLIENT_DEFAULT = "CLIENT";

    private final AccountWebPort accountWebPort;
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;


    public AccountService(AccountWebPort accountWebPort, PasswordEncoder passwordEncoder, JwtPortService jwtPort,
                          RolPort rolPort, EmailSend emailSend, ObtainAccountPort obtainAccountPort,
                          SaveAccountPort saveAccountPort) {
        this.accountWebPort = accountWebPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtPort = jwtPort;
        this.rolPort = rolPort;
        this.emailSend = emailSend;
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
    }

    @Override
    public JwtDto createWebAccount(AccountWebDto accountWebDto, String transactionId) {

        if (accountWebPort.findByEmail(accountWebDto.getEmail(), transactionId) != null) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final String passwordEncode = passwordEncoder.encode(accountWebDto.getPassword());

        AccountWebModel accountWebModel = new AccountWebModel(accountWebDto.getCountry(),
                accountWebDto.getDateOfBirth(), accountWebDto.getFirstName(), accountWebDto.getLastName(),
                accountWebDto.getCellPhone(), accountWebDto.getEmail(), passwordEncode);

        final RolModel rolModel = rolPort.findByName(ROL_CLIENT_DEFAULT, transactionId);

        if (rolModel == null) {
            LOGGER.error("An error occurred while assigning a role.  TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("An error occurred while assigning a role.", transactionId);
        }

        final Long id = accountWebPort.save(accountWebModel, rolModel, transactionId).id;

        CustomUserDetails customUserDetails = new CustomUserDetails(
                List.of(rolModel),
                passwordEncode,
                accountWebModel.email,
                true,
                true,
                true,
                true,
                id
        );
        final String token = jwtPort.generateToken(customUserDetails);
        final Date expiration = jwtPort.extractExpiration(token);
        final String refreshToken = jwtPort.generateRefreshToken(customUserDetails);
        final String email = accountWebDto.getEmail();

        emailSend.sendHTMLEmail(email, "Bienvenido, Su cuenta ha sido creada exitosamente, " +
                        "Por favor verifique su correo",
                MailSenderVars.builder().email(email).transactionId(transactionId).build());

        return new JwtDto(token, refreshToken, expiration);
    }

    @Override
    public boolean isEmailExists(String email, String transactionId) {
        return Optional.ofNullable(accountWebPort.findByEmail(email, transactionId)).isPresent();
    }

    @Override
    public List<AccountsDetailDto> availableAccounts(String email, String transactionId) {

        final AccountWebModel accountWebModel = getAccountWebModel(email, transactionId);

        List<AccountEntity> accounts = obtainAccountPort.findByAccountWebId(accountWebModel.id);

        return accounts.stream().map(account ->
                new AccountsDetailDto(account.getId(), account.getUsername(), account.getEmail(),
                        account.getExpansion(), account.isOnline(), account.getFailedLogins(), account.getJoinDate(),
                        account.getLastIp())
        ).collect(Collectors.toList());
    }

    @Override
    public void createGameAccount(AccountGameDto accountGameDto, String email, String transactionId) {

        if (obtainAccountPort.findByUsername(accountGameDto.getUsername()).isPresent()) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final AccountWebModel accountWebModel = getAccountWebModel(email, transactionId);

        List<AccountEntity> accounts = obtainAccountPort.findByAccountWebId(accountWebModel.id);

        if (accounts.size() >= LIMIT_ACCOUNT) {
            throw new GenericErrorException(
                    transactionId, "You have exceeded the maximum number of accounts created.", HttpStatus.CONFLICT);
        }


        AccountWebModel accountWeb = accountWebPort.findByEmail(email, transactionId);

        if (accountWeb == null) {
            LOGGER.error("You do not have an administrative account or it is not valid, please contact support.  " +
                            "TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("You do not have an administrative account or it is not valid, please contact" +
                    " support.", transactionId);
        }

        try {
            final String username = accountGameDto.getUsername();
            byte[] verifier = Hex.decodeHex(accountGameDto.getVerifier());
            byte[] salt = Hex.decodeHex(accountGameDto.getSalt());
            AccountEntity account = new AccountEntity();
            account.setSalt(salt);
            account.setVerifier(verifier);
            account.setLocked(false);
            account.setUsername(username);
            account.setEmail(accountWeb.email);
            account.setAccountWeb(AccountWebEntity.fromDomainModel(accountWeb));

            saveAccountPort.save(account);
        } catch (DecoderException e) {
            LOGGER.error("Decoder Error Password. Message: [{}] -  TransactionId: [{}]",
                    e.getMessage(), transactionId);
            throw new InternalException("An unexpected error has occurred internally, please try again later.",
                    transactionId);
        }
    }

    private AccountWebModel getAccountWebModel(String email, String transactionId) {
        final AccountWebModel accountWebModel = accountWebPort.findByEmail(email, transactionId);

        if (accountWebModel == null) {
            LOGGER.error("You do not have an administrative account or it is not valid, please contact support.  " +
                            "TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("You do not have an administrative account or it is not valid, please contact" +
                    " support.", transactionId);
        }

        return accountWebModel;
    }


}
