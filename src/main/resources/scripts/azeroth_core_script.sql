ALTER TABLE acore_auth.account
    ADD COLUMN user_id bigint;

CREATE TABLE acore_auth.users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    status BOOLEAN NOT NULL,
    emulator VARCHAR(30) NOT NULL,
    expansion_id INT NOT NULL,
    realm_id INT(10) UNSIGNED NOT NULL,

    CONSTRAINT uq_config_username UNIQUE (username),
    CONSTRAINT config_realm_id_fk
        FOREIGN KEY (realm_id) REFERENCES acore_auth.realmlist(id)
);


ALTER TABLE acore_characters.characters
    ADD COLUMN dream integer,
    ADD COLUMN hunger integer,
    ADD COLUMN thirst integer;



ALTER TABLE acore_characters.guild
    ADD COLUMN public_access boolean,
    ADD COLUMN discord       text,
    ADD COLUMN multi_faction boolean;

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
    emulator VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT character_transaction_reference_uq UNIQUE (reference)
);

