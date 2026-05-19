CREATE TABLE tickets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         booking_id BIGINT NOT NULL,
                         payment_id BIGINT NOT NULL,
                         issue_date DATETIME NOT NULL,
                         CONSTRAINT uk_booking_id UNIQUE (booking_id)
);