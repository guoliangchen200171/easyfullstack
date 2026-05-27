-- Migrate membership rows from EMS monolith DB to membership_db.
-- Prerequisites:
--   1. membership-service has started once (ddl-auto=update created membership table in membership_db)
--   2. Both databases exist on the same MySQL instance
--
-- Run in MySQL client (adjust schema names if yours differ):

INSERT INTO membership_db.membership (user_id, points)
SELECT m.user_id, m.points
FROM studentmanagementsystem.membership m
WHERE NOT EXISTS (
    SELECT 1 FROM membership_db.membership ms WHERE ms.user_id = m.user_id
);

-- Optional: drop legacy table from EMS after verifying migration
-- DROP TABLE studentmanagementsystem.membership;
