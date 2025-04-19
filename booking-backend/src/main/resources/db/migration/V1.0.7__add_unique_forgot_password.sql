USE booking_management;

-- 1) Drop foreign key constraint (tên constraint này bạn có thể lấy từ SHOW CREATE TABLE)
ALTER TABLE forgot_password
    DROP FOREIGN KEY FK_FORGOT_PASSWORD_ON_ACCOUNT_ACCOUNT;

-- 2) Drop index backing cho FK
ALTER TABLE forgot_password
    DROP INDEX FK_FORGOT_PASSWORD_ON_ACCOUNT_ACCOUNT;

-- 3) Thêm unique constraint
ALTER TABLE forgot_password
    ADD CONSTRAINT uc_forgot_password_account UNIQUE (account_id);

-- 4) Tạo lại foreign key nếu bạn vẫn muốn giữ ràng buộc
ALTER TABLE forgot_password
    ADD CONSTRAINT FK_FORGOT_PASSWORD_ON_ACCOUNT_ACCOUNT
        FOREIGN KEY (account_id) REFERENCES account(account_id);