


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