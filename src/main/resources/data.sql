SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM appointment_service_items;
DELETE FROM model_package_items;
DELETE FROM service_items;
DELETE FROM service_packages;
DELETE FROM vehicle_models;
-- Dữ liệu cho các model xe VinFast
-- Dữ liệu cho các model xe VinFast
INSERT INTO vehicle_models (id, name, model_year, basic_maintenance, comprehensive_maintenance, basic_maintenance_time, comprehensive_maintenance_time, create_at, update_at) VALUES
                                                                                                                                                                                  (1, N'VinFast VF e34', '2023', 10000, 20000, 6, 12, NOW(), NOW()),
                                                                                                                                                                                  (2, N'VinFast VF 8', '2023', 12000, 24000, 6, 12, NOW(), NOW()),
                                                                                                                                                                                  (3, N'VinFast VF 9', '2023', 15000, 30000, 6, 12, NOW(), NOW()),
                                                                                                                                                                                  (4, N'VinFast VF 5', '2023', 8000, 16000, 6, 12, NOW(), NOW()),
                                                                                                                                                                                  (5, N'VinFast VF 6', '2023', 9000, 18000, 6, 12, NOW(), NOW());

-- Gói dịch vụ
INSERT INTO service_packages (id, name, description, create_at, update_at) VALUES
                                                                               (1, N'Bảo dưỡng cơ bản', N'Gói bảo dưỡng định kỳ cơ bản cho xe điện', NOW(), NOW()),
                                                                               (2, N'Bảo dưỡng tiêu chuẩn', N'Gói bảo dưỡng tiêu chuẩn cho xe điện', NOW(), NOW()),
                                                                               (3, N'Bảo dưỡng cao cấp', N'Gói bảo dưỡng toàn diện cho xe điện', NOW(), NOW()),
                                                                               (4, N'Bảo dưỡng pin chuyên sâu', N'Gói bảo dưỡng chuyên sâu hệ thống pin', NOW(), NOW());

-- Dịch vụ lẻ (giá base)
INSERT INTO service_items (id, name, description, price, create_at, update_at) VALUES
                                                                                   (1, N'Kiểm tra hệ thống pin', N'Kiểm tra tổng thể tình trạng pin', 200000, NOW(), NOW()),
                                                                                   (2, N'Kiểm tra động cơ điện', N'Kiểm tra hiệu suất động cơ', 150000, NOW(), NOW()),
                                                                                   (3, N'Kiểm tra hệ thống phanh', N'Kiểm tra má phanh, dầu phanh', 100000, NOW(), NOW()),
                                                                                   (4, N'Kiểm tra hệ thống treo', N'Kiểm tra giảm xóc, lò xo', 120000, NOW(), NOW()),
                                                                                   (5, N'Kiểm tra lốp xe', N'Kiểm tra áp suất, độ mòn lốp', 80000, NOW(), NOW()),
                                                                                   (6, N'Thay dầu phanh', N'Thay mới dầu phanh', 150000, NOW(), NOW()),
                                                                                   (7, N'Vệ sinh hệ thống làm mát pin', N'Vệ sinh hệ thống làm mát pin', 250000, NOW(), NOW()),
                                                                                   (8, N'Cập nhật phần mềm', N'Cập nhật phần mềm xe', 300000, NOW(), NOW()),
                                                                                   (9, N'Kiểm tra hệ thống điện', N'Kiểm tra hệ thống điện', 100000, NOW(), NOW()),
                                                                                   (10, N'Kiểm tra hệ thống sạc', N'Kiểm tra cổng sạc', 180000, NOW(), NOW()),
                                                                                   (11, N'Thay lọc cabin', N'Thay lọc gió cabin', 100000, NOW(), NOW()),
                                                                                   (12, N'Kiểm tra điều hòa', N'Kiểm tra hệ thống điều hòa', 150000, NOW(), NOW()),
                                                                                   (13, N'Kiểm tra hệ thống chiếu sáng', N'Kiểm tra đèn xe', 80000, NOW(), NOW()),
                                                                                   (14, N'Cân bằng bánh xe', N'Cân bằng bánh xe', 120000, NOW(), NOW()),
                                                                                   (15, N'Kiểm tra hệ thống an toàn', N'Kiểm tra túi khí, cảm biến', 200000, NOW(), NOW());

-- ========================================
-- MODEL VF e34 (id = 1)
-- ========================================

-- Gói cơ bản (500,000 VND) cho VF e34
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 1, 1, 120000, NOW(), NOW()),
                                                                                                                         (1, 1, 5, 70000, NOW(), NOW()),
                                                                                                                         (1, 1, 9, 90000, NOW(), NOW()),
                                                                                                                         (1, 1, 13, 70000, NOW(), NOW());

-- Gói tiêu chuẩn (900,000 VND) cho VF e34
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 2, 1, 110000, NOW(), NOW()),
                                                                                                                         (1, 2, 2, 140000, NOW(), NOW()),
                                                                                                                         (1, 2, 3, 90000, NOW(), NOW()),
                                                                                                                         (1, 2, 5, 60000, NOW(), NOW()),
                                                                                                                         (1, 2, 9, 80000, NOW(), NOW()),
                                                                                                                         (1, 2, 10, 160000, NOW(), NOW());

-- Gói cao cấp (1,800,000 VND) cho VF e34
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 3, 1, 100000, NOW(), NOW()),
                                                                                                                         (1, 3, 2, 120000, NOW(), NOW()),
                                                                                                                         (1, 3, 3, 80000, NOW(), NOW()),
                                                                                                                         (1, 3, 4, 100000, NOW(), NOW()),
                                                                                                                         (1, 3, 5, 50000, NOW(), NOW()),
                                                                                                                         (1, 3, 6, 130000, NOW(), NOW()),
                                                                                                                         (1, 3, 8, 280000, NOW(), NOW()),
                                                                                                                         (1, 3, 9, 70000, NOW(), NOW()),
                                                                                                                         (1, 3, 11, 80000, NOW(), NOW()),
                                                                                                                         (1, 3, 12, 120000, NOW(), NOW()),
                                                                                                                         (1, 3, 13, 60000, NOW(), NOW()),
                                                                                                                         (1, 3, 14, 100000, NOW(), NOW()),
                                                                                                                         (1, 3, 15, 180000, NOW(), NOW());

-- Gói pin chuyên sâu (1,300,000 VND) cho VF e34
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 4, 1, 180000, NOW(), NOW()),
                                                                                                                         (1, 4, 7, 220000, NOW(), NOW()),
                                                                                                                         (1, 4, 10, 150000, NOW(), NOW());

-- VF e34 - dịch vụ lẻ (giá riêng theo model)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, NULL, 1, 220000, NOW(), NOW()),
                                                                                                                         (1, NULL, 2, 165000, NOW(), NOW()),
                                                                                                                         (1, NULL, 3, 110000, NOW(), NOW()),
                                                                                                                         (1, NULL, 4, 130000, NOW(), NOW()),
                                                                                                                         (1, NULL, 5, 85000, NOW(), NOW()),
                                                                                                                         (1, NULL, 6, 160000, NOW(), NOW()),
                                                                                                                         (1, NULL, 7, 270000, NOW(), NOW()),
                                                                                                                         (1, NULL, 8, 320000, NOW(), NOW()),
                                                                                                                         (1, NULL, 9, 110000, NOW(), NOW()),
                                                                                                                         (1, NULL, 10, 195000, NOW(), NOW()),
                                                                                                                         (1, NULL, 11, 110000, NOW(), NOW()),
                                                                                                                         (1, NULL, 12, 165000, NOW(), NOW()),
                                                                                                                         (1, NULL, 13, 85000, NOW(), NOW()),
                                                                                                                         (1, NULL, 14, 130000, NOW(), NOW()),
                                                                                                                         (1, NULL, 15, 220000, NOW(), NOW());

-- ========================================
-- MODEL VF 8 (id = 2) - cao cấp hơn VF e34
-- ========================================

-- Gói cơ bản (600,000 VND) cho VF 8
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 1, 1, 150000, NOW(), NOW()),
                                                                                                                         (2, 1, 5, 80000, NOW(), NOW()),
                                                                                                                         (2, 1, 9, 110000, NOW(), NOW()),
                                                                                                                         (2, 1, 13, 90000, NOW(), NOW());

-- Gói tiêu chuẩn (1,200,000 VND) cho VF 8
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 2, 1, 140000, NOW(), NOW()),
                                                                                                                         (2, 2, 2, 170000, NOW(), NOW()),
                                                                                                                         (2, 2, 3, 110000, NOW(), NOW()),
                                                                                                                         (2, 2, 4, 130000, NOW(), NOW()),
                                                                                                                         (2, 2, 5, 70000, NOW(), NOW()),
                                                                                                                         (2, 2, 9, 100000, NOW(), NOW()),
                                                                                                                         (2, 2, 10, 200000, NOW(), NOW()),
                                                                                                                         (2, 2, 12, 140000, NOW(), NOW());

-- Gói cao cấp (2,000,000 VND) cho VF 8
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 3, 1, 130000, NOW(), NOW()),
                                                                                                                         (2, 3, 2, 150000, NOW(), NOW()),
                                                                                                                         (2, 3, 3, 100000, NOW(), NOW()),
                                                                                                                         (2, 3, 4, 120000, NOW(), NOW()),
                                                                                                                         (2, 3, 5, 70000, NOW(), NOW()),
                                                                                                                         (2, 3, 6, 160000, NOW(), NOW()),
                                                                                                                         (2, 3, 8, 320000, NOW(), NOW()),
                                                                                                                         (2, 3, 9, 90000, NOW(), NOW()),
                                                                                                                         (2, 3, 11, 110000, NOW(), NOW()),
                                                                                                                         (2, 3, 12, 150000, NOW(), NOW()),
                                                                                                                         (2, 3, 13, 80000, NOW(), NOW()),
                                                                                                                         (2, 3, 14, 150000, NOW(), NOW()),
                                                                                                                         (2, 3, 15, 240000, NOW(), NOW());

-- Gói pin chuyên sâu (1,500,000 VND) cho VF 8
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 4, 1, 220000, NOW(), NOW()),
                                                                                                                         (2, 4, 7, 260000, NOW(), NOW()),
                                                                                                                         (2, 4, 10, 180000, NOW(), NOW());

-- VF 8 - dịch vụ lẻ (giá cao hơn vì là model cao cấp hơn)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, NULL, 1, 250000, NOW(), NOW()),
                                                                                                                         (2, NULL, 2, 190000, NOW(), NOW()),
                                                                                                                         (2, NULL, 3, 130000, NOW(), NOW()),
                                                                                                                         (2, NULL, 4, 150000, NOW(), NOW()),
                                                                                                                         (2, NULL, 5, 100000, NOW(), NOW()),
                                                                                                                         (2, NULL, 6, 180000, NOW(), NOW()),
                                                                                                                         (2, NULL, 7, 300000, NOW(), NOW()),
                                                                                                                         (2, NULL, 8, 350000, NOW(), NOW()),
                                                                                                                         (2, NULL, 9, 130000, NOW(), NOW()),
                                                                                                                         (2, NULL, 10, 220000, NOW(), NOW()),
                                                                                                                         (2, NULL, 11, 130000, NOW(), NOW()),
                                                                                                                         (2, NULL, 12, 190000, NOW(), NOW()),
                                                                                                                         (2, NULL, 13, 100000, NOW(), NOW()),
                                                                                                                         (2, NULL, 14, 150000, NOW(), NOW()),
                                                                                                                         (2, NULL, 15, 250000, NOW(), NOW());

-- ========================================
-- MODEL VF 9 (id = 3) - cao cấp nhất
-- ========================================

-- Gói cơ bản (700,000 VND) cho VF 9
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 1, 1, 180000, NOW(), NOW()),
                                                                                                                         (3, 1, 5, 90000, NOW(), NOW()),
                                                                                                                         (3, 1, 9, 130000, NOW(), NOW()),
                                                                                                                         (3, 1, 13, 110000, NOW(), NOW());

-- Gói tiêu chuẩn (1,400,000 VND) cho VF 9
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 2, 1, 170000, NOW(), NOW()),
                                                                                                                         (3, 2, 2, 200000, NOW(), NOW()),
                                                                                                                         (3, 2, 3, 130000, NOW(), NOW()),
                                                                                                                         (3, 2, 4, 160000, NOW(), NOW()),
                                                                                                                         (3, 2, 5, 80000, NOW(), NOW()),
                                                                                                                         (3, 2, 9, 120000, NOW(), NOW()),
                                                                                                                         (3, 2, 10, 230000, NOW(), NOW()),
                                                                                                                         (3, 2, 12, 170000, NOW(), NOW());

-- Gói cao cấp (2,500,000 VND) cho VF 9
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 3, 1, 160000, NOW(), NOW()),
                                                                                                                         (3, 3, 2, 180000, NOW(), NOW()),
                                                                                                                         (3, 3, 3, 120000, NOW(), NOW()),
                                                                                                                         (3, 3, 4, 150000, NOW(), NOW()),
                                                                                                                         (3, 3, 5, 80000, NOW(), NOW()),
                                                                                                                         (3, 3, 6, 190000, NOW(), NOW()),
                                                                                                                         (3, 3, 8, 380000, NOW(), NOW()),
                                                                                                                         (3, 3, 9, 110000, NOW(), NOW()),
                                                                                                                         (3, 3, 11, 130000, NOW(), NOW()),
                                                                                                                         (3, 3, 12, 180000, NOW(), NOW()),
                                                                                                                         (3, 3, 13, 100000, NOW(), NOW()),
                                                                                                                         (3, 3, 14, 180000, NOW(), NOW()),
                                                                                                                         (3, 3, 15, 290000, NOW(), NOW());

-- Gói pin chuyên sâu (1,800,000 VND) cho VF 9
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 4, 1, 260000, NOW(), NOW()),
                                                                                                                         (3, 4, 7, 310000, NOW(), NOW()),
                                                                                                                         (3, 4, 10, 210000, NOW(), NOW());

-- VF 9 - dịch vụ lẻ (giá cao nhất vì là model cao cấp nhất)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, NULL, 1, 290000, NOW(), NOW()),
                                                                                                                         (3, NULL, 2, 220000, NOW(), NOW()),
                                                                                                                         (3, NULL, 3, 160000, NOW(), NOW()),
                                                                                                                         (3, NULL, 4, 180000, NOW(), NOW()),
                                                                                                                         (3, NULL, 5, 120000, NOW(), NOW()),
                                                                                                                         (3, NULL, 6, 220000, NOW(), NOW()),
                                                                                                                         (3, NULL, 7, 360000, NOW(), NOW()),
                                                                                                                         (3, NULL, 8, 420000, NOW(), NOW()),
                                                                                                                         (3, NULL, 9, 160000, NOW(), NOW()),
                                                                                                                         (3, NULL, 10, 260000, NOW(), NOW()),
                                                                                                                         (3, NULL, 11, 160000, NOW(), NOW()),
                                                                                                                         (3, NULL, 12, 220000, NOW(), NOW()),
                                                                                                                         (3, NULL, 13, 130000, NOW(), NOW()),
                                                                                                                         (3, NULL, 14, 180000, NOW(), NOW()),
                                                                                                                         (3, NULL, 15, 290000, NOW(), NOW());

-- ========================================
-- MODEL VF 5 (id = 4) - entry level
-- ========================================

-- Gói cơ bản (400,000 VND) cho VF 5
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 1, 1, 100000, NOW(), NOW()),
                                                                                                                         (4, 1, 5, 60000, NOW(), NOW()),
                                                                                                                         (4, 1, 9, 80000, NOW(), NOW()),
                                                                                                                         (4, 1, 13, 60000, NOW(), NOW());

-- Gói tiêu chuẩn (800,000 VND) cho VF 5
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 2, 1, 95000, NOW(), NOW()),
                                                                                                                         (4, 2, 2, 120000, NOW(), NOW()),
                                                                                                                         (4, 2, 3, 75000, NOW(), NOW()),
                                                                                                                         (4, 2, 4, 100000, NOW(), NOW()),
                                                                                                                         (4, 2, 5, 50000, NOW(), NOW()),
                                                                                                                         (4, 2, 9, 70000, NOW(), NOW()),
                                                                                                                         (4, 2, 10, 150000, NOW(), NOW());

-- Gói cao cấp (1,600,000 VND) cho VF 5
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 3, 1, 85000, NOW(), NOW()),
                                                                                                                         (4, 3, 2, 100000, NOW(), NOW()),
                                                                                                                         (4, 3, 3, 70000, NOW(), NOW()),
                                                                                                                         (4, 3, 4, 90000, NOW(), NOW()),
                                                                                                                         (4, 3, 5, 45000, NOW(), NOW()),
                                                                                                                         (4, 3, 6, 110000, NOW(), NOW()),
                                                                                                                         (4, 3, 8, 250000, NOW(), NOW()),
                                                                                                                         (4, 3, 9, 60000, NOW(), NOW()),
                                                                                                                         (4, 3, 11, 70000, NOW(), NOW()),
                                                                                                                         (4, 3, 12, 100000, NOW(), NOW()),
                                                                                                                         (4, 3, 13, 50000, NOW(), NOW()),
                                                                                                                         (4, 3, 14, 90000, NOW(), NOW()),
                                                                                                                         (4, 3, 15, 160000, NOW(), NOW());

-- Gói pin chuyên sâu (1,100,000 VND) cho VF 5
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 4, 1, 150000, NOW(), NOW()),
                                                                                                                         (4, 4, 7, 180000, NOW(), NOW()),
                                                                                                                         (4, 4, 10, 120000, NOW(), NOW());

-- VF 5 - dịch vụ lẻ (giá thấp nhất vì là entry level)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, NULL, 1, 200000, NOW(), NOW()),
                                                                                                                         (4, NULL, 2, 140000, NOW(), NOW()),
                                                                                                                         (4, NULL, 3, 90000, NOW(), NOW()),
                                                                                                                         (4, NULL, 4, 110000, NOW(), NOW()),
                                                                                                                         (4, NULL, 5, 70000, NOW(), NOW()),
                                                                                                                         (4, NULL, 6, 130000, NOW(), NOW()),
                                                                                                                         (4, NULL, 7, 230000, NOW(), NOW()),
                                                                                                                         (4, NULL, 8, 270000, NOW(), NOW()),
                                                                                                                         (4, NULL, 9, 90000, NOW(), NOW()),
                                                                                                                         (4, NULL, 10, 160000, NOW(), NOW()),
                                                                                                                         (4, NULL, 11, 90000, NOW(), NOW()),
                                                                                                                         (4, NULL, 12, 140000, NOW(), NOW()),
                                                                                                                         (4, NULL, 13, 70000, NOW(), NOW()),
                                                                                                                         (4, NULL, 14, 110000, NOW(), NOW()),
                                                                                                                         (4, NULL, 15, 200000, NOW(), NOW());

-- ========================================
-- MODEL VF 6 (id = 5) - mid-range
-- ========================================

-- Gói cơ bản (450,000 VND) cho VF 6
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 1, 1, 110000, NOW(), NOW()),
                                                                                                                         (5, 1, 5, 65000, NOW(), NOW()),
                                                                                                                         (5, 1, 9, 85000, NOW(), NOW()),
                                                                                                                         (5, 1, 13, 65000, NOW(), NOW());

-- Gói tiêu chuẩn (850,000 VND) cho VF 6
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 2, 1, 105000, NOW(), NOW()),
                                                                                                                         (5, 2, 2, 135000, NOW(), NOW()),
                                                                                                                         (5, 2, 3, 85000, NOW(), NOW()),
                                                                                                                         (5, 2, 4, 115000, NOW(), NOW()),
                                                                                                                         (5, 2, 5, 55000, NOW(), NOW()),
                                                                                                                         (5, 2, 9, 75000, NOW(), NOW()),
                                                                                                                         (5, 2, 10, 170000, NOW(), NOW());

-- Gói cao cấp (1,700,000 VND) cho VF 6
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 3, 1, 95000, NOW(), NOW()),
                                                                                                                         (5, 3, 2, 115000, NOW(), NOW()),
                                                                                                                         (5, 3, 3, 80000, NOW(), NOW()),
                                                                                                                         (5, 3, 4, 105000, NOW(), NOW()),
                                                                                                                         (5, 3, 5, 50000, NOW(), NOW()),
                                                                                                                         (5, 3, 6, 125000, NOW(), NOW()),
                                                                                                                         (5, 3, 8, 280000, NOW(), NOW()),
                                                                                                                         (5, 3, 9, 70000, NOW(), NOW()),
                                                                                                                         (5, 3, 11, 80000, NOW(), NOW()),
                                                                                                                         (5, 3, 12, 115000, NOW(), NOW()),
                                                                                                                         (5, 3, 13, 60000, NOW(), NOW()),
                                                                                                                         (5, 3, 14, 105000, NOW(), NOW()),
                                                                                                                         (5, 3, 15, 190000, NOW(), NOW());

-- Gói pin chuyên sâu (1,200,000 VND) cho VF 6
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 4, 1, 170000, NOW(), NOW()),
                                                                                                                         (5, 4, 7, 210000, NOW(), NOW()),
                                                                                                                         (5, 4, 10, 140000, NOW(), NOW());

-- VF 6 - dịch vụ lẻ (giá mid-range)
INSERT INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, NULL, 1, 230000, NOW(), NOW()),
                                                                                                                         (5, NULL, 2, 160000, NOW(), NOW()),
                                                                                                                         (5, NULL, 3, 110000, NOW(), NOW()),
                                                                                                                         (5, NULL, 4, 130000, NOW(), NOW()),
                                                                                                                         (5, NULL, 5, 80000, NOW(), NOW()),
                                                                                                                         (5, NULL, 6, 165000, NOW(), NOW()),
                                                                                                                         (5, NULL, 7, 280000, NOW(), NOW()),
                                                                                                                         (5, NULL, 8, 330000, NOW(), NOW()),
                                                                                                                         (5, NULL, 9, 120000, NOW(), NOW()),
                                                                                                                         (5, NULL, 10, 200000, NOW(), NOW()),
                                                                                                                         (5, NULL, 11, 120000, NOW(), NOW()),
                                                                                                                         (5, NULL, 12, 160000, NOW(), NOW()),
                                                                                                                         (5, NULL, 13, 85000, NOW(), NOW()),
                                                                                                                         (5, NULL, 14, 130000, NOW(), NOW()),
                                                                                                                         (5, NULL, 15, 230000, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;