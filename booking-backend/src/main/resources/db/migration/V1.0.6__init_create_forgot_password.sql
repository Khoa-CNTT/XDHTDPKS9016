USE booking_management;

CREATE TABLE forgot_password
(
    fpid               INT AUTO_INCREMENT NOT NULL,
    otp                INT                NULL,
    expiration_time    datetime           NULL,
    account_id BIGINT             NULL,
    CONSTRAINT pk_forgot_password PRIMARY KEY (fpid)
);

ALTER TABLE forgot_password
    ADD CONSTRAINT FK_FORGOT_PASSWORD_ON_ACCOUNT_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (account_id);


