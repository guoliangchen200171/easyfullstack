-- Clear invalid departments.user_id values that block new department registration.
-- 1) Orphan user_id: no matching users row
UPDATE departments d
LEFT JOIN users u ON u.id = d.user_id
SET d.user_id = NULL
WHERE d.user_id IS NOT NULL AND u.id IS NULL;

-- 2) user_id points to a non-DEPARTMENT account (e.g. STUDENT reused the same id)
UPDATE departments d
JOIN users u ON u.id = d.user_id
SET d.user_id = NULL
WHERE u.role <> 'DEPARTMENT';
