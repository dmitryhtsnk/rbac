-- Створення ролей, якщо не існують
INSERT INTO roles (name)
    SELECT 'ROLE_USER'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name)
    SELECT 'ROLE_ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

-- Додати користувача admin, якщо не існує
INSERT INTO users (username, password)
    SELECT 'admin', '$2a$10$aie89i8SoaHZfTlIkPuzTe8lP9TJJXk5ptXB1Bwr7ZYhKwnVEoe2.'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Додати користувача Bobr, якщо не існує
INSERT INTO users (username, password)
    SELECT 'Bobr', '$2a$10$W429s5lBebQFkpyf2.f1muQ//v9XpZEvHOQ4Oy9hTN1NyJDpoBbF6'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'Bobr');

-- Додати роль ROLE_ADMIN користувачу admin
INSERT INTO user_roles (user_id, role_id)
    SELECT u.id, r.id
    FROM users u, roles r
    WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
    AND NOT EXISTS (
        SELECT 1 FROM user_roles
        WHERE user_id = u.id AND role_id = r.id
    );

-- Додати роль ROLE_USER користувачу Bobr
INSERT INTO user_roles (user_id, role_id)
    SELECT u.id, r.id
    FROM users u, roles r
    WHERE u.username = 'Bobr' AND r.name = 'ROLE_USER'
    AND NOT EXISTS (
        SELECT 1 FROM user_roles
        WHERE user_id = u.id AND role_id = r.id
    );
