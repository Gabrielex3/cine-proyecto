CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               receptor VARCHAR(255) NOT NULL,
                               asunto VARCHAR(255) NOT NULL,
                               message TEXT NOT NULL,
                               status VARCHAR(50) NOT NULL,
                               sent_at DATETIME NOT NULL
);