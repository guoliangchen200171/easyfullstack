-- User FK refactor: move link from users.student_id/department_id to students.user_id / departments.user_id
-- Run once against studentmanagementsystem (backup first).

-- 1. Backfill students.user_id from legacy users.student_id (if columns still exist)
UPDATE students s
JOIN users u ON u.student_id = s.id AND u.role = 'STUDENT'
SET s.user_id = u.id
WHERE s.user_id IS NULL;

-- 2. Backfill departments.user_id from legacy users.department_id
UPDATE departments d
JOIN users u ON u.department_id = d.id AND u.role = 'DEPARTMENT'
SET d.user_id = u.id
WHERE d.user_id IS NULL;

-- 3. Verify backfill (optional)
-- SELECT id, username, role FROM users;
-- SELECT id, email, user_id FROM students;
-- SELECT id, department_name, user_id FROM departments;

-- 4. Drop legacy columns from users (run after backfill succeeds)
ALTER TABLE users DROP COLUMN student_id;
ALTER TABLE users DROP COLUMN department_id;

-- 5. Verify final schema (optional)
-- DESCRIBE users;
