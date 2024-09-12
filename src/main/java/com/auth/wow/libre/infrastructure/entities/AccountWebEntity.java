package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.AccountWebModel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "account_web")
public class AccountWebEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    private Boolean status;
    private Boolean verified;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String language;

    @JoinColumn(
            name = "rol_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RolEntity rolId;

    private String otp;

    @Column(name = "recovery_requests")
    private Integer recoveryRequests;

    @Column(name = "date_recovery")
    private LocalDateTime dateRecovery;

    @Column(name = "activation_code")
    private String activationCode;

    public AccountWebEntity() {
    }


    public AccountWebEntity(Long id, String country, LocalDate dateOfBirth, String firstName, String lastName,
                            String cellPhone, String password, String email, RolEntity rolId, Boolean status,
                            Boolean verified,
                            String avatarUrl, String language, String otp, Integer recoveryRequests,
                            LocalDateTime dateRecovery, String activationCode) {
        this.id = id;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.password = password;
        this.email = email;
        this.rolId = rolId;
        this.status = status;
        this.verified = verified;
        this.avatarUrl = avatarUrl;
        this.language = language;
        this.otp = otp;
        this.recoveryRequests = recoveryRequests;
        this.dateRecovery = dateRecovery;
        this.activationCode = activationCode;
    }


    public static AccountWebEntity fromDomainModel(AccountWebModel accountWebModel) {
        return new AccountWebEntity(accountWebModel.id, accountWebModel.country, accountWebModel.dateOfBirth,
                accountWebModel.firstName,
                accountWebModel.lastName,
                accountWebModel.cellPhone, accountWebModel.password, accountWebModel.email,
                RolEntity.mapToAccountRolEntity(accountWebModel.rol),
                accountWebModel.status,
                accountWebModel.verified, accountWebModel.avatarUrl, accountWebModel.language,
                accountWebModel.otp, accountWebModel.recoveryRequests, accountWebModel.dateRecovery,
                accountWebModel.activationCode);
    }

    public static AccountWebModel mapToModel(AccountWebEntity accountWebEntity) {
        return new AccountWebModel(accountWebEntity.getId(), accountWebEntity.getCountry(),
                accountWebEntity.getDateOfBirth(),
                accountWebEntity.getFirstName(), accountWebEntity.getLastName(), accountWebEntity.getCellPhone(),
                accountWebEntity.getEmail(), accountWebEntity.getPassword(), accountWebEntity.getRolId().getId(),
                accountWebEntity.getRolId().getName(), accountWebEntity.getStatus(), accountWebEntity.getVerified(),
                accountWebEntity.getAvatarUrl(), accountWebEntity.getLanguage(), accountWebEntity.getOtp(),
                accountWebEntity.getRecoveryRequests(), accountWebEntity.getDateRecovery(),
                RolEntity.mapToModel(accountWebEntity.getRolId()), accountWebEntity.getActivationCode());
    }

}
