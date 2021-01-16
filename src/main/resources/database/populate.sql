INSERT INTO users (id, username, email, first_name, last_name, password, enabled)
VALUES  (1, 'admin', 'admin@gmail.com', 'Sergey', 'Smolkin', '$2y$04$fKcdpodIKAYRePZlB2V6k.fbQUKNrnCGcewbYqhR9Mz9iVIoVkd8y', true),
        (2, 'user1', 'user@yandex.ru', 'Semen', 'Giba', '$2y$04$gK5f5NqZ38Jf9vkOMEFQfe3ctnBt2BMPZays2JsggJ1JTsTUdr.6W', true)
ON CONFLICT DO NOTHING;

INSERT INTO users_roles (user_id, role)
VALUES  (1, 'USER'),
        (1, 'ADMIN'),
        (2, 'USER')
ON CONFLICT DO NOTHING;