alter table payment
    drop column status;

-- Add status column to payment table
ALTER TABLE payment ADD COLUMN status VARCHAR(20) DEFAULT 'PENDING';

