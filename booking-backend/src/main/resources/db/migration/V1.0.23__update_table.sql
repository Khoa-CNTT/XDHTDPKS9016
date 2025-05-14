-- Check if version column exists in bill table
SET @billVersionExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'bill'
    AND COLUMN_NAME = 'version'
    AND TABLE_SCHEMA = DATABASE()
);

SET @bookingVersionExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'booking'
    AND COLUMN_NAME = 'version'
    AND TABLE_SCHEMA = DATABASE()
);

-- Add version column to bill table if it doesn't exist
SET @sql = IF(@billVersionExists = 0,
    'ALTER TABLE bill ADD COLUMN version BIGINT DEFAULT 0',
    'SELECT "Version column already exists in bill table"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add version column to booking table if it doesn't exist
SET @sql = IF(@bookingVersionExists = 0,
    'ALTER TABLE booking ADD COLUMN version BIGINT DEFAULT 0',
    'SELECT "Version column already exists in booking table"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Update existing records to have version = 0
UPDATE bill SET version = 0 WHERE version IS NULL;
UPDATE booking SET version = 0 WHERE version IS NULL;

-- Add not null constraint after setting default values
ALTER TABLE bill MODIFY COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE booking MODIFY COLUMN version BIGINT NOT NULL DEFAULT 0;