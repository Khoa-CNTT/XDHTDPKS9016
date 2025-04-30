-- V3__migrate_room_to_room_type.sql

-- 1. Safe‐drop các FK, column cũ và table room
DELIMITER $$

CREATE PROCEDURE safe_cleanup_old_schema()
BEGIN
    -- Nếu gặp bất kỳ lỗi nào (FK/column/table không tồn tại), vẫn tiếp tục
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
    END;

    -- 1.1 Drop old foreign keys
    ALTER TABLE comment
        DROP FOREIGN KEY comment_ibfk_1;
    ALTER TABLE rating
        DROP FOREIGN KEY rating_ibfk_1;
    ALTER TABLE booking
        DROP FOREIGN KEY booking_ibfk_1;

    -- 1.2 Drop old room_id columns
    ALTER TABLE comment
        DROP COLUMN room_id;
    ALTER TABLE rating
        DROP COLUMN room_id;
    ALTER TABLE booking
        DROP COLUMN room_id;

    -- 1.3 Drop old room table
    DROP TABLE room;
END$$

DELIMITER ;

-- Gọi procedure để thực hiện cleanup
CALL safe_cleanup_old_schema();

-- Xóa procedure sau khi dùng
DROP PROCEDURE safe_cleanup_old_schema;

-- 2. Tạo bảng room_type mới (nếu chưa có)
CREATE TABLE IF NOT EXISTS room_type
(
    room_type_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name      VARCHAR(255),
    number_bed     INT,
    maximum_people INT,
    price          DECIMAL(10, 2),
    description    VARCHAR(255),
    room_image     VARCHAR(255),
    available_room INT DEFAULT 0,
    status         VARCHAR(50),
    hotel_id       BIGINT,
    CONSTRAINT fk_room_type_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (hotel_id)
);

-- 3. Thêm cột room_type_id và FK mới vào các bảng con
--    Nếu đã chạy rồi, các lệnh này sẽ ném lỗi và bị procedure bắt bỏ qua
DELIMITER $$

CREATE PROCEDURE safe_add_room_type_fks()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
    END;

    -- 3.1 rating
    ALTER TABLE rating
        ADD COLUMN room_type_id BIGINT;
    ALTER TABLE rating
        ADD CONSTRAINT fk_rating_room_type
            FOREIGN KEY (room_type_id) REFERENCES room_type (room_type_id);

    -- 3.2 comment
    ALTER TABLE comment
        ADD COLUMN room_type_id BIGINT;
    ALTER TABLE comment
        ADD CONSTRAINT fk_comment_room_type
            FOREIGN KEY (room_type_id) REFERENCES room_type (room_type_id);

    -- 3.3 booking
    ALTER TABLE booking
        ADD COLUMN room_type_id BIGINT;
    ALTER TABLE booking
        ADD CONSTRAINT fk_booking_room_type
            FOREIGN KEY (room_type_id) REFERENCES room_type (room_type_id);
END$$

DELIMITER ;

CALL safe_add_room_type_fks();
DROP PROCEDURE safe_add_room_type_fks;
