package com.auth.wow.libre.application.services.account_web;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.RolModel;
import com.auth.wow.libre.domain.model.comunication.MailSenderVars;
import com.auth.wow.libre.domain.model.comunication.OtpSenderVarsDto;
import com.auth.wow.libre.domain.model.comunication.PasswordSenderVarsDto;
import com.auth.wow.libre.domain.model.comunication.RegisterSenderVarsDto;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.dto.ValidateOtpDto;
import com.auth.wow.libre.domain.model.exception.FoundException;
import com.auth.wow.libre.domain.model.exception.InternalException;
import com.auth.wow.libre.domain.model.exception.NotFoundException;
import com.auth.wow.libre.domain.model.security.CustomUserDetails;
import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.auth.wow.libre.domain.ports.in.rol.RolPort;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.SaveAccountWebPort;
import com.auth.wow.libre.infrastructure.conf.Configurations;
import com.auth.wow.libre.infrastructure.conf.comunication.EmailSend;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import com.auth.wow.libre.infrastructure.util.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.auth.wow.libre.infrastructure.entities.AccountWebEntity.mapToModel;

@Service
public class AccountWebService implements AccountWebPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountWebService.class);
    private static final String PICTURE_DEFAULT_PROFILE_WEB = "https://i.ibb.co/M8Kfq9X/icon-Default.png";
    private static final String ROL_CLIENT_DEFAULT = "CLIENT";
    private final SaveAccountWebPort saveAccountWebPort;
    private final ObtainAccountWebPort obtainAccountWebPort;
    private final PasswordEncoder passwordEncoder;
    private final RolPort rolPort;
    private final JwtPort jwtPort;
    private final EmailSend emailSend;

    private final RandomString randomOtp;

    private final RandomString randomString;
    private final Configurations configurations;

    public AccountWebService(SaveAccountWebPort saveAccountWebPort,
                             ObtainAccountWebPort obtainAccountWebPort, PasswordEncoder passwordEncoder,
                             RolPort rolPort, JwtPort jwtPort, EmailSend emailSend,
                             @Qualifier("recover-password") RandomString randomOtp,
                             @Qualifier("random-string") RandomString randomString, Configurations configurations) {
        this.saveAccountWebPort = saveAccountWebPort;
        this.obtainAccountWebPort = obtainAccountWebPort;
        this.passwordEncoder = passwordEncoder;
        this.rolPort = rolPort;
        this.jwtPort = jwtPort;
        this.emailSend = emailSend;
        this.randomOtp = randomOtp;
        this.randomString = randomString;
        this.configurations = configurations;
    }


    @Override
    public AccountWebModel save(AccountWebModel accountWebModel, String transactionId) {
        AccountWebEntity accountWebEntity = AccountWebEntity.fromDomainModel(accountWebModel);
        return mapToModel(saveAccountWebPort.save(accountWebEntity, transactionId));
    }

    @Override
    public AccountWebModel findByEmail(String email, String transactionId) {
        return obtainAccountWebPort.findByEmailAndStatusIsTrue(email).map(AccountWebEntity::mapToModel).orElse(null);
    }

    @Override
    public JwtDto create(AccountWebDto accountWebDto, String transactionId) {

        if (findByEmail(accountWebDto.getEmail(), transactionId) != null) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final String passwordEncode = passwordEncoder.encode(accountWebDto.getPassword());
        final String activationCode = randomString.nextString();
        final String baseUrl = String.format("%s?code=%s&email=%s", configurations.getBaseUrlConfirmationAccount(),
                activationCode, accountWebDto.getEmail());

        final RolModel rolModel = rolPort.findByName(ROL_CLIENT_DEFAULT, transactionId);

        if (rolModel == null) {
            LOGGER.error("An error occurred while assigning a role.  TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("An error occurred while assigning a role.", transactionId);
        }

        AccountWebModel accountWebModel = new AccountWebModel(accountWebDto.getCountry(),
                accountWebDto.getDateOfBirth(), accountWebDto.getFirstName(), accountWebDto.getLastName(),
                accountWebDto.getCellPhone(), accountWebDto.getEmail(), passwordEncode, true, false,
                PICTURE_DEFAULT_PROFILE_WEB, accountWebDto.getLanguage(), randomOtp.nextString(), 0,
                LocalDateTime.now(), rolModel, activationCode);

        final Long id = save(accountWebModel, transactionId).id;

        CustomUserDetails customUserDetails = new CustomUserDetails(
                List.of(rolModel),
                passwordEncode,
                accountWebModel.email,
                true,
                true,
                true,
                true,
                id,
                PICTURE_DEFAULT_PROFILE_WEB,
                accountWebModel.language
        );

        final String token = jwtPort.generateToken(customUserDetails);
        final Date expiration = jwtPort.extractExpiration(token);
        final String refreshToken = jwtPort.generateRefreshToken(customUserDetails);
        final String email = accountWebDto.getEmail();

        emailSend.sendHTMLEmail(MailSenderVars.builder()
                .emailFrom(email)
                .idTemplate(1)
                .data(new RegisterSenderVarsDto("https://docs.google" +
                        ".com/document/d/1Sa7SA2HWTrUB5RLtS6IECBQT_sVvhAsp5nRnnHn-Wm4/edit?usp=drive_link",
                        "Descargar Realmlist", baseUrl,
                        "Confirmar Correo"))
                .subject("Bienvenido, Su cuenta ha sido creada exitosamente, Por favor verifique su correo")
                .transactionId(transactionId).build());

        return new JwtDto(token, refreshToken, expiration, PICTURE_DEFAULT_PROFILE_WEB,
                customUserDetails.getLanguage());
    }

    @Override
    public void recoverPassword(String email, String transactionId) {

        AccountWebModel accountWeb = findByEmail(email, transactionId);

        if (accountWeb == null) {
            throw new NotFoundException("The email provided is not registered or the account is not active.",
                    transactionId);
        }

        final LocalDateTime localDate = accountWeb.dateRecovery;
        Integer retries = accountWeb.recoveryRequests;
        AccountWebEntity accountWebUpdate = AccountWebEntity.fromDomainModel(accountWeb);

        if (retries >= 3) {
            if (localDate.isAfter(LocalDateTime.now())) {
                throw new InternalException("You have exceeded the number of attempts, try again in a few minutes.",
                        transactionId);
            }
            retries = 0;
        }

        final String otp = randomOtp.nextString().toUpperCase();

        accountWebUpdate.setRecoveryRequests(retries + 1);
        accountWebUpdate.setDateRecovery(LocalDateTime.now().plusMinutes(2));
        accountWebUpdate.setOtp(otp);
        saveAccountWebPort.save(accountWebUpdate, transactionId);

        emailSend.sendHTMLEmail(MailSenderVars.builder()
                .data(new OtpSenderVarsDto(otp))
                .emailFrom(email).idTemplate(2).subject("Su codigo de seguridad es")
                .transactionId(transactionId).build());
    }

    @Override
    public void validateOtp(ValidateOtpDto request, String transactionId) {
        final String otp = request.otp.toUpperCase();
        final String resetPassword = randomString.nextString();

        final String password = passwordEncoder.encode(resetPassword);
        final String email = request.email;

        AccountWebModel accountWeb = findByEmail(email, transactionId);

        if (accountWeb == null) {
            throw new NotFoundException("The email provided is not registered or the account is not active.",
                    transactionId);
        }

        if (!accountWeb.otp.equals(otp)) {
            throw new InternalException("The verification code is invalid", transactionId);
        }

        if(accountWeb.dateRecovery.isBefore(LocalDateTime.now())){
            throw new InternalException("Please regenerate a confirmation code, the code is expired", transactionId);
        }

        AccountWebEntity accountWebUpdate = AccountWebEntity.fromDomainModel(accountWeb);
        accountWebUpdate.setPassword(password);
        accountWebUpdate.setOtp(otp);

        saveAccountWebPort.save(accountWebUpdate, transactionId);
        emailSend.sendHTMLEmail(MailSenderVars.builder().emailFrom(email)
                .idTemplate(3).subject("Your password has been reset").data(new PasswordSenderVarsDto(resetPassword))
                .transactionId(transactionId).build());
    }

    @Override
    public void confirmation(String email, String confirmationCode, String transactionId) {
        AccountWebModel accountWeb = findByEmail(email, transactionId);

        if (accountWeb == null) {
            throw new NotFoundException("The email provided is not registered or the account is not active.",
                    transactionId);
        }

        if (!accountWeb.activationCode.equals(confirmationCode)) {
            throw new InternalException("The verification code is invalid", transactionId);
        }

        final String activationCode = randomString.nextString();
        AccountWebEntity accountWebUpdate = AccountWebEntity.fromDomainModel(accountWeb);
        accountWebUpdate.setActivationCode(activationCode);
        accountWebUpdate.setVerified(true);
        saveAccountWebPort.save(accountWebUpdate, transactionId);
    }


}
