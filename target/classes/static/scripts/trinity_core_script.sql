

ALTER TABLE auth.account
    ADD COLUMN user_id bigint;

CREATE TABLE characters.character_transaction
(
    id               bigint auto_increment NOT NULL,
    character_id     bigint  NOT NULL,
    account_id       bigint  NOT NULL,
    user_id          bigint  NOT NULL,
    amount           bigint  NOT NULL,
    command          text,
    successful       boolean NOT NULL,
    transaction_id   text,
    indebtedness     boolean NOT NULL,
    transaction_date date    NOT NULL,
    status           boolean NOT NULL,
    PRIMARY KEY (id)
);



