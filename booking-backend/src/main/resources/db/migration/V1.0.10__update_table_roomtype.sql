USE booking_management;
# xóa ràng buộc
ALTER TABLE comment
    DROP FOREIGN KEY comment_ibfk_1;
ALTER TABLE rating
    DROP FOREIGN KEY rating_ibfk_1;
ALTER TABLE booking
    DROP FOREIGN KEY booking_ibfk_1;

# xóa room
DROP TABLE room;

# xóa room_id
ALTER TABLE comment
    DROP COLUMN room_id;
ALTER TABLE rating
    DROP COLUMN room_id;
ALTER TABLE booking
    DROP COLUMN room_id;

#tạo bảng room_type
CREATE TABLE room_type
(
    room_type_id   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    type_name      VARCHAR(255)                      NOT NULL,
    number_bed     INT                               NOT NULL,
    maximum_people INT                               NOT NULL,
    price          DECIMAL(10, 2)                    NOT NULL,
    description    VARCHAR(255),
    room_image     VARCHAR(255),
    available_room INT DEFAULT 0,
    status         VARCHAR(50),
    hotel_id       BIGINT                            NOT NULL,
    CONSTRAINT fk_room_type_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (hotel_id)
);
#ràng buộc
ALTER TABLE rating ADD COLUMN room_type_id BIGINT;
ALTER TABLE rating ADD CONSTRAINT fk_rating_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id);

ALTER TABLE comment ADD COLUMN room_type_id BIGINT;
ALTER TABLE comment ADD CONSTRAINT fk_comment_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id);

ALTER TABLE booking ADD COLUMN room_type_id BIGINT;
ALTER TABLE booking ADD CONSTRAINT fk_booking_room_type FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id);
