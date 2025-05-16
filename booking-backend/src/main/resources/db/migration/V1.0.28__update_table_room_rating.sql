-- Drop existing tables if they exist
DROP TABLE IF EXISTS room_rating;

-- Create room_rating table
CREATE TABLE room_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    rating_id BIGINT NOT NULL,
    CONSTRAINT FK_room_rating_room FOREIGN KEY (room_id) REFERENCES room(id_room),
    CONSTRAINT FK_room_rating_rating FOREIGN KEY (rating_id) REFERENCES rating(rating_id),
    UNIQUE KEY UK_room_rating (room_id, rating_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes for room_rating
CREATE INDEX idx_room_rating_room ON room_rating(room_id);
CREATE INDEX idx_room_rating_rating ON room_rating(rating_id);

