-- Vx__alter_room_type.sql

DELIMITER $$
CREATE PROCEDURE safe_alter_room_type()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
    END;

    -- 1. Thêm cột number_room nếu chưa tồn tại
    IF NOT EXISTS (SELECT 1
                   FROM information_schema.COLUMNS
                   WHERE TABLE_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'room_type'
                     AND COLUMN_NAME = 'number_room') THEN
        ALTER TABLE `room_type`
            ADD COLUMN `number_room` INT NULL AFTER `number_bed`;
    END IF;

    -- 2. Xóa các cột cũ nếu tồn tại
    IF EXISTS (SELECT 1
               FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE()
                 AND TABLE_NAME = 'room_type'
                 AND COLUMN_NAME = 'maximum_people') THEN
        ALTER TABLE `room_type`
            DROP COLUMN `maximum_people`;
    END IF;

    IF EXISTS (SELECT 1
               FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE()
                 AND TABLE_NAME = 'room_type'
                 AND COLUMN_NAME = 'price') THEN
        ALTER TABLE `room_type`
            DROP COLUMN `price`;
    END IF;

    IF EXISTS (SELECT 1
               FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE()
                 AND TABLE_NAME = 'room_type'
                 AND COLUMN_NAME = 'available_room') THEN
        ALTER TABLE `room_type`
            DROP COLUMN `available_room`;
    END IF;

    IF EXISTS (SELECT 1
               FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE()
                 AND TABLE_NAME = 'room_type'
                 AND COLUMN_NAME = 'status') THEN
        ALTER TABLE `room_type`
            DROP COLUMN `status`;
    END IF;

END$$
DELIMITER ;

-- Gọi procedure để thực thi các ALTER TABLE an toàn
CALL safe_alter_room_type();
-- Xóa procedure sau khi dùng
DROP PROCEDURE safe_alter_room_type;

-- 3. Tạo bảng `room` nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS `room`
(
    `id_room`      BIGINT AUTO_INCREMENT NOT NULL,
    `number_bed`   INT                   NULL,
    `price`        DECIMAL(10, 2)        NULL,
    `room_type_id` BIGINT                NULL,
    `user_id`      BIGINT                NULL,
    `status`       VARCHAR(50)           NULL,
    CONSTRAINT `pk_room` PRIMARY KEY (`id_room`),
    CONSTRAINT `FK_ROOM_ON_ROOM_TYPE`
        FOREIGN KEY (`room_type_id`)
            REFERENCES `room_type` (`room_type_id`),
    CONSTRAINT `FK_ROOM_ON_USER`
        FOREIGN KEY (`user_id`)
            REFERENCES `user_profile` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
