
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

ALTER TABLE acore_auth.account
    ADD COLUMN user_id bigint;






