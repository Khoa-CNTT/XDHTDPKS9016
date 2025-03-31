USE booking_management;

CREATE TABLE ACCOUNT
(
    account_id   INT AUTO_INCREMENT PRIMARY KEY ,
    username VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE ROLE
(
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(10)
);

CREATE TABLE ROLE_ACCOUNT
(
    account_id INT,
    role_id INT,
    PRIMARY KEY (account_id,role_id),
    FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id),
    FOREIGN KEY (role_id) REFERENCES ROLE(role_id)
)
