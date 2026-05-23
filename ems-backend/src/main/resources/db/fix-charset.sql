-- Run this once in MySQL if Chinese characters still fail to save,
-- or after adding the adopted column for existing pets.

ALTER DATABASE studentmanagementsystem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

ALTER TABLE pets CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE students CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE departments CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Existing rows: default not adopted
UPDATE pets SET adopted = 0 WHERE adopted IS NULL;
