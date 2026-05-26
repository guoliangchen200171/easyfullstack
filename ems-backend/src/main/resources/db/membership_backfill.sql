-- One-time backfill: create membership rows (points = 0) for existing students.
-- Run after the application has created the membership table (ddl-auto=update or manual DDL).

INSERT INTO membership (user_id, points)
SELECT u.id, 0
FROM users u
INNER JOIN students s ON s.user_id = u.id
WHERE u.role = 'STUDENT'
  AND NOT EXISTS (SELECT 1 FROM membership m WHERE m.user_id = u.id);
