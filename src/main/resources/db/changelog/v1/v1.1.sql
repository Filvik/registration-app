INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at)
VALUES ('John Doe', 'john@example.com', '$2b$04$Rtw2.aMcwVGS5eD8ZtEhRO58gzYv6rW5Boozg6NEpfaJ0B1fkiTUW', '+79031234567', NOW(), NOW());
INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at)
VALUES ('Peter Parker', 'peter@example.com', '$2b$04$EAMxznukxyKJPeEWuLazjuzd0O7.EXgSG/JFJNb6Vwx7SnOMK3DBi', '+79037654321', NOW(), NOW());
INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at)
VALUES ('Bruce Wayne', 'bruce@example.com', '$2b$04$DpdhIMpoi344bAwqhYwBZuFl2YgPELjVQFikUVSZa46pyI9EEVkEq', '+79031234444', NOW(), NOW());

INSERT INTO roles (role_name) VALUES ('User');
INSERT INTO roles (role_name) VALUES ('Operator');
INSERT INTO roles (role_name) VALUES ('Administrator');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3);
