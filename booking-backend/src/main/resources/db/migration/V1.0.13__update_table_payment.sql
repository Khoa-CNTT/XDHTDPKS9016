-- 1. DROP các cột nếu có
ALTER TABLE payment
    DROP COLUMN name_client,
DROP COLUMN account_number,
  DROP COLUMN payment_type;

-- 2. ADD các cột mới
ALTER TABLE payment
    ADD COLUMN `payment_method`   VARCHAR(255) NULL,
    ADD COLUMN `transaction_id`   VARCHAR(255) NULL;