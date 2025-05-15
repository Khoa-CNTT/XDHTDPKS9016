-- Check if version column exists in bill table
SET @versionColumnExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'bill'
    AND COLUMN_NAME = 'version'
    AND TABLE_SCHEMA = DATABASE()
);

-- Add version column if it doesn't exist
SET @sql = IF(@versionColumnExists = 0,
    'ALTER TABLE bill ADD COLUMN version BIGINT DEFAULT 0',
    'SELECT "version column already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add booking_id foreign key if needed
SET @fkExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
    WHERE TABLE_NAME = 'bill'
    AND COLUMN_NAME = 'id_booking'
    AND CONSTRAINT_NAME = 'FK_bill_booking'
    AND TABLE_SCHEMA = DATABASE()
);

-- Add foreign key if it doesn't exist
SET @sql = IF(@fkExists = 0,
    'ALTER TABLE bill ADD CONSTRAINT FK_bill_booking FOREIGN KEY (id_booking) REFERENCES booking(id_booking)',
    'SELECT "Foreign key already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check if payment table has foreign key to bill
SET @paymentFkExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
    WHERE TABLE_NAME = 'payment'
    AND COLUMN_NAME = 'bill_id'
    AND CONSTRAINT_NAME = 'FK_payment_bill'
    AND TABLE_SCHEMA = DATABASE()
);

-- Add payment foreign key if it doesn't exist
SET @sql = IF(@paymentFkExists = 0,
    'ALTER TABLE payment ADD CONSTRAINT FK_payment_bill FOREIGN KEY (bill_id) REFERENCES bill(bill_id)',
    'SELECT "Payment foreign key already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check if payment table has foreign key to booking
SET @paymentBookingFkExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
    WHERE TABLE_NAME = 'payment'
    AND COLUMN_NAME = 'booking_id'
    AND CONSTRAINT_NAME = 'FK_payment_booking'
    AND TABLE_SCHEMA = DATABASE()
);

-- Check if booking_id column exists in payment table
SET @bookingIdColumnExists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'payment'
    AND COLUMN_NAME = 'booking_id'
    AND TABLE_SCHEMA = DATABASE()
);

-- Add booking_id column if it doesn't exist 
SET @sql = IF(@bookingIdColumnExists = 0,
    'ALTER TABLE payment ADD COLUMN booking_id BIGINT',
    'SELECT "booking_id column already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add payment-booking foreign key if it doesn't exist
SET @sql = IF(@paymentBookingFkExists = 0 AND @bookingIdColumnExists > 0,
    'ALTER TABLE payment ADD CONSTRAINT FK_payment_booking FOREIGN KEY (booking_id) REFERENCES booking(id_booking)',
    'SELECT "Payment-booking foreign key already exists or booking_id column missing"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check if indexes exist and create if needed
-- Check and create index on bill(id_booking)
SET @billBookingIndexExists = (
    SELECT COUNT(1) IndexIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = DATABASE()
    AND table_name = 'bill'
    AND index_name = 'idx_bill_booking'
);

SET @sql = IF(@billBookingIndexExists = 0, 
    'CREATE INDEX idx_bill_booking ON bill(id_booking)',
    'SELECT "idx_bill_booking already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and create index on payment(bill_id)
SET @paymentBillIndexExists = (
    SELECT COUNT(1) IndexIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = DATABASE()
    AND table_name = 'payment'
    AND index_name = 'idx_payment_bill'
);

SET @sql = IF(@paymentBillIndexExists = 0, 
    'CREATE INDEX idx_payment_bill ON payment(bill_id)',
    'SELECT "idx_payment_bill already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and create index on payment(booking_id)
SET @paymentBookingIndexExists = (
    SELECT COUNT(1) IndexIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = DATABASE()
    AND table_name = 'payment'
    AND index_name = 'idx_payment_booking'
);

SET @sql = IF(@paymentBookingIndexExists = 0 AND @bookingIdColumnExists > 0, 
    'CREATE INDEX idx_payment_booking ON payment(booking_id)',
    'SELECT "idx_payment_booking already exists or booking_id column missing"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and create index on payment(transaction_id)
SET @paymentTransactionIndexExists = (
    SELECT COUNT(1) IndexIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = DATABASE()
    AND table_name = 'payment'
    AND index_name = 'idx_payment_transaction'
);

SET @sql = IF(@paymentTransactionIndexExists = 0, 
    'CREATE INDEX idx_payment_transaction ON payment(transaction_id)',
    'SELECT "idx_payment_transaction already exists"'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
