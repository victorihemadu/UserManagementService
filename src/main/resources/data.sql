INSERT INTO users (username, password, email, role, created_at, updated_at)
VALUES ('admin', '$2a$10$yfB0G9.VVaHKGjfpXY3B8uQHANOKGRFUnx1VMmFOZJoRJVl5wChRC', 'admin@example.com', 'ADMIN', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO users (username, password, email, role, created_at, updated_at)
VALUES ('user', '$2a$10$yfB0G9.VVaHKGjfpXY3B8uQHANOKGRFUnx1VMmFOZJoRJVl5wChRC', 'user@example.com', 'USER', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());