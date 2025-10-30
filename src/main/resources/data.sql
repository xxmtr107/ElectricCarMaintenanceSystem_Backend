-- ========================================
-- 1. ROLES (Giữ nguyên)
-- ========================================
INSERT IGNORE INTO roles (name, create_at, created_by) VALUES
                                                           ('CUSTOMER', NOW(), 'SYSTEM'),
                                                           ('TECHNICIAN', NOW(), 'SYSTEM'),
                                                           ('STAFF', NOW(), 'SYSTEM'),
                                                           ('ADMIN', NOW(), 'SYSTEM');

-- ========================================
-- 2. USERS (Giữ nguyên)
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
-- BƯỚC 1 MỚI: ĐỊNH NGHĨA CÁC "GÓI" TƯƠNG ỨNG VỚI CỘT MỐC (SERVICE_PACKAGES)
-- =================================================================
-- Đảm bảo chỉ insert nếu chưa tồn tại
INSERT IGNORE INTO service_packages (name, description) VALUES
                                                            (N'Bảo dưỡng mốc 12000km', N'Gói bảo dưỡng định kỳ tại 12.000km hoặc 1 năm'),
                                                            (N'Bảo dưỡng mốc 24000km', N'Gói bảo dưỡng định kỳ tại 24.000km hoặc 2 năm'),
                                                            (N'Bảo dưỡng mốc 36000km', N'Gói bảo dưỡng định kỳ tại 36.000km hoặc 3 năm'),
                                                            (N'Bảo dưỡng mốc 48000km', N'Gói bảo dưỡng định kỳ tại 48.000km hoặc 4 năm'),
                                                            (N'Bảo dưỡng mốc 60000km', N'Gói bảo dưỡng định kỳ tại 60.000km hoặc 5 năm'),
                                                            (N'Bảo dưỡng mốc 72000km', N'Gói bảo dưỡng định kỳ tại 72.000km hoặc 6 năm'),
                                                            (N'Bảo dưỡng mốc 84000km', N'Gói bảo dưỡng định kỳ tại 84.000km hoặc 7 năm'),
                                                            (N'Bảo dưỡng mốc 96000km', N'Gói bảo dưỡng định kỳ tại 96.000km hoặc 8 năm'),
                                                            (N'Bảo dưỡng mốc 108000km', N'Gói bảo dưỡng định kỳ tại 108.000km hoặc 9 năm'),
                                                            (N'Bảo dưỡng mốc 120000km', N'Gói bảo dưỡng định kỳ tại 120.000km hoặc 10 năm');

-- =================================================================
-- BƯỚC 2 MỚI: ĐỊNH NGHĨA CÁC "HẠNG MỤC" DỊCH VỤ LẺ (SERVICE_ITEMS)
-- =================================================================
-- Đảm bảo chỉ insert nếu chưa tồn tại và giữ ID ổn định
INSERT IGNORE INTO service_items (id, name, description) VALUES
                                                             (1, N'Lọc gió điều hòa', N'Thay thế hoặc vệ sinh lọc gió cabin'),
                                                             (2, N'Dầu phanh', N'Kiểm tra hoặc thay thế dầu phanh'),
                                                             (3, N'Bảo dưỡng hệ thống điều hòa', N'Kiểm tra, vệ sinh hệ thống điều hòa'),
                                                             (4, N'Pin chìa khóa điều khiển', N'Kiểm tra hoặc thay pin chìa khóa'),
                                                             (5, N'Pin bộ T-Box', N'Kiểm tra hoặc thay pin T-Box'),
                                                             (6, N'Nước làm mát cho Pin/Động cơ', N'Kiểm tra hoặc thay/bổ sung nước làm mát'),
                                                             (7, N'Lốp (áp suất, độ mòn, vành...)', N'Kiểm tra lốp, vành, cân bằng động, đảo lốp'),
                                                             (8, N'Má phanh và đĩa phanh', N'Kiểm tra độ mòn má phanh, đĩa phanh'),
                                                             (9, N'Đường ống, đầu nối hệ thống phanh', N'Kiểm tra rò rỉ, nứt vỡ đường ống phanh'),
                                                             (10, N'Bộ dẫn động (Động cơ điện và hộp số)', N'Kiểm tra tình trạng hoạt động'),
                                                             (11, N'Hệ thống treo', N'Kiểm tra giảm xóc, lò xo, các liên kết'),
                                                             (12, N'Trục truyền động', N'Kiểm tra trục truyền động, khớp chữ thập'),
                                                             (13, N'Khớp cầu', N'Kiểm tra các khớp cầu, rotuyn'),
                                                             (14, N'Thước lái và khớp nối cầu', N'Kiểm tra hệ thống lái'),
                                                             (15, N'Đường ống làm mát', N'Kiểm tra rò rỉ, nứt vỡ đường ống hệ thống làm mát'),
                                                             (16, N'Pin cao áp (EV)', N'Kiểm tra tình trạng pin, quạt làm mát pin'),
                                                             (17, N'Dây cáp cửa hệ thống điện áp cao', N'Kiểm tra dây cáp cao áp'),
                                                             (18, N'Cổng sạc', N'Kiểm tra, vệ sinh cổng sạc'),
                                                             (19, N'Ắc quy 12V', N'Kiểm tra tình trạng ắc quy 12V'),
                                                             (20, N'Gạt mưa / Nước rửa kính', N'Kiểm tra và bổ sung nước rửa kính, thay gạt mưa nếu cần'),
                                                             (21, N'Kiểm tra gỉ sét / ăn mòn gầm xe', N'Kiểm tra tổng thể gầm xe');

-- =================================================================
-- BƯỚC 3 MỚI: ĐỊNH NGHĨA CÁC MODEL XE (VEHICLE_MODELS) - Không còn chu kỳ/gói
-- =================================================================
-- Đảm bảo chỉ insert nếu chưa tồn tại
INSERT IGNORE INTO vehicle_models (id, name, model_year) VALUES
                                                             (1, N'VFe34', N'2021'),
                                                             (2, N'VF 3', N'2024'),
                                                             (3, N'VF 5', N'2023'),
                                                             (4, N'VF 6', N'2023'),
                                                             (5, N'VF 7', N'2024'),
                                                             (6, N'VF 8', N'2022'),
                                                             (7, N'VF 9', N'2022');

-- =================================================================
-- BƯỚC 4 MỚI: ĐỊNH NGHĨA "MENU" DỊCH VỤ THEO CỘT MỐC (MODEL_PACKAGE_ITEMS)
-- =================================================================
-- Xóa toàn bộ dữ liệu cũ trong bảng này trước khi thêm mới
# DELETE FROM model_package_items;

-- -----------------------------------------------------------------
-- Helper Function/Procedure (Tùy chọn, nếu DB hỗ trợ) để thêm item
-- DELIMITER //
-- CREATE PROCEDURE AddMaintenanceItem (
--     IN modelId INT,
--     IN milestoneKm INT,
--     IN itemName NVARCHAR(100),
--     IN itemPrice DECIMAL(10, 2),
--     IN action ENUM('CHECK', 'REPLACE')
-- )
-- BEGIN
--     DECLARE itemId INT;
--     SELECT id INTO itemId FROM service_items WHERE name = itemName LIMIT 1;
--     IF itemId IS NOT NULL THEN
--         INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type)
--         VALUES (modelId, milestoneKm, itemId, itemPrice, action);
--     ELSE
--         -- Log lỗi hoặc bỏ qua nếu item không tồn tại
--         -- SELECT CONCAT('Service item not found: ', itemName);
--     END IF;
-- END //
-- DELIMITER ;
-- -----------------------------------------------------------------

-- ---------------------------------
-- --- MODEL: VFe34 (model_id=1) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d60587.jpg)
-- Giá tham khảo từ data.sql cũ, CẦN KIỂM TRA LẠI
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (1, 12000, 1, 150000, 'REPLACE'), (1, 12000, 2, 50000, 'CHECK'), (1, 12000, 3, 50000, 'CHECK'), (1, 12000, 4, 30000, 'CHECK'), (1, 12000, 5, 30000, 'CHECK'),
                                                                                                          (1, 12000, 6, 40000, 'CHECK'), (1, 12000, 7, 50000, 'CHECK'), (1, 12000, 8, 50000, 'CHECK'), (1, 12000, 9, 40000, 'CHECK'), (1, 12000, 10, 50000, 'CHECK'),
                                                                                                          (1, 12000, 11, 50000, 'CHECK'), (1, 12000, 12, 40000, 'CHECK'), (1, 12000, 13, 40000, 'CHECK'), (1, 12000, 14, 50000, 'CHECK'), (1, 12000, 15, 40000, 'CHECK'),
                                                                                                          (1, 12000, 16, 50000, 'CHECK'), (1, 12000, 17, 50000, 'CHECK'), (1, 12000, 18, 30000, 'CHECK'), (1, 12000, 19, 40000, 'CHECK'), (1, 12000, 20, 30000, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
UPDATE model_package_items SET price = 250000 WHERE vehicle_model_id = 1 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k + Thay Pin chìa khóa)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 48000, service_item_id, price, CASE WHEN service_item_id = 4 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 100000 WHERE vehicle_model_id = 1 AND milestone_km = 48000 AND service_item_id = 4;
-- Mốc 60000km (Như 12k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 60000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
UPDATE model_package_items SET price = 350000 WHERE vehicle_model_id = 1 AND milestone_km = 60000 AND service_item_id = 6;
-- Mốc 72000km (Như 24k + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 72000, service_item_id, price, CASE WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 300000 WHERE vehicle_model_id = 1 AND milestone_km = 72000 AND service_item_id = 3;
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 96000km (Như 48k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 48000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 120000km (Như 60k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 120000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 60000;
UPDATE model_package_items SET price = 400000 WHERE vehicle_model_id = 1 AND milestone_km = 120000 AND service_item_id = 5;

-- ---------------------------------
-- --- MODEL: VF 3 (model_id=2) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d60527.jpg)
-- Giá tham khảo từ data.sql cũ, CẦN KIỂM TRA LẠI
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (2, 12000, 1, 120000, 'REPLACE'), (2, 12000, 20, 20000, 'CHECK'), (2, 12000, 3, 40000, 'CHECK'), (2, 12000, 2, 40000, 'CHECK'), (2, 12000, 7, 40000, 'CHECK'),
                                                                                                          (2, 12000, 8, 40000, 'CHECK'), (2, 12000, 9, 30000, 'CHECK'), (2, 12000, 10, 40000, 'CHECK'), (2, 12000, 11, 40000, 'CHECK'), (2, 12000, 12, 30000, 'CHECK'),
                                                                                                          (2, 12000, 13, 30000, 'CHECK'), (2, 12000, 14, 40000, 'CHECK'), (2, 12000, 16, 50000, 'CHECK'), (2, 12000, 17, 40000, 'CHECK'), (2, 12000, 18, 20000, 'CHECK'),
                                                                                                          (2, 12000, 19, 30000, 'CHECK'), (2, 12000, 21, 30000, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
UPDATE model_package_items SET price = 200000 WHERE vehicle_model_id = 2 AND milestone_km = 24000 AND service_item_id = 2;
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
UPDATE model_package_items SET price = 250000 WHERE vehicle_model_id = 2 AND milestone_km = 120000 AND service_item_id = 3;


-- ---------------------------------
-- --- MODEL: VF 5 (model_id=3) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d605a5.png / image_c5a4c5.jpg)
-- Giá tham khảo từ data.sql cũ, CẦN KIỂM TRA LẠI
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (3, 12000, 1, 140000, 'REPLACE'), (3, 12000, 20, 25000, 'CHECK'), (3, 12000, 3, 45000, 'CHECK'), (3, 12000, 5, 25000, 'CHECK'), (3, 12000, 6, 35000, 'CHECK'),
                                                                                                          (3, 12000, 2, 45000, 'CHECK'), (3, 12000, 7, 45000, 'CHECK'), (3, 12000, 8, 45000, 'CHECK'), (3, 12000, 9, 35000, 'CHECK'), (3, 12000, 10, 45000, 'CHECK'),
                                                                                                          (3, 12000, 11, 45000, 'CHECK'), (3, 12000, 12, 35000, 'CHECK'), (3, 12000, 13, 35000, 'CHECK'), (3, 12000, 14, 45000, 'CHECK'), (3, 12000, 15, 35000, 'CHECK'),
                                                                                                          (3, 12000, 16, 50000, 'CHECK'), (3, 12000, 17, 45000, 'CHECK'), (3, 12000, 18, 25000, 'CHECK'), (3, 12000, 19, 35000, 'CHECK'), (3, 12000, 21, 35000, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
UPDATE model_package_items SET price = 220000 WHERE vehicle_model_id = 3 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k) - Ảnh VF5 không ghi thay nước làm mát ở 48k? Chỉ kiểm tra.
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 400000 WHERE vehicle_model_id = 3 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 380000 WHERE vehicle_model_id = 3 AND milestone_km = 120000 AND service_item_id = 6;


-- ---------------------------------
-- --- MODEL: VF 6 (model_id=4) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d605c0.png) - Giá tăng 10% so với VF5 (Ước tính)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (4, 12000, 1, 154000, 'REPLACE'), (4, 12000, 20, 27500, 'CHECK'), (4, 12000, 3, 49500, 'CHECK'), (4, 12000, 5, 27500, 'CHECK'), (4, 12000, 6, 38500, 'CHECK'),
                                                                                                          (4, 12000, 2, 49500, 'CHECK'), (4, 12000, 7, 49500, 'CHECK'), (4, 12000, 8, 49500, 'CHECK'), (4, 12000, 9, 38500, 'CHECK'), (4, 12000, 10, 49500, 'CHECK'),
                                                                                                          (4, 12000, 11, 49500, 'CHECK'), (4, 12000, 12, 38500, 'CHECK'), (4, 12000, 13, 38500, 'CHECK'), (4, 12000, 14, 49500, 'CHECK'), (4, 12000, 15, 38500, 'CHECK'),
                                                                                                          (4, 12000, 16, 55000, 'CHECK'), (4, 12000, 17, 49500, 'CHECK'), (4, 12000, 18, 27500, 'CHECK'), (4, 12000, 19, 38500, 'CHECK'), (4, 12000, 21, 38500, 'CHECK');
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
UPDATE model_package_items SET price = 242000 WHERE vehicle_model_id = 4 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 440000 WHERE vehicle_model_id = 4 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 418000 WHERE vehicle_model_id = 4 AND milestone_km = 120000 AND service_item_id = 6;

-- ---------------------------------
-- --- MODEL: VF 7 (model_id=5) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d605c7.png) - Giá như VF6 (Ước tính)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 12000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
UPDATE model_package_items SET price = 242000 WHERE vehicle_model_id = 5 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 440000 WHERE vehicle_model_id = 5 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 418000 WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 330000 WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 3;


-- ---------------------------------
-- --- MODEL: VF 8 (model_id=6) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d605e2.png) - Giá như VFe34 (Ước tính)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 12000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000 AND service_item_id != 4; -- VF8 không có pin chìa khóa
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 250000 WHERE vehicle_model_id = 6 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 400000 WHERE vehicle_model_id = 6 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 350000 WHERE vehicle_model_id = 6 AND milestone_km = 120000 AND service_item_id = 6;


-- ---------------------------------
-- --- MODEL: VF 9 (model_id=7) ---
-- ---------------------------------
-- (Dựa trên ảnh image_d605ff.png) - Giá tăng 15% so với VF8 (Ước tính)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 12000, service_item_id, price * 1.15, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 24000km (Như 12k + Thay dầu phanh)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 24000, service_item_id, price, CASE WHEN service_item_id = 2 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
UPDATE model_package_items SET price = 287500 WHERE vehicle_model_id = 7 AND milestone_km = 24000 AND service_item_id = 2; -- 250k * 1.15
-- Mốc 36000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 48000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
-- Mốc 60000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 72000km (Như 24k + Thay Pin T-Box)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 72000, service_item_id, price, CASE WHEN service_item_id = 5 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 460000 WHERE vehicle_model_id = 7 AND milestone_km = 72000 AND service_item_id = 5; -- 400k * 1.15
-- Mốc 84000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 96000km (Như 24k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
-- Mốc 108000km (Như 12k)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 120000km (Như 24k + Thay nước làm mát + Thay hệ thống ĐH)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 120000, service_item_id, price, CASE WHEN service_item_id = 6 THEN 'REPLACE' WHEN service_item_id = 3 THEN 'REPLACE' ELSE action_type END FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 402500 WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 6; -- 350k * 1.15
UPDATE model_package_items SET price = 345000 WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 3; -- Ước tính

-- =================================================================
-- SERVICE CENTER (Giữ nguyên)
-- =================================================================

INSERT INTO service_centers (name, address, district, city, phone)
VALUES
    ('Showroom Vin3S TT Hóc Môn', '166 Lý Thường Kiệt, Khu phố 3 Thị trấn Hóc Môn', 'Huyện Hóc Môn', 'TP Hồ Chí Minh', '0762718718'),
    ('Showroom Vin3S 39A Hà Huy Giáp', '39A Hà Huy Giáp', 'Quận 12', 'TP Hồ Chí Minh', '0702721721'),
    ('Showroom Vin3S 314 Nguyễn Ảnh Thủ - Hiệp Thành', '314A Đường Nguyễn Ảnh Thủ Phường Hiệp Thành', 'Quận 12', 'TP Hồ Chí Minh', '0796621621'),
    ('Showroom Vin3S Nhà Bè', '2250 Huỳnh Tấn Phát Xã Phú Xuân', 'Huyện Nhà Bè', 'TP Hồ Chí Minh', '0946143939'),
    ('Showroom Vin3S TT Củ Chi', '158 TL8 Thị trấn Củ Chi', 'Huyện Củ Chi', 'TP Hồ Chí Minh', '0904404692'),
    ('Showroom Vin3S 217A Bến Vân Đồn', '217A Bến Vân Đồn Phường 2', 'Quận 4', 'TP Hồ Chí Minh', '0938362217'),
    ('Showroom Vin3S 57 Phạm Ngọc Thạch', '57 Phạm Ngọc Thạch Phường 6', 'Quận 3', 'TP Hồ Chí Minh', '0938399843'),
    ('Showroom Vin3S Lê Quang Định', '486 Lê Quang Định Phường 11', 'Quận Bình Thạnh', 'TP Hồ Chí Minh', '0796553553'),
    ('Showroom Vin3S Nguyễn Thị Tú', '212 Nguyễn Thị Tú, Khu Phố 2 Phường Bình Hưng Hòa B', 'Quận Bình Tân', 'TP Hồ Chí Minh', '0973565179'),
    ('Showroom Vin3S 677 Âu Cơ', '677 Âu Cơ Phường Tân Thành', 'Quận Tân Phú', 'TP Hồ Chí Minh', '0971971010'),
    ('Showroom Vin3S 594 Lê Văn Quới', '594 Lê Văn Quới Phường Bình Hưng Hòa A', 'Quận Bình Tân', 'TP Hồ Chí Minh', '0867036536'),
    ('Showroom Vin3S 307 Lạc Long Quân', '305-307-309 Lạc Long Quân Phường 3', 'Quận 11', 'TP Hồ Chí Minh', '0979905353'),
    ('Showroom Vin3S 255 Hiệp Bình Phước', '255 Quốc lộ 13 Phường Hiệp Bình Phước', 'TP Thủ Đức', 'TP Hồ Chí Minh', '0964100808'),
    ('Showroom Vin3S 460 Nguyễn Văn Luông', '460 Nguyễn Văn Luông Phường 12', 'Quận 6', 'TP Hồ Chí Minh', '0971801010'),
    ('Showroom Vin3S 337 Đỗ Xuân Hợp', '337 Đỗ Xuân Hợp Phường Phước Long B', 'TP Thủ Đức', 'TP Hồ Chí Minh', '0768420420'),
    ('Showroom Vin3S Bình Chánh', 'C8/1 Phạm Hùng Xã Bình Hưng', 'Huyện Bình Chánh', 'TP Hồ Chí Minh', '0963764839'),
    ('NPP ủy quyền 3S VinFast Thuận Nhân', '447- 447A- 447B Cộng Hòa Phường 15', 'Quận Tân Bình', 'TP Hồ Chí Minh', '0965201018'),
    ('NPP ủy quyền 3S VinFast Skytt', '214 Nguyễn Oanh Phường 17', 'Quận Gò Vấp', 'TP Hồ Chí Minh', '02873032689'),
    ('VinFast Lê Văn Việt', 'Tầng 1, TTTM Vincom Plaza Lê Văn Việt, 50 Lê Văn Việt Phường Hiệp Phú', 'Quận 9', 'TP Hồ Chí Minh', '0981335517'),
    ('VinFast Thảo Điền', 'Tầng L1, TTTM Vincom Mega Mall Thảo Điền, 159 Xa lộ Hà Nội Phường Thảo Điền', 'Quận 2', 'TP Hồ Chí Minh', '0981335514');


-- =================================================================
-- PART-CATEGORIES
-- =================================================================
INSERT  INTO part_categories
(id, name, code, description, create_at, created_by, update_at, updated_by)
VALUES
    (1, N'Bộ lọc', 'FILTER', N'Các loại lọc gió, lọc điều hòa', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (2, N'Chất lỏng & Hóa chất', 'LIQUID', N'Dầu phanh, nước làm mát...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (3, N'Pin & Điện tử', 'ELECTRONIC', N'Pin chìa khóa, Pin T-Box, Cảm biến...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (4, N'Gạt mưa & Rửa kính', 'WIPER', N'Lưỡi gạt mưa, bơm nước rửa kính...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (5, N'Ắc quy', 'BATTERY', N'Ắc quy 12V', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (6, N'Hệ thống Phanh', 'BRAKE', N'Má phanh, đĩa phanh...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (7, N'Hệ thống Treo & Lái', 'SUSPENSION', N'Giảm xóc, rô-tuyn, khớp cầu...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (8, N'Hệ thống Truyền động', 'DRIVETRAIN', N'Trục láp, khớp...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (9, N'Hệ thống Làm mát & Điều hòa', 'COOLING', N'Ống dẫn, van, cảm biến...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (10, N'Hệ thống Điện Cao áp', 'HIGH_VOLTAGE', N'Cáp sạc, cầu chì...', NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
    (11, N'Lốp & Vành', 'WHEEL', N'Lốp xe, van cảm biến áp suất...', NOW(), 'SYSTEM', NOW(), 'SYSTEM');


-- =================================================================
-- SPARE-PARTS
-- =================================================================
INSERT INTO spare_parts
(part_number, name, unit_price, quantity_in_stock, minimum_stock_level, category_id, create_at, created_by, update_at, updated_by)
VALUES
-- === PHỤ TÙNG CHO VF 3 ===
('FLT-VF3', N'Lọc gió điều hòa VF 3', 490000.00, 50, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF3', N'Dầu phanh DOT 4 (VF 3)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-KEY-GEN', N'Pin chìa khóa CR2032 (Chung)', 45000.00, 300, 50, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Chung
('BAT-TBOX-VF3', N'Pin bộ T-Box VF 3', 650000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF3', N'Ắc quy 12V AGM 60Ah (VF 3)', 4300000.00, 15, 5, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF3', N'Má phanh trước VF 3', 990000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Vẫn giữ F/R để phân biệt
('BRK-PAD-R-VF3', N'Má phanh sau VF 3', 920000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF3', N'Đĩa phanh trước VF 3', 1580000.00, 10, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF3', N'Giảm xóc trước VF 3', 1950000.00, 10, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF3', N'Rô-tuyn trụ dưới VF 3', 780000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF3', N'Rô-tuyn lái ngoài VF 3', 720000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF3', N'Khớp trục láp VF 3', 2450000.00, 5, 2, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF3', N'Ống nước làm mát chính VF 3', 520000.00, 10, 3, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF3', N'Van tiết lưu điều hòa VF 3', 990000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF3', N'Cáp sạc VF 3', 6200000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF3', N'Cầu chì tổng HV VF 3', 1450000.00, 5, 2, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF3', N'Lốp xe VF 3 (175/75R16)', 1850000.00, 30, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF3', N'Van cảm biến áp suất lốp VF 3', 850000.00, 40, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF3', N'Bơm nước rửa kính VF 3', 520000.00, 10, 3, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF3', N'Bộ lưỡi gạt mưa VF 3', 480000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === PHỤ TÙNG CHO VF 5 ===
('FLT-VF5', N'Lọc gió điều hòa VF 5', 490000.00, 50, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF5', N'Dầu phanh DOT 4 (VF 5)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF5', N'Nước làm mát EV Loại 1 (VF 5)', 650000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
-- BAT-KEY-GEN dùng chung
('BAT-TBOX-VF5', N'Pin bộ T-Box VF 5', 680000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF5', N'Ắc quy 12V AGM 60Ah (VF 5)', 4300000.00, 15, 5, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF5', N'Má phanh trước VF 5', 1020000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF5', N'Má phanh sau VF 5', 960000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF5', N'Đĩa phanh trước VF 5', 1620000.00, 10, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF5', N'Giảm xóc trước VF 5', 2020000.00, 10, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF5', N'Rô-tuyn trụ dưới VF 5', 820000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF5', N'Rô-tuyn lái ngoài VF 5', 760000.00, 15, 5, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF5', N'Khớp trục láp VF 5', 2580000.00, 5, 2, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF5', N'Ống nước làm mát chính VF 5', 550000.00, 10, 3, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF5', N'Van tiết lưu điều hòa VF 5', 1020000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF5', N'Cáp sạc VF 5', 6400000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF5', N'Cầu chì tổng HV VF 5', 1520000.00, 5, 2, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF5', N'Lốp xe VF 5', 2050000.00, 30, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF5', N'Van cảm biến áp suất lốp VF 5', 860000.00, 40, 10, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF5', N'Bơm nước rửa kính VF 5', 540000.00, 10, 3, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === PHỤ TÙNG CHO VF 6 ===
('FLT-VF6', N'Lọc gió điều hòa VF 6', 520000.00, 40, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF6', N'Dầu phanh DOT 4 (VF 6)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF6', N'Nước làm mát EV Loại 1 (VF 6)', 650000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF6', N'Pin bộ T-Box VF 6', 680000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF6', N'Bộ Lưỡi gạt mưa VF 6', 720000.00, 30, 10, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF6', N'Ắc quy 12V AGM 70Ah (VF 6)', 4700000.00, 10, 3, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF6', N'Má phanh trước VF 6', 1150000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF6', N'Má phanh sau VF 6', 1080000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF6', N'Đĩa phanh trước VF 6', 1850000.00, 5, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF6', N'Giảm xóc trước VF 6', 2200000.00, 5, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF6', N'Rô-tuyn trụ dưới VF 6', 850000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF6', N'Rô-tuyn lái ngoài VF 6', 790000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF6', N'Khớp trục láp VF 6', 2700000.00, 3, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF6', N'Ống nước làm mát chính VF 6', 570000.00, 8, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF6', N'Van tiết lưu điều hòa VF 6', 1050000.00, 3, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF6', N'Cáp sạc VF 6', 6500000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF6', N'Cầu chì tổng HV VF 6', 1550000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF6', N'Lốp xe VF 6', 2450000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF6', N'Van cảm biến áp suất lốp VF 6', 860000.00, 30, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF6', N'Bơm nước rửa kính VF 6', 540000.00, 8, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === PHỤ TÙNG CHO VF 7 ===
('FLT-VF7', N'Lọc gió điều hòa VF 7', 520000.00, 40, 10, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF7', N'Dầu phanh DOT 4 (VF 7)', 380000.00, 30, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF7', N'Nước làm mát EV Loại 1 (VF 7)', 650000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF7', N'Pin bộ T-Box VF 7', 680000.00, 20, 5, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF7', N'Bộ Lưỡi gạt mưa VF 7', 730000.00, 30, 10, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF7', N'Ắc quy 12V AGM 70Ah (VF 7)', 4700000.00, 10, 3, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF7', N'Má phanh trước VF 7', 1160000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF7', N'Má phanh sau VF 7', 1090000.00, 15, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF7', N'Đĩa phanh trước VF 7', 1860000.00, 5, 2, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF7', N'Giảm xóc trước VF 7', 2250000.00, 5, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF7', N'Rô-tuyn trụ dưới VF 7', 860000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF7', N'Rô-tuyn lái ngoài VF 7', 800000.00, 10, 3, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF7', N'Khớp trục láp VF 7', 2750000.00, 3, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF7', N'Ống nước làm mát chính VF 7', 580000.00, 8, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF7', N'Van tiết lưu điều hòa VF 7', 1060000.00, 3, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF7', N'Cáp sạc VF 7', 6600000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF7', N'Cầu chì tổng HV VF 7', 1560000.00, 3, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF7', N'Lốp xe VF 7', 2750000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF7', N'Van cảm biến áp suất lốp VF 7', 860000.00, 30, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF7', N'Bơm nước rửa kính VF 7', 540000.00, 8, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === PHỤ TÙNG CHO VFe34 ===
('FLT-VFE34', N'Lọc gió điều hòa VFe34', 520000.00, 30, 5, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VFE34', N'Dầu phanh DOT 4 (VFe34)', 380000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VFE34', N'Nước làm mát EV Loại 2 (VFe34)', 680000.00, 15, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VFE34', N'Pin bộ T-Box VFe34', 700000.00, 10, 2, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VFE34', N'Ắc quy 12V AGM 70Ah (VFe34)', 4700000.00, 8, 2, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VFE34', N'Má phanh trước VFe34', 1380000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VFE34', N'Má phanh sau VFe34', 1280000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VFE34', N'Đĩa phanh trước VFe34', 2150000.00, 5, 1, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VFE34', N'Giảm xóc trước VFe34', 2500000.00, 3, 1, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VFE34', N'Rô-tuyn trụ dưới VFe34', 900000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VFE34', N'Rô-tuyn lái ngoài VFe34', 850000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VFE34', N'Khớp trục láp VFe34', 3000000.00, 2, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VFE34', N'Ống nước làm mát chính VFe34', 600000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VFE34', N'Van tiết lưu điều hòa VFe34', 1100000.00, 2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VFE34', N'Cáp sạc VFe34', 6800000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VFE34', N'Cầu chì tổng HV VFe34', 1600000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VFE34', N'Lốp xe VFe34', 2950000.00, 15, 3, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VFE34', N'Van cảm biến áp suất lốp VFe34', 860000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VFE34', N'Bơm nước rửa kính VFe34', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === PHỤ TÙNG CHO VF 8 ===
('FLT-VF8', N'Lọc gió điều hòa VF 8', 530000.00, 30, 5, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF8', N'Dầu phanh DOT 4 (VF 8)', 380000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF8', N'Nước làm mát EV Loại 2 (VF 8)', 690000.00, 15, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF8', N'Pin bộ T-Box VF 8', 710000.00, 10, 2, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF8', N'Ắc quy 12V AGM 70Ah (VF 8)', 4700000.00, 8, 2, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF8', N'Má phanh trước VF 8', 1390000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF8', N'Má phanh sau VF 8', 1290000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF8', N'Đĩa phanh trước VF 8', 2160000.00, 5, 1, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF8', N'Giảm xóc trước VF 8', 2550000.00, 3, 1, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF8', N'Rô-tuyn trụ dưới VF 8', 910000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF8', N'Rô-tuyn lái ngoài VF 8', 860000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF8', N'Khớp trục láp VF 8', 3050000.00, 2, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF8', N'Ống nước làm mát chính VF 8', 610000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF8', N'Van tiết lưu điều hòa VF 8', 1110000.00, 2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF8', N'Cáp sạc VF 8', 6900000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF8', N'Cầu chì tổng HV VF 8', 1610000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF8', N'Lốp xe VF 8', 3600000.00, 15, 3, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF8', N'Van cảm biến áp suất lốp VF 8', 860000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF8', N'Bơm nước rửa kính VF 8', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),

-- === PHỤ TÙNG CHO VF 9 ===
('FLT-VF9', N'Lọc gió điều hòa VF 9', 550000.00, 30, 5, 1, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-BRK-VF9', N'Dầu phanh DOT 4 (VF 9)', 380000.00, 20, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('FLD-COOL-VF9', N'Nước làm mát EV Loại 2 (VF 9)', 700000.00, 15, 5, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-TBOX-VF9', N'Pin bộ T-Box VF 9', 720000.00, 10, 2, 3, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BAT-12V-VF9', N'Ắc quy 12V AGM 70Ah (VF 9)', 4700000.00, 8, 2, 5, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-F-VF9', N'Má phanh trước VF 9', 1400000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-PAD-R-VF9', N'Má phanh sau VF 9', 1300000.00, 10, 3, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-DISC-F-VF9', N'Đĩa phanh trước VF 9', 2170000.00, 5, 1, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-SHOCK-F-VF9', N'Giảm xóc trước VF 9', 2600000.00, 3, 1, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-BALLJ-VF9', N'Rô-tuyn trụ dưới VF 9', 920000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('SUS-TIEROD-VF9', N'Rô-tuyn lái ngoài VF 9', 870000.00, 8, 2, 7, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('DRV-AXLE-VF9', N'Khớp trục láp VF 9', 3100000.00, 2, 1, 8, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('COOL-HOSE-VF9', N'Ống nước làm mát chính VF 9', 620000.00, 5, 2, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('AC-VALVE-VF9', N'Van tiết lưu điều hòa VF 9', 1120000.00, 2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-CABLE-VF9', N'Cáp sạc VF 9', 7000000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('HV-FUSE-VF9', N'Cầu chì tổng HV VF 9', 1620000.00, 2, 1, 10, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TIRE-VF9', N'Lốp xe VF 9', 4200000.00, 15, 3, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WHL-TPMS-VF9', N'Van cảm biến áp suất lốp VF 9', 860000.00, 20, 5, 11, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-PUMP-VF9', N'Bơm nước rửa kính VF 9', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM');

-- TẮT KIỂM TRA KHÓA NGOẠI (Để cho phép xóa)
SET FOREIGN_KEY_CHECKS = 0;

-- LÀM RỖNG HAI BẢNG
TRUNCATE TABLE spare_parts;
TRUNCATE TABLE part_categories;

-- BẬT LẠI KIỂM TRA KHÓA NGOẠI
SET FOREIGN_KEY_CHECKS = 1;