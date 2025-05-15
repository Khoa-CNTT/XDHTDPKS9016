-- Check if foreign key exists in booking table
SET @fkExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
    WHERE TABLE_NAME = 'booking'
    AND CONSTRAINT_NAME = 'FK_booking_room'
    AND TABLE_SCHEMA = DATABASE()
);

-- Drop foreign key if exists
SET @sql = IF(@fkExists > 0,
    'ALTER TABLE booking DROP FOREIGN KEY FK_booking_room',
    'SELECT "Foreign key does not exist"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check if id_room column exists in booking table
SET @columnExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'booking'
    AND COLUMN_NAME = 'id_room'
    AND TABLE_SCHEMA = DATABASE()
);

-- Drop column if exists
SET @sql = IF(@columnExists > 0,
    'ALTER TABLE booking DROP COLUMN id_room',
    'SELECT "Column does not exist"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Create booking_room table
CREATE TABLE booking_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    number_of_rooms INT NOT NULL,
    room_type_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_booking_room_booking FOREIGN KEY (booking_id) REFERENCES booking(id_booking),
    CONSTRAINT FK_booking_room_room FOREIGN KEY (room_id) REFERENCES room(id_room),
    CONSTRAINT FK_booking_room_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id)
);

-- Add indexes for better performance
CREATE INDEX idx_booking_room_booking_id ON booking_room(booking_id);
CREATE INDEX idx_booking_room_room_id ON booking_room(room_id);
CREATE INDEX idx_booking_room_room_type_id ON booking_room(room_type_id);

-- Add audit columns to booking table
SET @createdAtExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'booking'
    AND COLUMN_NAME = 'created_at'
    AND TABLE_SCHEMA = DATABASE()
);

SET @updatedAtExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'booking'
    AND COLUMN_NAME = 'updated_at'
    AND TABLE_SCHEMA = DATABASE()
);

-- Add created_at if not exists
SET @sql = IF(@createdAtExists = 0,
    'ALTER TABLE booking ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP',
    'SELECT "created_at column already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add updated_at if not exists
SET @sql = IF(@updatedAtExists = 0,
    'ALTER TABLE booking ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
    'SELECT "updated_at column already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
