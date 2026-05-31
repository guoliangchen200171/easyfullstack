-- Remove legacy users.email column; login identity is users.username (JPA User entity has no email field).
ALTER TABLE users DROP COLUMN email;
