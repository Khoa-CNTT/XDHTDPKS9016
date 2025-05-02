USE booking_management;

ALTER TABLE hotel
    ADD CONSTRAINT FK_HOTEL_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (account_id);