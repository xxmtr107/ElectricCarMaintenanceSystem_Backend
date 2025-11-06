-- ========================================
-- 1. ROLES (Keep as is)
-- ========================================
INSERT IGNORE INTO roles (name, create_at, created_by) VALUES
                                                           ('CUSTOMER', NOW(), 'SYSTEM'),
                                                           ('TECHNICIAN', NOW(), 'SYSTEM'),
                                                           ('STAFF', NOW(), 'SYSTEM'),
                                                           ('ADMIN', NOW(), 'SYSTEM');

-- ========================================
-- 2. USERS (Keep as is)
-- ========================================
INSERT IGNORE INTO users (username, email, password, phone, full_name, gender, role_id, create_at, update_at) VALUES
                                                                                                                  -- CUSTOMER (role_id = 1)
                                                                                                                  ('baobao1', 'baobao1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234001', N'Bảo Bảo', 'MALE', 1, NOW(), NOW()),
                                                                                                                  ('triettriet1', 'triettriet1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234002', N'Triết Triết', 'FEMALE', 1, NOW(), NOW()),
                                                                                                                  ('nguyennguyen1', 'nguyennguyen1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234003', N'Nguyên Nguyên', 'MALE', 1, NOW(), NOW()),
                                                                                                                  ('thaothao1', 'thaothao1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234004', N'Thảo Thảo', 'FEMALE', 1,  NOW(), NOW()),
                                                                                                                  -- TECHNICIAN (role_id = 2)
                                                                                                                  ('baobao2', 'baobao2@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234005', N'Bảo Bảo', 'MALE', 2, NOW(), NOW()),
                                                                                                                  ('triettriet2', 'triettriet2@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234006', N'Triết Triết', 'FEMALE', 2, NOW(), NOW()),
                                                                                                                  ('nguyennguyen2', 'nguyennguyen2@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234007', N'Nguyên Nguyên', 'MALE', 2, NOW(), NOW()),
                                                                                                                  ('thaothao2', 'thaothao2@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234008', N'Thảo Thảo', 'FEMALE', 2,  NOW(), NOW()),
                                                                                                                  -- STAFF (role_id = 3)
                                                                                                                  ('baobao3', 'baobao3@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234009', N'Bảo Bảo', 'MALE', 3, NOW(), NOW()),
                                                                                                                  ('triettriet3', 'triettriet3@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234010', N'Triết Triết', 'FEMALE', 3, NOW(), NOW()),
                                                                                                                  ('nguyennguyen3', 'nguyennguyen3@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234011', N'Nguyên Nguyên', 'MALE', 3, NOW(), NOW()),
                                                                                                                  ('thaothao3', 'thaothao3@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234012', N'Thảo Thảo', 'FEMALE', 3, NOW(), NOW()),
                                                                                                                  -- ADMIN (role_id = 4)
                                                                                                                  ('baobao4', 'baobao4@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234013', N'Bảo Bảo', 'MALE', 4, NOW(), NOW()),
                                                                                                                  ('triettriet4', 'triettriet4@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234014', N'Triết Triết', 'FEMALE', 4, NOW(), NOW()),
                                                                                                                  ('nguyennguyen4', 'nguyennguyen4@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234015', N'Nguyên Nguyên', 'MALE', 4, NOW(), NOW()),
                                                                                                                  ('thaothao4', 'thaothao4@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234016', N'Thảo Thảo', 'FEMALE', 4, NOW(), NOW());

SET NAMES utf8mb4;

-- =================================================================
-- 1. SERVICE_PACKAGES (Các mốc bảo dưỡng)
-- =================================================================
INSERT IGNORE INTO service_packages (name, description) VALUES
                                                            (N'Maintenance 12000km milestone', N'Periodic maintenance package at 12,000km or 1 year'),
                                                            (N'Maintenance 24000km milestone', N'Periodic maintenance package at 24,000km or 2 years'),
                                                            (N'Maintenance 36000km milestone', N'Periodic maintenance package at 36,000km or 3 years'),
                                                            (N'Maintenance 48000km milestone', N'Periodic maintenance package at 48,000km or 4 years'),
                                                            (N'Maintenance 60000km milestone', N'Periodic maintenance package at 60,000km or 5 years'),
                                                            (N'Maintenance 72000km milestone', N'Periodic maintenance package at 72,000km or 6 years'),
                                                            (N'Maintenance 84000km milestone', N'Periodic maintenance package at 84,000km or 7 years'),
                                                            (N'Maintenance 96000km milestone', N'Periodic maintenance package at 96,000km or 8 years'),
                                                            (N'Maintenance 108000km milestone', N'Periodic maintenance package at 108,000km or 9 years'),
                                                            (N'Maintenance 120000km milestone', N'Periodic maintenance package at 120,000km or 10 years');

-- =================================================================
-- 2. SERVICE_ITEMS (Các hạng mục dịch vụ)
-- =================================================================
INSERT IGNORE INTO service_items (id, name, description) VALUES
                                                             (1, N'Cabin air filter', N'Replace or clean cabin air filter'),
                                                             (2, N'Brake fluid', N'Check or replace brake fluid'),
                                                             (3, N'Air conditioning system maintenance', N'Check, clean air conditioning system'),
                                                             (4, N'Remote key battery', N'Check or replace key battery'),
                                                             (5, N'T-Box battery', N'Check or replace T-Box battery'),
                                                             (6, N'Coolant for Battery/Engine', N'Check or replace/top up coolant'),
                                                             (7, N'Tires (pressure, wear, rims...)', N'Check tires, rims, dynamic balancing, tire rotation'),
                                                             (8, N'Brake pads and brake discs', N'Check brake pad wear, brake discs'),
                                                             (9, N'Brake lines, connections', N'Check for leaks, cracks in brake lines'),
                                                             (10, N'Drivetrain (Electric motor and gearbox)', N'Check operational status'),
                                                             (11, N'Suspension system', N'Check shock absorbers, springs, links'),
                                                             (12, N'Drive shaft', N'Check drive shaft, universal joints'),
                                                             (13, N'Ball joints', N'Check ball joints, steering knuckles'),
                                                             (14, N'Steering rack and ball joints', N'Check steering system'),
                                                             (15, N'Coolant hoses', N'Check for leaks, cracks in cooling system hoses'),
                                                             (16, N'High voltage battery (EV)', N'Check battery status, battery cooling fan'),
                                                             (17, N'High voltage system door cables', N'Check high voltage cables'),
                                                             (18, N'Charging port', N'Check, clean charging port'),
                                                             (19, N'12V battery', N'Check 12V battery status'),
                                                             (20, N'Wipers / Washer fluid', N'Check and top up washer fluid, replace wipers if needed'),
                                                             (21, N'Check for rust / corrosion undercarriage', N'Overall undercarriage inspection');

-- =================================================================
-- 3. VEHICLE_MODELS (Các mẫu xe)
-- =================================================================
INSERT IGNORE INTO vehicle_models (id, name, model_year) VALUES
                                                             (1, N'VFe34', N'2021'),
                                                             (2, N'VF 3', N'2024'),
                                                             (3, N'VF 5', N'2023'),
                                                             (4, N'VF 6', N'2023'),
                                                             (5, N'VF 7', N'2024'),
                                                             (6, N'VF 8', N'2022'),
                                                             (7, N'VF 9', N'2022');

-- =================================================================
-- 4. MODEL_PACKAGE_ITEMS (Bảng giá dịch vụ theo mốc)
-- LƯU Ý: GIÁ ĐÃ ĐƯỢC CẬP NHẬT
-- GIÁ CHECK = Tiền công kiểm tra
-- GIÁ REPLACE = Tiền công thay + Tiền vật tư (nếu có)
-- =================================================================

-- ---------------------------------
-- --- MODEL: VFe34 (model_id=1) ---
-- ---------------------------------
-- Mốc 12000km (CHECK là chủ yếu)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (1, 12000, 1, 670000, 'REPLACE'), -- (Công 150k + Lọc 520k)
                                                                                                          (1, 12000, 2, 50000, 'CHECK'),
                                                                                                          (1, 12000, 3, 50000, 'CHECK'),
                                                                                                          (1, 12000, 4, 30000, 'CHECK'),
                                                                                                          (1, 12000, 5, 30000, 'CHECK'),
                                                                                                          (1, 12000, 6, 40000, 'CHECK'),
                                                                                                          (1, 12000, 7, 50000, 'CHECK'),
                                                                                                          (1, 12000, 8, 50000, 'CHECK'),
                                                                                                          (1, 12000, 9, 40000, 'CHECK'),
                                                                                                          (1, 12000, 10, 50000, 'CHECK'),
                                                                                                          (1, 12000, 11, 50000, 'CHECK'),
                                                                                                          (1, 12000, 12, 40000, 'CHECK'),
                                                                                                          (1, 12000, 13, 40000, 'CHECK'),
                                                                                                          (1, 12000, 14, 50000, 'CHECK'),
                                                                                                          (1, 12000, 15, 40000, 'CHECK'),
                                                                                                          (1, 12000, 16, 50000, 'CHECK'),
                                                                                                          (1, 12000, 17, 50000, 'CHECK'),
                                                                                                          (1, 12000, 18, 30000, 'CHECK'),
                                                                                                          (1, 12000, 19, 40000, 'CHECK'),
                                                                                                          (1, 12000, 20, 30000, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
UPDATE model_package_items SET price = 630000 WHERE vehicle_model_id = 1 AND milestone_km = 24000 AND service_item_id = 2; -- (Công 250k + Dầu phanh 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k + Thay Pin chìa khóa)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 48000, service_item_id, price, CASE WHEN service_item_id = 4 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 145000 WHERE vehicle_model_id = 1 AND milestone_km = 48000 AND service_item_id = 4; -- (Công 100k + Pin CR2032 45k)
-- Mốc 60000km (Như 12k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 60000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1030000 WHERE vehicle_model_id = 1 AND milestone_km = 60000 AND service_item_id = 6; -- (Công 350k + Nước làm mát 680k)
-- Mốc 72000km (Như 24k + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 72000, service_item_id, price, CASE WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1400000 WHERE vehicle_model_id = 1 AND milestone_km = 72000 AND service_item_id = 3; -- (Công 300k + Van AC 1100k)
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 96000km (Như 48k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 48000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 120000km (Như 60k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 120000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 60000;
UPDATE model_package_items SET price = 1100000 WHERE vehicle_model_id = 1 AND milestone_km = 120000 AND service_item_id = 5; -- (Công 400k + Pin Tbox 700k)

-- ---------------------------------
-- --- MODEL: VF 3 (model_id=2) ---
-- ---------------------------------
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (2, 12000, 1, 610000, 'REPLACE'), -- (Công 120k + Lọc 490k)
                                                                                                          (2, 12000, 20, 20000, 'CHECK'),
                                                                                                          (2, 12000, 3, 40000, 'CHECK'),
                                                                                                          (2, 12000, 2, 40000, 'CHECK'),
                                                                                                          (2, 12000, 7, 40000, 'CHECK'),
                                                                                                          (2, 12000, 8, 40000, 'CHECK'),
                                                                                                          (2, 12000, 9, 30000, 'CHECK'),
                                                                                                          (2, 12000, 10, 40000, 'CHECK'),
                                                                                                          (2, 12000, 11, 40000, 'CHECK'),
                                                                                                          (2, 12000, 12, 30000, 'CHECK'),
                                                                                                          (2, 12000, 13, 30000, 'CHECK'),
                                                                                                          (2, 12000, 14, 40000, 'CHECK'),
                                                                                                          (2, 12000, 16, 50000, 'CHECK'),
                                                                                                          (2, 12000, 17, 40000, 'CHECK'),
                                                                                                          (2, 12000, 18, 20000, 'CHECK'),
                                                                                                          (2, 12000, 19, 30000, 'CHECK'),
                                                                                                          (2, 12000, 21, 30000, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
UPDATE model_package_items SET price = 580000 WHERE vehicle_model_id = 2 AND milestone_km = 24000 AND service_item_id = 2; -- (Công 200k + Dầu 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 120000, service_item_id, price, CASE WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1240000 WHERE vehicle_model_id = 2 AND milestone_km = 120000 AND service_item_id = 3; -- (Công 250k + Van AC 990k)


-- ---------------------------------
-- --- MODEL: VF 5 (model_id=3) ---
-- ---------------------------------
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (3, 12000, 1, 630000, 'REPLACE'), -- (Công 140k + Lọc 490k)
                                                                                                          (3, 12000, 20, 25000, 'CHECK'),
                                                                                                          (3, 12000, 3, 45000, 'CHECK'),
                                                                                                          (3, 12000, 5, 25000, 'CHECK'),
                                                                                                          (3, 12000, 6, 35000, 'CHECK'),
                                                                                                          (3, 12000, 2, 45000, 'CHECK'),
                                                                                                          (3, 12000, 7, 45000, 'CHECK'),
                                                                                                          (3, 12000, 8, 45000, 'CHECK'),
                                                                                                          (3, 12000, 9, 35000, 'CHECK'),
                                                                                                          (3, 12000, 10, 45000, 'CHECK'),
                                                                                                          (3, 12000, 11, 45000, 'CHECK'),
                                                                                                          (3, 12000, 12, 35000, 'CHECK'),
                                                                                                          (3, 12000, 13, 35000, 'CHECK'),
                                                                                                          (3, 12000, 14, 45000, 'CHECK'),
                                                                                                          (3, 12000, 15, 35000, 'CHECK'),
                                                                                                          (3, 12000, 16, 50000, 'CHECK'),
                                                                                                          (3, 12000, 17, 45000, 'CHECK'),
                                                                                                          (3, 12000, 18, 25000, 'CHECK'),
                                                                                                          (3, 12000, 19, 35000, 'CHECK'),
                                                                                                          (3, 12000, 21, 35000, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
UPDATE model_package_items SET price = 600000 WHERE vehicle_model_id = 3 AND milestone_km = 24000 AND service_item_id = 2; -- (Công 220k + Dầu 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1080000 WHERE vehicle_model_id = 3 AND milestone_km = 72000 AND service_item_id = 5; -- (Công 400k + Pin 680k)
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1030000 WHERE vehicle_model_id = 3 AND milestone_km = 120000 AND service_item_id = 6; -- (Công 380k + Nước 650k)


-- ---------------------------------
-- --- MODEL: VF 6 (model_id=4) ---
-- ---------------------------------
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (4, 12000, 1, 674000, 'REPLACE'), -- (Công 154k + Lọc 520k)
                                                                                                          (4, 12000, 20, 27500, 'CHECK'),
                                                                                                          (4, 12000, 3, 49500, 'CHECK'),
                                                                                                          (4, 12000, 5, 27500, 'CHECK'),
                                                                                                          (4, 12000, 6, 38500, 'CHECK'),
                                                                                                          (4, 12000, 2, 49500, 'CHECK'),
                                                                                                          (4, 12000, 7, 49500, 'CHECK'),
                                                                                                          (4, 12000, 8, 49500, 'CHECK'),
                                                                                                          (4, 12000, 9, 38500, 'CHECK'),
                                                                                                          (4, 12000, 10, 49500, 'CHECK'),
                                                                                                          (4, 12000, 11, 49500, 'CHECK'),
                                                                                                          (4, 12000, 12, 38500, 'CHECK'),
                                                                                                          (4, 12000, 13, 38500, 'CHECK'),
                                                                                                          (4, 12000, 14, 49500, 'CHECK'),
                                                                                                          (4, 12000, 15, 38500, 'CHECK'),
                                                                                                          (4, 12000, 16, 55000, 'CHECK'),
                                                                                                          (4, 12000, 17, 49500, 'CHECK'),
                                                                                                          (4, 12000, 18, 27500, 'CHECK'),
                                                                                                          (4, 12000, 19, 38500, 'CHECK'),
                                                                                                          (4, 12000, 21, 38500, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
UPDATE model_package_items SET price = 622000 WHERE vehicle_model_id = 4 AND milestone_km = 24000 AND service_item_id = 2; -- (Công 242k + Dầu 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1120000 WHERE vehicle_model_id = 4 AND milestone_km = 72000 AND service_item_id = 5; -- (Công 440k + Pin 680k)
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1068000 WHERE vehicle_model_id = 4 AND milestone_km = 120000 AND service_item_id = 6; -- (Công 418k + Nước 650k)

-- ---------------------------------
-- --- MODEL: VF 7 (model_id=5) ---
-- ---------------------------------
-- Mốc 12000km (Giá như VF6)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 12000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
UPDATE model_package_items SET price = 622000 WHERE vehicle_model_id = 5 AND milestone_km = 24000 AND service_item_id = 2; -- (Công 242k + Dầu 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1120000 WHERE vehicle_model_id = 5 AND milestone_km = 72000 AND service_item_id = 5; -- (Công 440k + Pin 680k)
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1068000 WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 6; -- (Công 418k + Nước 650k)
UPDATE model_package_items SET price = 1390000 WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 3; -- (Công 330k + Van 1060k)

-- ---------------------------------
-- --- MODEL: VF 8 (model_id=6) ---
-- ---------------------------------
-- Mốc 12000km (Như VFe34 12k, không có pin chìa khóa)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 12000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000 AND service_item_id != 4;
UPDATE model_package_items SET price = 680000 WHERE vehicle_model_id = 6 AND milestone_km = 12000 AND service_item_id = 1; -- (Công 150k + Lọc 530k)
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 630000 WHERE vehicle_model_id = 6 AND milestone_km = 24000 AND service_item_id = 2; -- (Công 250k + Dầu 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1110000 WHERE vehicle_model_id = 6 AND milestone_km = 72000 AND service_item_id = 5; -- (Công 400k + Pin 710k)
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1040000 WHERE vehicle_model_id = 6 AND milestone_km = 120000 AND service_item_id = 6; -- (Công 350k + Nước 690k)


-- ---------------------------------
-- --- MODEL: VF 9 (model_id=7) ---
-- ---------------------------------
-- Mốc 12000km (Giá * 1.15)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 12000, service_item_id, price * 1.15, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 782500 WHERE vehicle_model_id = 7 AND milestone_km = 12000 AND service_item_id = 1; -- (150k*1.15 + 550k)
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
UPDATE model_package_items SET price = 667500 WHERE vehicle_model_id = 7 AND milestone_km = 24000 AND service_item_id = 2; -- (250k*1.15 + 380k)
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1180000 WHERE vehicle_model_id = 7 AND milestone_km = 72000 AND service_item_id = 5; -- (400k*1.15 + 720k)
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1102500 WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 6; -- (350k*1.15 + 700k)
UPDATE model_package_items SET price = 1514500 WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 3; -- (345k + 1120k)

-- =================================================================
-- SERVICE CENTER (Keep as is)
-- =================================================================
INSERT INTO service_centers (name, address, district, city, phone)
VALUES
    ('Vin3S Showroom Hoc Mon Center', '166 Ly Thuong Kiet, Quarter 3, Hoc Mon Town', 'Hoc Mon District', 'Ho Chi Minh City', '0762718718'),
    ('Vin3S Showroom 39A Ha Huy Giap', '39A Ha Huy Giap Street', 'District 12', 'Ho Chi Minh City', '0702721721'),
    ('Vin3S Showroom 314 Nguyen Anh Thu - Hiep Thanh', '314A Nguyen Anh Thu Street, Hiep Thanh Ward', 'District 12', 'Ho Chi Minh City', '0796621621'),
    ('Vin3S Showroom Nha Be', '2250 Huynh Tan Phat Street, Phu Xuan Commune', 'Nha Be District', 'Ho Chi Minh City', '0946143939'),
    ('Vin3S Showroom Cu Chi Center', '158 TL8, Cu Chi Town', 'Cu Chi District', 'Ho Chi Minh City', '0904404692'),
    ('Vin3S Showroom 217A Ben Van Don', '217A Ben Van Don Street, Ward 2', 'District 4', 'Ho Chi Minh City', '0938362217'),
    ('Vin3S Showroom 57 Pham Ngoc Thach', '57 Pham Ngoc Thach Street, Ward 6', 'District 3', 'Ho Chi Minh City', '0938399843'),
    ('Vin3S Showroom Le Quang Dinh', '486 Le Quang Dinh Street, Ward 11', 'Binh Thanh District', 'Ho Chi Minh City', '0796553553'),
    ('Vin3S Showroom Nguyen Thi Tu', '212 Nguyen Thi Tu Street, Quarter 2, Binh Hung Hoa B Ward', 'Binh Tan District', 'Ho Chi Minh City', '0973565179'),
    ('Vin3S Showroom 677 Au Co', '677 Au Co Street, Tan Thanh Ward', 'Tan Phu District', 'Ho Chi Minh City', '0971971010'),
    ('Vin3S Showroom 594 Le Van Quoi', '594 Le Van Quoi Street, Binh Hung Hoa A Ward', 'Binh Tan District', 'Ho Chi Minh City', '0867036536'),
    ('Vin3S Showroom 307 Lac Long Quan', '305-307-309 Lac Long Quan Street, Ward 3', 'District 11', 'Ho Chi Minh City', '0979905353'),
    ('Vin3S Showroom 255 Hiep Binh Phuoc', '255 National Highway 13, Hiep Binh Phuoc Ward', 'Thu Duc City', 'Ho Chi Minh City', '0964100808'),
    ('Vin3S Showroom 460 Nguyen Van Luong', '460 Nguyen Van Luong Street, Ward 12', 'District 6', 'Ho Chi Minh City', '0971801010'),
    ('Vin3S Showroom 337 Do Xuan Hop', '337 Do Xuan Hop Street, Phuoc Long B Ward', 'Thu Duc City', 'Ho Chi Minh City', '0768420420'),
    ('Vin3S Showroom Binh Chanh', 'C8/1 Pham Hung Street, Binh Hung Commune', 'Binh Chanh District', 'Ho Chi Minh City', '0963764839'),
    ('Authorized 3S Distributor VinFast Thuan Nhan', '447-447A-447B Cong Hoa Street, Ward 15', 'Tan Binh District', 'Ho Chi Minh City', '0965201018'),
    ('Authorized 3S Distributor VinFast Skytt', '214 Nguyen Oanh Street, Ward 17', 'Go Vap District', 'Ho Chi Minh City', '02873032689'),
    ('VinFast Le Van Viet', '1st Floor, Vincom Plaza Le Van Viet, 50 Le Van Viet Street, Hiep Phu Ward', 'District 9', 'Ho Chi Minh City', '0981335517'),
    ('VinFast Thao Dien', 'L1 Floor, Vincom Mega Mall Thao Dien, 159 Xa Lo Ha Noi, Thao Dien Ward', 'District 2', 'Ho Chi Minh City', '0981335514');

-- =================================================================
-- 5. PART-CATEGORIES (Dùng cho quản lý kho)
-- =================================================================
INSERT IGNORE INTO part_categories
(id, name, code, description, create_at, created_by, update_at, updated_by)
VALUES
    (1, N'Filters', 'FILTER', N'Air filters, cabin filters', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (2, N'Liquids & Chemicals', 'LIQUID', N'Brake fluid, coolant...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (3, N'Batteries & Electronics', 'ELECTRONIC', N'Key battery, T-Box battery, Sensors...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (4, N'Wipers & Washers', 'WIPER', N'Wiper blades, washer pump...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (5, N'Battery', 'BATTERY', N'12V battery', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (6, N'Brake System', 'BRAKE', N'Brake pads, brake discs...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (7, N'Suspension & Steering', 'SUSPENSION', N'Shock absorbers, tie rods, ball joints...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (8, N'Drivetrain', 'DRIVETRAIN', N'Axle shafts, joints...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (9, N'Cooling & A/C System', 'COOLING', N'Hoses, valves, sensors...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (10, N'High Voltage System', 'HIGH_VOLTAGE', N'Charging cables, fuses...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (11, N'Tires & Wheels', 'WHEEL', N'Tires, TPMS valves...', NOW(), 'SYSTEM', NOW(), 'SYSTEM');


-- =================================================================
-- 6. SPARE-PARTS (Dùng cho quản lý kho)
-- (Giữ nguyên giá vật tư)
-- =================================================================
INSERT IGNORE INTO spare_parts
(part_number, name, unit_price, quantity_in_stock, minimum_stock_level, category_id, create_at, created_by, update_at, updated_by)
VALUES
-- === SPARE PARTS FOR VF 3 ===
('FLT-VF3', N'Cabin air filter VF 3', 490000.00, 50, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF3', N'Brake fluid DOT 4 (VF 3)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-KEY-GEN', N'Key battery CR2032 (Common)', 45000.00, 300, 50, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Common
('BAT-TBOX-VF3', N'T-Box battery VF 3', 650000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF3', N'12V battery AGM 60Ah (VF 3)', 4300000.00, 15, 5, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF3', N'Front brake pads VF 3', 990000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF3', N'Rear brake pads VF 3', 920000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF3', N'Front brake disc VF 3', 1580000.00, 10, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF3', N'Front shock absorber VF 3', 1950000.00, 10, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF3', N'Lower ball joint VF 3', 780000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF3', N'Outer tie rod end VF 3', 720000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF3', N'CV joint VF 3', 2450000.00, 5, 2, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF3', N'Main coolant hose VF 3', 520000.00, 10, 3, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF3', N'A/C expansion valve VF 3', 990000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF3', N'Charging cable VF 3', 6200000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF3', N'Main HV fuse VF 3', 1450000.00, 5, 2, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF3', N'Tire VF 3 (175/75R16)', 1850000.00, 30, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF3', N'TPMS valve sensor VF 3', 850000.00, 40, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF3', N'Washer fluid pump VF 3', 520000.00, 10, 3, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF3', N'Wiper blade set VF 3', 480000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === SPARE PARTS FOR VF 5 ===
('FLT-VF5', N'Cabin air filter VF 5', 490000.00, 50, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF5', N'Brake fluid DOT 4 (VF 5)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF5', N'EV Coolant Type 1 (VF 5)', 650000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF5', N'T-Box battery VF 5', 680000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF5', N'12V battery AGM 60Ah (VF 5)', 4300000.00, 15, 5, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF5', N'Front brake pads VF 5', 1020000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF5', N'Rear brake pads VF 5', 960000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF5', N'Front brake disc VF 5', 1620000.00, 10, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF5', N'Front shock absorber VF 5', 2020000.00, 10, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF5', N'Lower ball joint VF 5', 820000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF5', N'Outer tie rod end VF 5', 760000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF5', N'CV joint VF 5', 2580000.00, 5, 2, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF5', N'Main coolant hose VF 5', 550000.00, 10, 3, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF5', N'A/C expansion valve VF 5', 1020000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF5', N'Charging cable VF 5', 6400000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF5', N'Main HV fuse VF 5', 1520000.00, 5, 2, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF5', N'Tire VF 5', 2050000.00, 30, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF5', N'TPMS valve sensor VF 5', 860000.00, 40, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF5', N'Washer fluid pump VF 5', 540000.00, 10, 3, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === SPARE PARTS FOR VF 6 ===
('FLT-VF6', N'Cabin air filter VF 6', 520000.00, 40, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF6', N'Brake fluid DOT 4 (VF 6)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF6', N'EV Coolant Type 1 (VF 6)', 650000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF6', N'T-Box battery VF 6', 680000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF6', N'Wiper blade set VF 6', 720000.00, 30, 10, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF6', N'12V battery AGM 70Ah (VF 6)', 4700000.00, 10, 3, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF6', N'Front brake pads VF 6', 1150000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF6', N'Rear brake pads VF 6', 1080000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF6', N'Front brake disc VF 6', 1850000.00, 5, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF6', N'Front shock absorber VF 6', 2200000.00, 5, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF6', N'Lower ball joint VF 6', 850000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF6', N'Outer tie rod end VF 6', 790000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF6', N'CV joint VF 6', 2700000.00, 3, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF6', N'Main coolant hose VF 6', 570000.00, 8, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF6', N'A/C expansion valve VF 6', 1050000.00, 3, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF6', N'Charging cable VF 6', 6500000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF6', N'Main HV fuse VF 6', 1550000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF6', N'Tire VF 6', 2450000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF6', N'TPMS valve sensor VF 6', 860000.00, 30, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF6', N'Washer fluid pump VF 6', 540000.00, 8, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === SPARE PARTS FOR VF 7 ===
('FLT-VF7', N'Cabin air filter VF 7', 520000.00, 40, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF7', N'Brake fluid DOT 4 (VF 7)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF7', N'EV Coolant Type 1 (VF 7)', 650000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF7', N'T-Box battery VF 7', 680000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF7', N'Wiper blade set VF 7', 730000.00, 30, 10, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF7', N'12V battery AGM 70Ah (VF 7)', 4700000.00, 10, 3, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF7', N'Front brake pads VF 7', 1160000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF7', N'Rear brake pads VF 7', 1090000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF7', N'Front brake disc VF 7', 1860000.00, 5, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF7', N'Front shock absorber VF 7', 2250000.00, 5, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF7', N'Lower ball joint VF 7', 860000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF7', N'Outer tie rod end VF 7', 800000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF7', N'CV joint VF 7', 2750000.00, 3, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF7', N'Main coolant hose VF 7', 580000.00, 8, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF7', N'A/C expansion valve VF 7', 1060000.00, 3, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF7', N'Charging cable VF 7', 6600000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF7', N'Main HV fuse VF 7', 1560000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF7', N'Tire VF 7', 2750000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF7', N'TPMS valve sensor VF 7', 860000.00, 30, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF7', N'Washer fluid pump VF 7', 540000.00, 8, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === SPARE PARTS FOR VFe34 ===
('FLT-VFE34', N'Cabin air filter VFe34', 520000.00, 30, 5, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VFE34', N'Brake fluid DOT 4 (VFe34)', 380000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VFE34', N'EV Coolant Type 2 (VFe34)', 680000.00, 15, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VFE34', N'T-Box battery VFe34', 700000.00, 10, 2, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VFE34', N'12V battery AGM 70Ah (VFe34)', 4700000.00, 8, 2, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VFE34', N'Front brake pads VFe34', 1380000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VFE34', N'Rear brake pads VFe34', 1280000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VFE34', N'Front brake disc VFe34', 2150000.00, 5, 1, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VFE34', N'Front shock absorber VFe34', 2500000.00, 3, 1, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VFE34', N'Lower ball joint VFe34', 900000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VFE34', N'Outer tie rod end VFe34', 850000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VFE34', N'CV joint VFe34', 3000000.00, 2, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VFE34', N'Main coolant hose VFe34', 600000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VFE34', N'A/C expansion valve VFe34', 1100000.00, 2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VFE34', N'Charging cable VFe34', 6800000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VFE34', N'Main HV fuse VFe34', 1600000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VFE34', N'Tire VFe34', 2950000.00, 15, 3, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VFE34', N'TPMS valve sensor VFe34', 860000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VFE34', N'Washer fluid pump VFe34', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === SPARE PARTS FOR VF 8 ===
('FLT-VF8', N'Cabin air filter VF 8', 530000.00, 30, 5, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF8', N'Brake fluid DOT 4 (VF 8)', 380000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF8', N'EV Coolant Type 2 (VF 8)', 690000.00, 15, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF8', N'T-Box battery VF 8', 710000.00, 10, 2, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF8', N'12V battery AGM 70Ah (VF 8)', 4700000.00, 8, 2, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF8', N'Front brake pads VF 8', 1390000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF8', N'Rear brake pads VF 8', 1290000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF8', N'Front brake disc VF 8', 2160000.00, 5, 1, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF8', N'Front shock absorber VF 8', 2550000.00, 3, 1, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF8', N'Lower ball joint VF 8', 910000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF8', N'Outer tie rod end VF 8', 860000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF8', N'CV joint VF 8', 3050000.00, 2, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF8', N'Main coolant hose VF 8', 610000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF8', N'A/C expansion valve VF 8', 1110000.00, 2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF8', N'Charging cable VF 8', 6900000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF8', N'Main HV fuse VF 8', 1610000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF8', N'Tire VF 8', 3600000.00, 15, 3, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF8', N'TPMS valve sensor VF 8', 860000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF8', N'Washer fluid pump VF 8', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === SPARE PARTS FOR VF 9 ===
('FLT-VF9', N'Cabin air filter VF 9', 550000.00, 30, 5, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF9', N'Brake fluid DOT 4 (VF 9)', 380000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF9', N'EV Coolant Type 2 (VF 9)', 700000.00, 15, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF9', N'T-Box battery VF 9', 720000.00, 10, 2, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF9', N'12V battery AGM 70Ah (VF 9)', 4700000.00, 8, 2, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF9', N'Front brake pads VF 9', 1400000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF9', N'Rear brake pads VF 9', 1300000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF9', N'Front brake disc VF 9', 2170000.00, 5, 1, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF9', N'Front shock absorber VF 9', 2600000.00, 3, 1, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF9', N'Lower ball joint VF 9', 920000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF9', N'Outer tie rod end VF 9', 870000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF9', N'CV joint VF 9', 3100000.00, 2, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF9', N'Main coolant hose VF 9', 620000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF9', N'A/C expansion valve VF 9', 1120000.00, 2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF9', N'Charging cable VF 9', 7000000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF9', N'Main HV fuse VF 9', 1620000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF9', N'Tire VF 9', 4200000.00, 15, 3, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF9', N'TPMS valve sensor VF 9', 860000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF9', N'Washer fluid pump VF 9', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM');

-- SET FOREIGN_KEY_CHECKS = 0;
-- SET FOREIGN_KEY_CHECKS = 1;