-- Backfill membership rows for user IDs that exist in EMS but have no membership row yet.
-- Use when EMS still owns user/student data and you need logical membership rows in membership_db.
-- Replace studentmanagementsystem with your EMS schema name if different.

INSERT INTO membership_db.membership (user_id, points)
SELECT u.id, 0
FROM studentmanagementsystem.users u
INNER JOIN studentmanagementsystem.students s ON s.user_id = u.id
WHERE u.role = 'STUDENT'
  AND NOT EXISTS (
    SELECT 1 FROM membership_db.membership m WHERE m.user_id = u.id
  );
