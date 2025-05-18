-- Bảng hotel
SET @col_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'hotel'
      AND COLUMN_NAME = 'description'
);

SET @sql := IF(
        @col_exists > 0,
        'ALTER TABLE hotel MODIFY COLUMN description TEXT;',
        'SELECT "Column description does not exist in hotel.";'
            );

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Bảng service
SET @col_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'service'
      AND COLUMN_NAME = 'description'
);

SET @sql := IF(
        @col_exists > 0,
        'ALTER TABLE service MODIFY COLUMN description TEXT;',
        'SELECT "Column description does not exist in service.";'
            );

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Bảng room_type
SET @col_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'room_type'
      AND COLUMN_NAME = 'description'
);

SET @sql := IF(
        @col_exists > 0,
        'ALTER TABLE room_type MODIFY COLUMN description TEXT;',
        'SELECT "Column description does not exist in room_type.";'
            );

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
