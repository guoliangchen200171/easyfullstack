-- Run against membership_db after first startup (or rely on MembershipLevelDataLoader).
INSERT INTO membership_level (level_code, level_name, min_points, description, sort_order)
SELECT 'BRONZE', '铜牌会员', 0, '默认等级', 1
WHERE NOT EXISTS (SELECT 1 FROM membership_level WHERE level_code = 'BRONZE');

INSERT INTO membership_level (level_code, level_name, min_points, description, sort_order)
SELECT 'SILVER', '银牌会员', 3000, NULL, 2
WHERE NOT EXISTS (SELECT 1 FROM membership_level WHERE level_code = 'SILVER');

INSERT INTO membership_level (level_code, level_name, min_points, description, sort_order)
SELECT 'GOLD', '金牌会员', 10000, NULL, 3
WHERE NOT EXISTS (SELECT 1 FROM membership_level WHERE level_code = 'GOLD');
