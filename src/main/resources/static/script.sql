/* ACCOUNT WEB */
/*
  DESCRIPTION : Responsible for managing the system's web services.
*/
  CREATE TABLE auth.account_web
 */
(
    id            bigint AUTO_INCREMENT NOT NULL,
    country       varchar(30) NOT NULL,
    date_of_birth DATE        NOT NULL,
    first_name    varchar(30) NOT NULL,
    last_name     varchar(30) NOT NULL,
    cell_phone    varchar(20) NOT NULL,
    password      text        not null,
    email         varchar(40) NOT NULL,
    ADD
        CONSTRAINT uq_email UNIQUE (email),
    PRIMARY KEY (id)
);


ALTER TABLE auth.account
    ADD COLUMN account_web bigint,
 ADD CONSTRAINT FOREIGN KEY (account_web) REFERENCES auth.account_web (id);



CREATE TABLE auth.product
(
    id               bigint AUTO_INCREMENT NOT NULL,
    name             varchar(50) NOT NULL,
    description      text        NOT NULL,
    img_url          text        NOT NULL,
    price            bigint      NOT NULL,
    status           boolean     NOT NULL,
    discount         bigint      NOT NULL,
    reference_number varchar(50) NOT NULL,
    item_code        varchar(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT reference_number_uq UNIQUE (reference_number)
);


CREATE TABLE auth.rol(
                         id bigint AUTO_INCREMENT  NOT NULL,
                         name varchar(50) NOT NULL,
                         status boolean NOT NULL,
                         PRIMARY KEY  (id),
                         CONSTRAINT  name_uq UNIQUE (name)
);


ALTER TABLE AUTH.account_web
    ADD COLUMN rol_id BIGINT NOT NULL,
ADD CONSTRAINT fk_account_web_rol_id FOREIGN KEY (rol_id) REFERENCES AUTH.rol(id);


INSERT INTO auth.rol (name, status) VALUES ('ADMIN', TRUE);
INSERT INTO auth.rol (name, status) VALUES ('CLIENT', TRUE);
INSERT INTO auth.rol (name, status) VALUES ('SUPPORT', TRUE);
