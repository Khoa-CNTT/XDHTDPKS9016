USE booking_management;

CREATE TABLE invalid_token
(
    id          VARCHAR(255) PRIMARY KEY NOT NULL,
    expiry_time DATETIME
);

-- Nếu cần đảm bảo event_scheduler đang bật (lưu ý: cần quyền SUPER)
SET GLOBAL event_scheduler = ON;

-- Tạo event tự động xóa các row hết hạn
CREATE EVENT IF NOT EXISTS ev_cleanup_invalid_token
    ON SCHEDULE EVERY 1 DAY
    DO
    DELETE
    FROM invalid_token
    WHERE expiry_time < NOW() - INTERVAL 1 DAY;


