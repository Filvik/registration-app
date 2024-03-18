INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at)
VALUES ('John Doe', 'john@example.com', 'hash1', '+79031234567', NOW(), NOW());
INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at)
VALUES ('Peter Parker', 'peter@example.com', 'hash2', '+79037654321', NOW(), NOW());
INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at)
VALUES ('Bruce Wayne', 'bruce@example.com', 'hash3', '+79031234444', NOW(), NOW());

INSERT INTO roles (role_name) VALUES ('User');
INSERT INTO roles (role_name) VALUES ('Operator');
INSERT INTO roles (role_name) VALUES ('Administrator');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3);
