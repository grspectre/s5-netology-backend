-- admin@example.com / adminPass
INSERT INTO person (id, email, name, password, enabled, role, created, modified)
SELECT
    gen_random_uuid()::varchar,
    'admin@example.com',
    'Administrator',
    '$2a$12$trUJHd5Q8nn1zvPai/V7kufyZ2RSgt9Q3ml67o.TCWb7rP.c9cnxy',
    true,
    'ADMIN',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM person WHERE email = 'admin@example.com'
);

-- user@example.com / userPass
INSERT INTO person (id, email, name, password, enabled, role, created, modified)
SELECT
    gen_random_uuid()::varchar,
    'user@example.com',
    'User',
    '$2a$12$GXY73t8bfmy0ADkZXdKtq.hKqWZfMs7jwQn7uuXLLkNrvNQpyyDwa',
    true,
    'USER',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM person WHERE email = 'user@example.com'
);