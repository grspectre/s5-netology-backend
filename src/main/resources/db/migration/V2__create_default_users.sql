-- Создание администратора (пароль будет захеширован при первом входе или через DataInitializer)
INSERT INTO person (id, email, name, password, enabled, role, created, modified)
SELECT gen_random_uuid()::varchar, 'admin@example.com', 'Administrator', 'adminPass', true, 'ADMIN', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM person WHERE email = 'admin@example.com');

-- Создание обычного пользователя
INSERT INTO person (id, email, name, password, enabled, role, created, modified)
SELECT gen_random_uuid()::varchar, 'user@example.com', 'User', 'userPass', true, 'USER', NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM person WHERE email = 'user@example.com');
