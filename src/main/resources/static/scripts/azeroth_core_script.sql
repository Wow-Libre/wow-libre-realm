
CREATE TABLE acore_auth.rol
(
    id     bigint AUTO_INCREMENT NOT NULL,
    name   varchar(50) NOT NULL,
    status boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT name_uq UNIQUE (name)
);

INSERT INTO acore_auth.rol (name, status)
VALUES ('ADMIN', TRUE);
INSERT INTO acore_auth.rol (name, status)
VALUES ('CLIENT', TRUE);
INSERT INTO acore_auth.rol (name, status)
VALUES ('SUPPORT', TRUE);



CREATE TABLE acore_auth.account_web
(
    id            bigint auto_increment NOT NULL,
    country       varchar(30) NOT NULL,
    date_of_birth DATE        NOT NULL,
    first_name    varchar(30) NOT NULL,
    last_name     varchar(30) NOT NULL,
    cell_phone    varchar(20) NOT NULL,
    password      text        not null,
    status        boolean,
    verified      boolean,
    avatar_url    text,
    email         varchar(40) NOT NULL,
    rol_id        BIGINT      NOT null,
    CONSTRAINT uq_email UNIQUE (email),
    CONSTRAINT fk_account_web_rol_id FOREIGN KEY (rol_id) REFERENCES acore_auth.rol (id),
    PRIMARY KEY (id)
);


ALTER TABLE acore_auth.account
    ADD COLUMN account_web bigint,
 ADD CONSTRAINT FOREIGN KEY (account_web) REFERENCES acore_auth.account_web (id);

ALTER TABLE acore_auth.account_web
    ADD COLUMN language varchar(10);


ALTER TABLE acore_auth.account_web
    ADD COLUMN otp varchar (5),
    ADD COLUMN recovery_requests integer,
    ADD COLUMN date_recovery DATETIME;


ALTER TABLE acore_auth.account_web
    ADD COLUMN activation_code  varchar (15);




