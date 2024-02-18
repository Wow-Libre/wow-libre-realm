


CREATE TABLE auth.account_web(
                                 id bigint AUTO_INCREMENT  NOT NULL,
                                 country varchar(30) NOT NULL,
                                 date_of_birth DATE NOT NULL,
                                 first_name varchar (30) NOT NULL,
                                 last_name varchar (30) NOT NULL,
                                 cell_phone varchar (20) NOT NULL,
                                 password text not null,

                                 PRIMARY KEY  (id)
);


ALTER TABLE auth.account
    ADD COLUMN account_web bigint,
 ADD CONSTRAINT FOREIGN KEY (account_web) REFERENCES auth.account_web (id);



CREATE TABLE auth.product(
                             id bigint AUTO_INCREMENT  NOT NULL,
                             name varchar(50) NOT NULL,
                             description text NOT NULL,
                             img_url text NOT NULL,
                             price bigint NOT NULL,
                             status boolean NOT NULL,
                             discount bigint NOT NULL,
                             reference_number varchar(50) NOT NULL,
                             item_code varchar(30) NOT NULL,
                             PRIMARY KEY  (id),
                             CONSTRAINT  reference_number_uq UNIQUE (reference_number)
);