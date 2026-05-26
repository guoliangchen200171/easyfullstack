-- 商品垂直分表：从 products 迁移到 product_inventory + product_detail
-- 执行前请先启动应用一次（ddl-auto=update）以创建新表，或手动建表后再迁移。
-- 在维护窗口执行；执行前请备份数据库。

-- 1) 若新表为空且旧表 products 仍存在，迁移数据
INSERT INTO product_inventory (id, stock)
SELECT id, stock FROM products
WHERE NOT EXISTS (SELECT 1 FROM product_inventory pi WHERE pi.id = products.id);

INSERT INTO product_detail (product_id, name, description, price)
SELECT id, name, description, price FROM products
WHERE NOT EXISTS (SELECT 1 FROM product_detail pd WHERE pd.product_id = products.id);

-- 2) 将 product_orders 外键从 products 改指向 product_inventory（若 MySQL 仍引用 products）
-- 根据实际约束名调整；可用 SHOW CREATE TABLE product_orders; 查看
-- ALTER TABLE product_orders DROP FOREIGN KEY <fk_name>;
-- ALTER TABLE product_orders
--   ADD CONSTRAINT fk_product_orders_inventory
--   FOREIGN KEY (product_id) REFERENCES product_inventory(id);

-- 3) 验证行数一致后，备份并删除旧表
-- RENAME TABLE products TO products_backup;
-- DROP TABLE products_backup;
