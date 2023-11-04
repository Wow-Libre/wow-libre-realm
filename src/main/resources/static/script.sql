ALTER TABLE auth.account
    ADD COLUMN country varchar(30);

ALTER TABLE auth.account
    ADD COLUMN date_of_birth DATE;

ALTER TABLE auth.account
    ADD COLUMN first_name varchar(30);

ALTER TABLE auth.account
    ADD COLUMN last_name varchar(30);

ALTER TABLE auth.account
    ADD COLUMN cell_phone varchar(20);


