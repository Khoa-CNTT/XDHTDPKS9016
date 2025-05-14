-- V1__booking_relink_room_idempotent.sql

DELIMITER $$

CREATE PROCEDURE safe_relink_rating()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;

    -- 1. Drop FK nếu tồn tại
    IF EXISTS (
        SELECT 1
        FROM information_schema.TABLE_CONSTRAINTS
        WHERE CONSTRAINT_SCHEMA = DATABASE()
          AND TABLE_NAME      = 'rating'
          AND CONSTRAINT_NAME = 'fk_rating_room_type'
    ) THEN
        ALTER TABLE `rating`
            DROP FOREIGN KEY `fk_rating_room_type`;
    END IF;

    -- 2. Thêm cột id_room nếu chưa có
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME   = 'rating'
          AND COLUMN_NAME  = 'id_room'
    ) THEN
        ALTER TABLE `rating`
            ADD COLUMN `id_room` BIGINT NULL;
    END IF;

    -- 3. Copy data cũ
    UPDATE `rating`
    SET `id_room` = `room_type_id`
    WHERE `room_type_id` IS NOT NULL;

    -- 4. Drop column cũ nếu tồn tại
    IF EXISTS (
        SELECT 1
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME   = 'rating'
          AND COLUMN_NAME  = 'room_type_id'
    ) THEN
        ALTER TABLE `booking`
            DROP COLUMN `room_type_id`;
    END IF;
END$$

DELIMITER ;

-- Gọi và xóa procedure
CALL safe_relink_rating();
DROP PROCEDURE safe_relink_rating;
