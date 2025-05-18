-- 1. Kiểm tra xem cột đã tồn tại chưa
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME   = 'room'
  AND COLUMN_NAME  = 'number_rooms';

-- 2. Nếu chưa có thì ALTER
SET @sql = IF(
        @col_exists = 0,
        'ALTER TABLE `room` ADD COLUMN `number_rooms` INT NULL;',
        'SELECT "Column already exists.";'
           );

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;