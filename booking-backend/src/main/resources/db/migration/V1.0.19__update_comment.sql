-- Vx__safe_drop_comment_fk_and_column.sql

DELIMITER $$

CREATE PROCEDURE safe_cleanup_comment()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;

    -- 1. Nếu FK tồn tại thì DROP
    IF EXISTS (
        SELECT 1
        FROM information_schema.TABLE_CONSTRAINTS
        WHERE CONSTRAINT_SCHEMA = DATABASE()
          AND TABLE_NAME        = 'comment'
          AND CONSTRAINT_NAME   = 'fk_comment_room_type'
    ) THEN
        ALTER TABLE `comment`
            DROP FOREIGN KEY `fk_comment_room_type`;
    END IF;

    -- 2. Nếu cột room_type_id tồn tại thì DROP
    IF EXISTS (
        SELECT 1
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME   = 'comment'
          AND COLUMN_NAME  = 'room_type_id'
    ) THEN
        ALTER TABLE `comment`
            DROP COLUMN `room_type_id`;
    END IF;

END$$
DELIMITER ;

-- Gọi procedure và sau đó xóa nó
CALL safe_cleanup_comment();
DROP PROCEDURE safe_cleanup_comment;
