CREATE TABLE acore_auth.rol
(
    id     bigint AUTO_INCREMENT NOT NULL,
    name   varchar(50)           NOT NULL,
    status boolean               NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT name_uq UNIQUE (name)
);

INSERT INTO acore_auth.rol (name, status)
VALUES ('ADMIN', TRUE);

ALTER TABLE acore_auth.account
    ADD COLUMN user_id bigint;

CREATE TABLE acore_characters.character_transaction
(
    id               bigint auto_increment NOT NULL,
    character_id     bigint                NOT NULL,
    account_id       bigint                NOT NULL,
    user_id          bigint                NOT NULL,
    amount           bigint                NOT NULL,
    command          text,
    successful       boolean               NOT NULL,
    transaction_id   text,
    indebtedness     boolean               NOT NULL,
    transaction_date date                  NOT NULL,
    reference        varchar(50)           NOT NULL,
    status           boolean               NOT NULL,
    transaction_type varchar(60)           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT character_transaction_reference_uq UNIQUE (reference)
);



ALTER TABLE acore_characters.guild
    ADD COLUMN public_access boolean;
