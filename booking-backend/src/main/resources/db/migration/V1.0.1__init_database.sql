USE booking_management;

CREATE TABLE account
(
    account_id BIGINT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username   VARCHAR(255)                    NULL,
    password   VARCHAR(255)                    NULL
);

CREATE TABLE `role`
(
    role_id   BIGINT  AUTO_INCREMENT NOT NULL,
    role_name VARCHAR(255)        NULL,
    PRIMARY KEY (role_id)
);

CREATE TABLE role_account
(
    account_id BIGINT  NOT NULL,
    role_id    BIGINT  NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account (account_id),
    FOREIGN KEY (role_id) REFERENCES `role` (role_id)
);

CREATE TABLE user_profile
(
    user_id    BIGINT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
    full_name  NVARCHAR(50)                    NULL,
    gender     VARCHAR(20)                     NULL,
    address    NVARCHAR(255)                   NULL,
    email      VARCHAR(255)                    NULL,
    phone      NVARCHAR(20)                    NULL,
    birth_date datetime                        NULL,
    status     NVARCHAR(255)                   NULL,
    account_id BIGINT                            NULL,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE hotel
(
    hotel_id      BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)        NULL,
    image         VARCHAR(255)        NULL,
    address       VARCHAR(255)        NULL,
    hotline       VARCHAR(255)        NULL,
    `description` VARCHAR(255)        NULL,
    CONSTRAINT pk_hotel PRIMARY KEY (hotel_id)
);

CREATE TABLE room
(
    room_id        BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    room_number    VARCHAR(255)                    NULL,
    room_type      VARCHAR(255)                    NULL,
    number_bed     INT                             NULL,
    maximum_people INT                             NULL,
    price          DECIMAL                         NULL,
    room_image     VARCHAR(255)                    NULL,
    `description`  VARCHAR(255)                    NULL,
    status         VARCHAR(255)                    NULL,
    hotel_id       BIGINT                            NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotel (hotel_id)
);

CREATE TABLE service
(
    service_id    BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    service_name  VARCHAR(255)                    NULL,
    service_price DECIMAL                         NULL,
    service_image VARCHAR(255)                    NULL,
    `description` NVARCHAR(255)                   NULL,
    hotel_id      BIGINT                            NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotel (hotel_id)
);

CREATE TABLE booking
(
    id_booking     BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    check_in_date  date                            NULL,
    check_out_date date                            NULL,
    check_in_time  time                            NULL,
    check_out_time time                            NULL,
    number_people  INT                             NULL,
    status         NVARCHAR(255)                   NULL,
    room_id        BIGINT                            NOT NULL,
    user_id        BIGINT                            NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (room_id),
    FOREIGN KEY (user_id) REFERENCES user_profile (user_id)
);

CREATE TABLE booking_service
(
    booking_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    PRIMARY KEY (booking_id, service_id),
    FOREIGN KEY (booking_id) REFERENCES booking (id_booking),
    FOREIGN KEY (service_id) REFERENCES service (service_id)
);

CREATE TABLE bill
(
    bill_id      BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    total_amount DECIMAL                         NULL,
    deposit      DECIMAL                         NULL,
    print_date   date                            NULL,
    print_time   time                            NULL,
    id_booking   BIGINT                            NULL,
    FOREIGN KEY (id_booking) REFERENCES booking (id_booking)
);

CREATE TABLE payment
(
    payment_id   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    payment_type NVARCHAR(255)                   NULL,
    amount       DECIMAL                         NULL,
    payment_date date                            NULL,
    payment_time time                            NULL,
    status       NVARCHAR(255)                   NULL,
    bill_id      BIGINT                            NULL,
    FOREIGN KEY (bill_id) REFERENCES bill (bill_id)
);

CREATE TABLE rating
(
    rating_id   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    score       INT                             NULL,
    rating_date date                            NULL,
    rating_time time                            NULL,
    user_id     BIGINT                            NOT NULL,
    room_id     BIGINT                            NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (room_id),
    FOREIGN KEY (user_id) REFERENCES user_profile (user_id)
);

CREATE TABLE comment
(
    comment_id   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    content      NVARCHAR(255)                   NULL,
    comment_date date                            NULL,
    comment_time time                            NULL,
    user_id      BIGINT                            NOT NULL,
    room_id      BIGINT                            NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (room_id),
    FOREIGN KEY (user_id) REFERENCES user_profile (user_id)
);

