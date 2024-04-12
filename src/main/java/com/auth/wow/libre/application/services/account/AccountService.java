package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.application.services.account_banned.AccountBannedService;
import com.auth.wow.libre.application.services.account_muted.AccountMutedService;
import com.auth.wow.libre.application.services.account_web.AccountWebService;
import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.AccountBanned;
import com.auth.wow.libre.domain.model.AccountMuted;
import com.auth.wow.libre.domain.model.comunication.MailSenderVars;
import com.auth.wow.libre.domain.model.dto.AccountBannedDto;
import com.auth.wow.libre.domain.model.dto.AccountDetail;
import com.auth.wow.libre.domain.model.dto.AccountDto;
import com.auth.wow.libre.domain.model.dto.AccountMutedDto;
import com.auth.wow.libre.domain.model.dto.ChangePasswordAccountDto;
import com.auth.wow.libre.domain.model.dto.UpdateAccountDto;
import com.auth.wow.libre.domain.model.dto.WebPasswordAccountDto;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.model.exception.FoundException;
import com.auth.wow.libre.domain.model.exception.InternalException;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.domain.ports.out.account.UpdateAccountPort;
import com.auth.wow.libre.infrastructure.conf.comunication.EmailSend;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final LoadAccountPort loadAccountPort;
    private final ObtainAccountPort obtainAccountPort;
    private final PasswordEncoder passwordEncoder;
    private final UpdateAccountPort updateAccountPort;
    private final AccountWebService accountWebService;
    private final AccountBannedService accountBannedService;
    private final AccountMutedService accountMutedService;
    private final EmailSend emailSend;


    public AccountService(LoadAccountPort loadAccountPort, ObtainAccountPort obtainAccountPort,
                          PasswordEncoder passwordEncoder,
                          UpdateAccountPort updateAccountPort, AccountWebService accountWebService,
                          AccountBannedService accountBannedService, AccountMutedService accountMutedService,
                          EmailSend emailSend) {
        this.loadAccountPort = loadAccountPort;
        this.obtainAccountPort = obtainAccountPort;
        this.passwordEncoder = passwordEncoder;
        this.updateAccountPort = updateAccountPort;
        this.accountWebService = accountWebService;
        this.accountBannedService = accountBannedService;
        this.accountMutedService = accountMutedService;
        this.emailSend = emailSend;
    }

    @Override
    public void create(AccountDto account, String transactionId) {


        if (obtainAccountPort.findByUsername(account.getUsername()) != null
                 || obtainAccountPort.findByEmail(account.getEmail()) != null) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        try {
            byte[] verifier = Hex.decodeHex(account.getVerifier());
            byte[] salt = Hex.decodeHex(account.getSalt());

            Account createAccount = Account.builder()
                    .password(passwordEncoder.encode(account.getPassword()))
                    .email(account.getEmail())
                    .salt(salt)
                    .username(account.getUsername())
                    .verifier(verifier)
                    .country(account.getCountry())
                    .dateOfBirth(account.getDateOfBirth())
                    .cellPhone(account.getCellPhone())
                    .lastName(account.getLastName())
                    .locked(true)
                    .firstName(account.getFirstName())
                    .build();

            AccountWebEntity accountWeb = accountWebService.save(createAccount, transactionId);

            loadAccountPort.create(createAccount, accountWeb);
            sendOtpRegisterUser(account.getEmail(), account.getUsername(), transactionId);
        } catch (DecoderException e) {
            LOGGER.error("Decoder Error Password. Message: [{}]",
                    e.getMessage());
            throw new InternalException("An unexpected error has occurred internally, please try again later.",
                    transactionId);
        }
    }

    private void sendOtpRegisterUser(String email, String username, String transactionId) {
        emailSend.sendHTMLEmail(email, "Bienvenido, Su cuenta ha sido creada exitosamente, Por favor verifique su correo",
                MailSenderVars.builder().email(email).transactionId(transactionId).username(username).build());
    }

    @Override
    public AccountDetail obtain(String username, String transactionId) {
        Account account = obtainAccountPort.findByUsername(username);
        Optional<AccountBanned> accountBanned = Optional.ofNullable(accountBannedService.getAccountBanned(account.id));
        Optional<AccountMuted> accountMuted = Optional.ofNullable(accountMutedService.getAccountMuted(account.id,
                transactionId));

        return AccountDetail.builder()
                .email(account.email)
                .username(username)
                .country(account.country)
                .dateOfBirth(account.dateOfBirth)
                .cellPhone(account.cellPhone)
                .accountWebId(account.accountWebId)
                .lastName(account.lastName)
                .firstName(account.firstName)
                .accountBanned(accountBanned.map(AccountBannedDto::new).orElse(null))
                .accountMuted(accountMuted.map(AccountMutedDto::new).orElse(null))
                .build();
    }

    @Override
    public void updated(String username, UpdateAccountDto account, String transactionId) {

        Account accountUpdate = Account.builder()
                .email(account.getEmail())
                .username(username)
                .country(account.getCountry())
                .dateOfBirth(account.getDateOfBirth())
                .cellPhone(account.getCellPhone())
                .lastName(account.getLastName())
                .firstName(account.getFirstName())
                .build();

        Account accountFound = updateAccountPort.update(accountUpdate, transactionId);

        accountWebService.update(accountUpdate, accountFound.accountWebId, transactionId);
    }

    @Override
    public void gameChangePassword(String username, ChangePasswordAccountDto changePasswordAccountDto,
                                   String transactionId) throws DecoderException {

        byte[] verifier = Hex.decodeHex(changePasswordAccountDto.getVerifier());
        byte[] salt = Hex.decodeHex(changePasswordAccountDto.getSalt());

        Account account = obtainAccountPort.findByUsername(username);

        if (!passwordEncoder.matches(changePasswordAccountDto.getPassword(), account.password)) {
            throw new BadRequestException("The web password provided is invalid, please check your data", transactionId);
        }

        Account changePassword = Account.builder()
                .username(username)
                .verifier(verifier)
                .salt(salt)
                .build();

        updateAccountPort.update(changePassword, transactionId);
    }

    @Override
    public void webChangePassword(String username, WebPasswordAccountDto webPasswordAccountDto, String transactionId) {
        Account account = obtainAccountPort.findByUsername(username);

        if (!passwordEncoder.matches(webPasswordAccountDto.oldPassword, account.password)) {
            throw new BadRequestException("The web password provided is invalid, please check your data", transactionId);
        }

        Account changePassword = Account.builder()
                .password(passwordEncoder.encode(webPasswordAccountDto.password))
                .build();

        accountWebService.update(changePassword, account.accountWebId, transactionId);
    }

    @Override
    public void validateEmail(String email, String otp, String transactionId) {
        Account account = obtainAccountPort.findByEmail(email);

        if (account == null) {
            throw new BadRequestException("The email provided is invalid, please check your data", transactionId);
        }

        if (!account.otpSecret.equals(otp)) {
            throw new BadRequestException("The otp provided is invalid, please check your data", transactionId);
        }

        Account accountUpdate = Account.builder()
                .locked(false)
                .build();

        updateAccountPort.update(accountUpdate, transactionId);
    }

    @Override
    public boolean searchEmail(String search, String transactionId) {
        return Optional.ofNullable(obtainAccountPort.findByEmail(search)).isPresent();
    }

}
