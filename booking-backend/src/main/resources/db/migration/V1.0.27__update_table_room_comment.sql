-- Drop table if exists
DROP TABLE IF EXISTS room_comment;

-- Create room_comment table
CREATE TABLE room_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    comment_id BIGINT NOT NULL,
    CONSTRAINT FK_room_comment_room FOREIGN KEY (room_id) REFERENCES room(id_room),
    CONSTRAINT FK_room_comment_comment FOREIGN KEY (comment_id) REFERENCES comment(comment_id),
    UNIQUE KEY UK_room_comment (room_id, comment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes
CREATE INDEX idx_room_comment_room ON room_comment(room_id);
CREATE INDEX idx_room_comment_comment ON room_comment(comment_id);
