-- ========================================
-- 1. ROLES
-- ========================================
INSERT IGNORE INTO roles (name, create_at, created_by) VALUES
('CUSTOMER', NOW(), 'SYSTEM'),
('TECHNICIAN', NOW(), 'SYSTEM'),
('STAFF', NOW(), 'SYSTEM'),
('ADMIN', NOW(), 'SYSTEM');

-- ========================================
-- 2. USERS
-- Password for all: "triet123" → BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq
-- ========================================
INSERT IGNORE INTO users (username, email, password, phone, full_name, gender, role_id, create_at, update_at) VALUES
-- CUSTOMER (role_id = 1)
('baobao1', 'baobao1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234001', N'Bảo Bảo', 'MALE', 1, NOW(), NOW()),
('triettriet1', 'triettriet1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234002', N'Triết Triết', 'FEMALE', 1, NOW(), NOW()),
('nguyennguyen1', 'nguyennguyen1@example.com', '$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234003', N'Nguyên Nguyên', 'MALE', 1, NOW(), NOW()),
('thaothao1', 'thaothao1@example.com', '$$2a$10$9ViHL2oWm7hurneJAixnW.6E2OUqWYaSrALzF3sVirp9.EwWnqncG', '0901234004', N'Thảo Thảo', 'FEMALE', 1,  NOW(), NOW()),
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

-- Nên đặt tên database của bạn ở đây
-- USE <your_database_name>;
SET NAMES utf8mb4;

-- =================================================================
-- BƯỚC 1: ĐỊNH NGHĨA CÁC "LOẠI" GÓI DỊCH VỤ (SERVICE_PACKAGES)
-- =================================================================
-- (Giả sử các trường `id`, `create_at`, `update_at` là tự động)
INSERT INTO service_packages (name, description) VALUES
                                                     (N'Bảo dưỡng cơ bản', N'Gói bảo dưỡng định kỳ cấp 1 (ví dụ: 12.000km, 36.000km)'),
                                                     (N'Bảo dưỡng tiêu chuẩn', N'Gói bảo dưỡng định kỳ cấp 2 (ví dụ: 24.000km, 72.000km)'),
                                                     (N'Bảo dưỡng cao cấp', N'Gói bảo dưỡng định kỳ cấp 3 (ví dụ: 48.000km, 96.000km)'),
                                                     (N'Bảo dưỡng pin chuyên sâu', N'Gói bảo dưỡng đặc biệt cho hệ thống pin cao áp');


-- =================================================================
-- BƯỚC 2: ĐỊNH NGHĨA TẤT CẢ CÁC "HẠNG MỤC" DỊCH VỤ LẺ (SERVICE_ITEMS)
-- =================================================================
-- Đây là danh sách tổng hợp tất cả hạng mục có thể có trên TẤT CẢ các model
INSERT INTO service_items (name, description) VALUES
                                                  (N'Lọc gió điều hòa', N'Thay thế hoặc vệ sinh lọc gió cabin'),
                                                  (N'Dầu phanh', N'Kiểm tra hoặc thay thế dầu phanh'),
                                                  (N'Bảo dưỡng hệ thống điều hòa', N'Kiểm tra, vệ sinh hệ thống điều hòa'),
                                                  (N'Pin chìa khóa điều khiển', N'Kiểm tra hoặc thay pin chìa khóa'),
                                                  (N'Pin bộ T-Box', N'Kiểm tra hoặc thay pin T-Box'),
                                                  (N'Nước làm mát cho Pin/Động cơ', N'Kiểm tra hoặc thay/bổ sung nước làm mát'),
                                                  (N'Lốp (áp suất, độ mòn, vành...)', N'Kiểm tra lốp, vành, cân bằng động, đảo lốp'),
                                                  (N'Má phanh và đĩa phanh', N'Kiểm tra độ mòn má phanh, đĩa phanh'),
                                                  (N'Đường ống, đầu nối hệ thống phanh', N'Kiểm tra rò rỉ, nứt vỡ đường ống phanh'),
                                                  (N'Bộ dẫn động (Động cơ điện và hộp số)', N'Kiểm tra tình trạng hoạt động'),
                                                  (N'Hệ thống treo', N'Kiểm tra giảm xóc, lò xo, các liên kết'),
                                                  (N'Trục truyền động', N'Kiểm tra trục truyền động, khớp chữ thập'),
                                                  (N'Khớp cầu', N'Kiểm tra các khớp cầu, rotuyn'),
                                                  (N'Thước lái và khớp nối cầu', N'Kiểm tra hệ thống lái'),
                                                  (N'Đường ống làm mát', N'Kiểm tra rò rỉ, nứt vỡ đường ống hệ thống làm mát'),
                                                  (N'Pin cao áp (EV)', N'Kiểm tra tình trạng pin, quạt làm mát pin'),
                                                  (N'Dây cáp cửa hệ thống điện áp cao', N'Kiểm tra dây cáp cao áp'),
                                                  (N'Cổng sạc', N'Kiểm tra, vệ sinh cổng sạc'),
                                                  (N'Ắc quy 12V', N'Kiểm tra tình trạng ắc quy 12V'),
                                                  (N'Gạt mưa / Nước rửa kính', N'Kiểm tra và bổ sung nước rửa kính, thay gạt mưa nếu cần'),
                                                  (N'Kiểm tra gỉ sét / ăn mòn gầm xe', N'Kiểm tra tổng thể gầm xe');


-- =================================================================
-- BƯỚC 3: ĐỊNH NGHĨA CÁC MODEL XE VÀ "QUY TẮC CHU KỲ" (VEHICLE_MODELS)
-- =================================================================
-- Chúng ta liên kết các quy tắc chu kỳ với các Gói đã tạo ở BƯỚC 1
INSERT INTO vehicle_models (name, model_year, basic_cycle_km, basic_cycle_months, basic_package_id, standard_cycle_km, standard_cycle_months, standard_package_id, premium_cycle_km, premium_cycle_months, premium_package_id, battery_cycle_km, battery_cycle_months, battery_package_id) VALUES
                                                                                                                                                                                                                                                                                               (N'VFe34', N'2021', 12000, 12, 1, 24000, 24, 2, 48000, 48, 3, 48000, 48, 4),
                                                                                                                                                                                                                                                                                               (N'VF 3', N'2024', 12000, 12, 1, 24000, 24, 2, 48000, 36, 3, NULL, NULL, NULL), -- VF3 không có gói Pin
                                                                                                                                                                                                                                                                                               (N'VF 5', N'2023', 12000, 12, 1, 24000, 24, 2, 48000, 48, 3, 48000, 48, 4),
                                                                                                                                                                                                                                                                                               (N'VF 6', N'2023', 12000, 12, 1, 24000, 24, 2, 48000, 48, 3, 48000, 48, 4),
                                                                                                                                                                                                                                                                                               (N'VF 7', N'2024', 12000, 12, 1, 24000, 24, 2, 48000, 48, 3, 48000, 48, 4),
                                                                                                                                                                                                                                                                                               (N'VF 8', N'2022', 12000, 12, 1, 24000, 24, 2, 48000, 48, 3, 48000, 48, 4),
                                                                                                                                                                                                                                                                                               (N'VF 9', N'2022', 12000, 12, 1, 24000, 24, 2, 48000, 48, 3, 48000, 48, 4);


-- =================================================================
-- BƯỚC 4: ĐỊNH NGHĨA "MENU" DỊCH VỤ (MODEL_PACKAGE_ITEMS)
-- =================================================================

-- ---------------------------------
-- --- MODEL: VFe34 (model_id=1) ---
-- ---------------------------------
-- (Dựa trên ảnh lịch bảo dưỡng VFe34)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Lọc gió điều hòa'), 150000, 'REPLACE'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Dầu phanh'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Pin chìa khóa điều khiển'), 30000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Pin bộ T-Box'), 30000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ'), 40000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Lốp (áp suất, độ mòn, vành...)'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Má phanh và đĩa phanh'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Đường ống, đầu nối hệ thống phanh'), 40000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Bộ dẫn động (Động cơ điện và hộp số)'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Hệ thống treo'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Trục truyền động'), 40000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Khớp cầu'), 40000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Thước lái và khớp nối cầu'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Đường ống làm mát'), 40000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Pin cao áp (EV)'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Dây cáp cửa hệ thống điện áp cao'), 50000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Cổng sạc'), 30000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Ắc quy 12V'), 40000, 'CHECK'),
                                                                                                                (1, 1, (SELECT id FROM service_items WHERE name=N'Gạt mưa / Nước rửa kính'), 30000, 'CHECK');

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
-- (Giống gói 1, nhưng 'Dầu phanh' là 'REPLACE')
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 1, 2, service_item_id, price,
       CASE
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Dầu phanh') THEN 'REPLACE'
           ELSE action_type
           END
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id = 1;
-- Cập nhật lại giá cho Dầu phanh (vì thay thế đắt hơn kiểm tra)
UPDATE model_package_items SET price = 250000 WHERE vehicle_model_id = 1 AND service_package_id = 2 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Dầu phanh');

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
-- (Giống gói 1, nhưng 'Pin chìa khóa' là 'REPLACE' và 'BH hệ thống ĐH' là 'REPLACE' - giả định)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 1, 3, service_item_id, price,
       CASE
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Pin chìa khóa điều khiển') THEN 'REPLACE'
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa') THEN 'REPLACE'
           ELSE action_type
           END
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id = 1;
-- Cập nhật giá
UPDATE model_package_items SET price = 100000 WHERE vehicle_model_id = 1 AND service_package_id = 3 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Pin chìa khóa điều khiển');
UPDATE model_package_items SET price = 300000 WHERE vehicle_model_id = 1 AND service_package_id = 3 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa');

-- Gói 4: BẢO DƯỠNG PIN (package_id=4)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (1, 4, (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ'), 350000, 'REPLACE'),
                                                                                                                (1, 4, (SELECT id FROM service_items WHERE name=N'Pin bộ T-Box'), 400000, 'REPLACE'),
                                                                                                                (1, 4, (SELECT id FROM service_items WHERE name=N'Pin cao áp (EV)'), 200000, 'CHECK'),
                                                                                                                (1, 4, (SELECT id FROM service_items WHERE name=N'Cổng sạc'), 50000, 'CHECK'),
                                                                                                                (1, 4, (SELECT id FROM service_items WHERE name=N'Dây cáp cửa hệ thống điện áp cao'), 100000, 'CHECK');

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VFe34
-- Đây là giá khi khách hàng chỉ muốn làm 1 dịch vụ duy nhất
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Lọc gió điều hòa'), 180000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Dầu phanh'), 300000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa'), 350000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Pin chìa khóa điều khiển'), 120000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Pin bộ T-Box'), 450000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ'), 400000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Ắc quy 12V'), 1500000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Gạt mưa / Nước rửa kính'), 250000, 'REPLACE'),
                                                                                                                (1, NULL, (SELECT id FROM service_items WHERE name=N'Lốp (áp suất, độ mòn, vành...)'), 100000, 'CHECK');
-- (Thêm các dịch vụ lẻ CHECK khác nếu cần)

-- ---------------------------------
-- --- MODEL: VF 3 (model_id=2) ---
-- ---------------------------------
-- (Dựa trên ảnh lịch bảo dưỡng VF 3, xe này đơn giản hơn)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Lọc gió điều hòa'), 120000, 'REPLACE'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Gạt mưa / Nước rửa kính'), 20000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Hệ thống treo'), 40000, 'CHECK'), -- VF3 gọi là "Hệ thống điều hòa"
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Dầu phanh'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Lốp (áp suất, độ mòn, vành...)'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Má phanh và đĩa phanh'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Đường ống, đầu nối hệ thống phanh'), 30000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Bộ dẫn động (Động cơ điện và hộp số)'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Hệ thống treo'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Trục truyền động'), 30000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Khớp cầu'), 30000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Thước lái và khớp nối cầu'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Pin cao áp (EV)'), 50000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Dây cáp cửa hệ thống điện áp cao'), 40000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Cổng sạc'), 20000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Ắc quy 12V'), 30000, 'CHECK'),
                                                                                                                (2, 1, (SELECT id FROM service_items WHERE name=N'Kiểm tra gỉ sét / ăn mòn gầm xe'), 30000, 'CHECK');

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
-- (Giống gói 1, nhưng 'Dầu phanh' là 'REPLACE')
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 2, 2, service_item_id, price,
       CASE
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Dầu phanh') THEN 'REPLACE'
           ELSE action_type
           END
FROM model_package_items WHERE vehicle_model_id = 2 AND service_package_id = 1;
-- Cập nhật giá
UPDATE model_package_items SET price = 200000 WHERE vehicle_model_id = 2 AND service_package_id = 2 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Dầu phanh');

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
-- (Giống gói 2, nhưng 'Hệ thống điều hòa' là 'REPLACE')
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 2, 3, service_item_id, price,
       CASE
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa') THEN 'REPLACE'
           ELSE action_type
           END
FROM model_package_items WHERE vehicle_model_id = 2 AND service_package_id = 2;
-- Cập nhật giá
UPDATE model_package_items SET price = 250000 WHERE vehicle_model_id = 2 AND service_package_id = 3 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa');

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VF 3
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (2, NULL, (SELECT id FROM service_items WHERE name=N'Lọc gió điều hòa'), 150000, 'REPLACE'),
                                                                                                                (2, NULL, (SELECT id FROM service_items WHERE name=N'Dầu phanh'), 220000, 'REPLACE'),
                                                                                                                (2, NULL, (SELECT id FROM service_items WHERE name=N'Bảo dưỡng hệ thống điều hòa'), 280000, 'REPLACE'),
                                                                                                                (2, NULL, (SELECT id FROM service_items WHERE name=N'Ắc quy 12V'), 1200000, 'REPLACE'),
                                                                                                                (2, NULL, (SELECT id FROM service_items WHERE name=N'Gạt mưa / Nước rửa kính'), 200000, 'REPLACE');


-- =================================================================
-- --- MODEL: VF 5 (model_id=3) ---
-- =================================================================
-- (Dựa trên ảnh VF 5 và logic 4 Gói)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Lọc gió điều hòa'), 140000, 'REPLACE'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Gạt mưa / Nước rửa kính'), 25000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Hệ thống treo'), 45000, 'CHECK'), -- VF5 gọi là "Hệ thống điều hòa không khí"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Pin bộ T-Box'), 25000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ'), 35000, 'CHECK'), -- "Chất lỏng làm mát E-Motor"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Dầu phanh'), 45000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Lốp (áp suất, độ mòn, vành...)'), 45000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Má phanh và đĩa phanh'), 45000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Đường ống, đầu nối hệ thống phanh'), 35000, 'CHECK'), -- "Dây phanh, ống mềm và Kết nối"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Bộ dẫn động (Động cơ điện và hộp số)'), 45000, 'CHECK'), -- "Bộ truyền động điện"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Hệ thống treo'), 45000, 'CHECK'), -- "Bộ phận của trục và hệ thống treo"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Trục truyền động'), 35000, 'CHECK'), -- "Trục và cầu xe truyền động"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Khớp cầu'), 35000, 'CHECK'), -- "Khớp ổ bi hệ thống treo"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Thước lái và khớp nối cầu'), 45000, 'CHECK'), -- "Liên kết lái và Khớp ổ bi"
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Đường ống làm mát'), 35000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Pin cao áp (EV)'), 50000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Dây cáp cửa hệ thống điện áp cao'), 45000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Cổng sạc'), 25000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Ắc quy 12V'), 35000, 'CHECK'),
                                                                                                                (3, 1, (SELECT id FROM service_items WHERE name=N'Kiểm tra gỉ sét / ăn mòn gầm xe'), 35000, 'CHECK');

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
-- (Giống gói 1, nhưng 'Dầu phanh' là 'REPLACE')
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 3, 2, service_item_id, price,
       CASE
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Dầu phanh') THEN 'REPLACE'
           ELSE action_type
           END
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id = 1;
UPDATE model_package_items SET price = 220000 WHERE vehicle_model_id = 3 AND service_package_id = 2 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Dầu phanh');

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
-- (Giống gói 2, nhưng 'Nước làm mát' là 'REPLACE')
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 3, 3, service_item_id, price,
       CASE
           WHEN service_item_id = (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ') THEN 'REPLACE'
           ELSE action_type
           END
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id = 2;
UPDATE model_package_items SET price = 350000 WHERE vehicle_model_id = 3 AND service_package_id = 3 AND service_item_id = (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ');

-- Gói 4: BẢO DƯỠNG PIN (package_id=4)
-- (Theo logic 48k km)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (3, 4, (SELECT id FROM service_items WHERE name=N'Pin cao áp (EV)'), 150000, 'CHECK'),
                                                                                                                (3, 4, (SELECT id FROM service_items WHERE name=N'Dây cáp cửa hệ thống điện áp cao'), 100000, 'CHECK'),
                                                                                                                (3, 4, (SELECT id FROM service_items WHERE name=N'Cổng sạc'), 50000, 'CHECK');
-- (Pin T-Box của VF5 thay ở 72k km, nó sẽ được xử lý bằng logic 'checkRule' của Gói 2 ở 72k km nếu bạn định nghĩa Gói 2 thay Pin T-box)

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VF 5
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type) VALUES
                                                                                                                (3, NULL, (SELECT id FROM service_items WHERE name=N'Lọc gió điều hòa'), 170000, 'REPLACE'),
                                                                                                                (3, NULL, (SELECT id FROM service_items WHERE name=N'Dầu phanh'), 250000, 'REPLACE'),
                                                                                                                (3, NULL, (SELECT id FROM service_items WHERE name=N'Nước làm mát cho Pin/Động cơ'), 380000, 'REPLACE'),
                                                                                                                (3, NULL, (SELECT id FROM service_items WHERE name=N'Pin bộ T-Box'), 400000, 'REPLACE'),
                                                                                                                (3, NULL, (SELECT id FROM service_items WHERE name=N'Ắc quy 12V'), 1300000, 'REPLACE'),
                                                                                                                (3, NULL, (SELECT id FROM service_items WHERE name=N'Gạt mưa / Nước rửa kính'), 220000, 'REPLACE');

-- =================================================================
-- --- MODEL: VF 6 (model_id=4) ---
-- =================================================================
-- (Giá cao hơn VF 5 một chút)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
-- (Sao chép từ VF 5, tăng giá 10%)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 4, 1, service_item_id, price * 1.1, action_type
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id = 1;

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 4, 2, service_item_id, price * 1.1, action_type
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id = 2;

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 4, 3, service_item_id, price * 1.1, action_type
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id = 3;

-- Gói 4: BẢO DƯỠNG PIN (package_id=4)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 4, 4, service_item_id, price * 1.1, action_type
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id = 4;

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VF 6
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 4, NULL, service_item_id, price * 1.1, action_type
FROM model_package_items WHERE vehicle_model_id = 3 AND service_package_id IS NULL;


-- =================================================================
-- --- MODEL: VF 7 (model_id=5) ---
-- =================================================================
-- (Giá cao hơn VF 6 một chút)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 5, 1, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 4 AND service_package_id = 1; -- (Lấy giá từ VF 6)

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 5, 2, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 4 AND service_package_id = 2;

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 5, 3, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 4 AND service_package_id = 3;

-- Gói 4: BẢO DƯỠNG PIN (package_id=4)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 5, 4, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 4 AND service_package_id = 4;

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VF 7
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 5, NULL, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 4 AND service_package_id IS NULL;


-- =================================================================
-- --- MODEL: VF 8 (model_id=6) ---
-- =================================================================
-- (Giá cao hơn VF 7, tương đương VFe34)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
-- (Sao chép từ VFe34 (model_id=1) vì chúng cùng phân khúc)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 6, 1, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id = 1;

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 6, 2, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id = 2;

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 6, 3, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id = 3;

-- Gói 4: BẢO DƯỠNG PIN (package_id=4)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 6, 4, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id = 4;

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VF 8
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 6, NULL, service_item_id, price, action_type
FROM model_package_items WHERE vehicle_model_id = 1 AND service_package_id IS NULL;


-- =================================================================
-- --- MODEL: VF 9 (model_id=7) ---
-- =================================================================
-- (Giá cao nhất, lấy từ VF 8 (model_id=6) và tăng 15%)

-- Gói 1: BẢO DƯỠNG CƠ BẢN (package_id=1)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 7, 1, service_item_id, price * 1.15, action_type
FROM model_package_items WHERE vehicle_model_id = 6 AND service_package_id = 1;

-- Gói 2: BẢO DƯỠNG TIÊU CHUẨN (package_id=2)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 7, 2, service_item_id, price * 1.15, action_type
FROM model_package_items WHERE vehicle_model_id = 6 AND service_package_id = 2;

-- Gói 3: BẢO DƯỠNG CAO CẤP (package_id=3)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 7, 3, service_item_id, price * 1.15, action_type
FROM model_package_items WHERE vehicle_model_id = 6 AND service_package_id = 3;

-- Gói 4: BẢO DƯỠNG PIN (package_id=4)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 7, 4, service_item_id, price * 1.15, action_type
FROM model_package_items WHERE vehicle_model_id = 6 AND service_package_id = 4;

-- DỊCH VỤ LẺ (service_package_id = NULL) cho VF 9
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, action_type)
SELECT 7, NULL, service_item_id, price * 1.15, action_type
FROM model_package_items WHERE vehicle_model_id = 6 AND service_package_id IS NULL;