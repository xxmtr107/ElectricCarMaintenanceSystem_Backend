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
-- Password for all: "baobao1234" → BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq
-- ========================================
INSERT IGNORE INTO users (username, email, password, phone, full_name, gender, role_id, specialization, experience_years, create_at, update_at) VALUES
-- CUSTOMER (role_id = 1)
('baobao1', 'baobao1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234001', N'Bảo Bảo', 'MALE', 1, NULL, NULL, NOW(), NOW()),
('triettriet1', 'triettriet1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234002', N'Triết Triết', 'FEMALE', 1, NULL, NULL, NOW(), NOW()),
('nguyennguyen1', 'nguyennguyen1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234003', N'Nguyên Nguyên', 'MALE', 1, NULL, NULL, NOW(), NOW()),
('thaothao1', 'thaothao1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234004', N'Thảo Thảo', 'FEMALE', 1, NULL, NULL, NOW(), NOW()),
-- TECHNICIAN (role_id = 2)
('baobao2', 'baobao2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234005', N'Bảo Bảo', 'MALE', 2, N'Hệ thống pin', 3, NOW(), NOW()),
('triettriet2', 'triettriet2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234006', N'Triết Triết', 'FEMALE', 2, N'Động cơ điện', 5, NOW(), NOW()),
('nguyennguyen2', 'nguyennguyen2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234007', N'Nguyên Nguyên', 'MALE', 2, N'Hệ thống phanh', 2, NOW(), NOW()),
('thaothao2', 'thaothao2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234008', N'Thảo Thảo', 'FEMALE', 2, N'Điện tử', 4, NOW(), NOW()),
-- STAFF (role_id = 3)
('baobao3', 'baobao3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234009', N'Bảo Bảo', 'MALE', 3, NULL, NULL, NOW(), NOW()),
('triettriet3', 'triettriet3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234010', N'Triết Triết', 'FEMALE', 3, NULL, NULL, NOW(), NOW()),
('nguyennguyen3', 'nguyennguyen3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234011', N'Nguyên Nguyên', 'MALE', 3, NULL, NULL, NOW(), NOW()),
('thaothao3', 'thaothao3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234012', N'Thảo Thảo', 'FEMALE', 3, NULL, NULL, NOW(), NOW()),
-- ADMIN (role_id = 4)
('baobao4', 'baobao4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234013', N'Bảo Bảo', 'MALE', 4, NULL, NULL, NOW(), NOW()),
('triettriet4', 'triettriet4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234014', N'Triết Triết', 'FEMALE', 4, NULL, NULL, NOW(), NOW()),
('nguyennguyen4', 'nguyennguyen4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234015', N'Nguyên Nguyên', 'MALE', 4, NULL, NULL, NOW(), NOW()),
('thaothao4', 'thaothao4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye5pYXKF9eRkCW9fUJdpQfE1mZ7nLXUGq', '0901234016', N'Thảo Thảo', 'FEMALE', 4, NULL, NULL, NOW(), NOW());

-- ========================================
-- 3. VEHICLE MODELS
-- ========================================
INSERT IGNORE INTO vehicle_models (name, model_year, basic_maintenance, comprehensive_maintenance, basic_maintenance_time, comprehensive_maintenance_time, create_at, update_at) VALUES
(N'VinFast VF e34', '2023', 10000, 20000, 6, 12, NOW(), NOW()),
(N'VinFast VF 8', '2023', 12000, 24000, 6, 12, NOW(), NOW()),
(N'VinFast VF 9', '2023', 15000, 30000, 6, 12, NOW(), NOW()),
(N'VinFast VF 5', '2023', 8000, 16000, 6, 12, NOW(), NOW()),
(N'VinFast VF 6', '2023', 9000, 18000, 6, 12, NOW(), NOW());

-- ========================================
-- 4. SERVICE PACKAGES
-- ========================================
INSERT IGNORE INTO service_packages (name, description, create_at, update_at) VALUES
('Basic Maintenance', 'Basic periodic maintenance package for electric vehicles', NOW(), NOW()),
('Standard Maintenance', 'Standard maintenance package for electric vehicles', NOW(), NOW()),
('Premium Maintenance', 'Comprehensive maintenance package for electric vehicles', NOW(), NOW()),
('Battery Deep Service', 'Deep maintenance package for battery system', NOW(), NOW());

-- ========================================
-- 5. SERVICE ITEMS
-- ========================================
INSERT IGNORE INTO service_items (name, description, price, create_at, update_at) VALUES
('Battery system inspection', 'Check overall battery condition', 200000, NOW(), NOW()),
('Electric motor inspection', 'Check motor performance', 150000, NOW(), NOW()),
('Brake system inspection', 'Check brake pads and brake fluid', 100000, NOW(), NOW()),
('Suspension system inspection', 'Check shock absorbers and springs', 120000, NOW(), NOW()),
('Tire inspection', 'Check tire pressure and wear', 80000, NOW(), NOW()),
('Brake fluid replacement', 'Replace brake fluid', 150000, NOW(), NOW()),
('Battery cooling system cleaning', 'Clean the battery cooling system', 250000, NOW(), NOW()),
('Software update', 'Update vehicle software', 300000, NOW(), NOW()),
('Electrical system inspection', 'Inspect electrical system', 100000, NOW(), NOW()),
('Charging system inspection', 'Check charging port', 180000, NOW(), NOW()),
('Cabin filter replacement', 'Replace cabin air filter', 100000, NOW(), NOW()),
('Air conditioning inspection', 'Inspect air conditioning system', 150000, NOW(), NOW()),
('Lighting system inspection', 'Check vehicle lights', 80000, NOW(), NOW()),
('Wheel balancing', 'Perform wheel balancing', 120000, NOW(), NOW()),
('Safety system inspection', 'Check airbags and sensors', 200000, NOW(), NOW());

-- ========================================
-- MODEL VF e34 (id = 1)
-- ========================================

-- Gói cơ bản (500,000 VND) cho VF e34
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 1, 1, 120000, NOW(), NOW()),
                                                                                                                         (1, 1, 5, 70000, NOW(), NOW()),
                                                                                                                         (1, 1, 9, 90000, NOW(), NOW()),
                                                                                                                         (1, 1, 13, 70000, NOW(), NOW());

-- Gói tiêu chuẩn (900,000 VND) cho VF e34
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 2, 1, 110000, NOW(), NOW()),
                                                                                                                         (1, 2, 2, 140000, NOW(), NOW()),
                                                                                                                         (1, 2, 3, 90000, NOW(), NOW()),
                                                                                                                         (1, 2, 5, 60000, NOW(), NOW()),
                                                                                                                         (1, 2, 9, 80000, NOW(), NOW()),
                                                                                                                         (1, 2, 10, 160000, NOW(), NOW());

-- Gói cao cấp (1,800,000 VND) cho VF e34
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (1, 4, 1, 180000, NOW(), NOW()),
                                                                                                                         (1, 4, 7, 220000, NOW(), NOW()),
                                                                                                                         (1, 4, 10, 150000, NOW(), NOW());

-- VF e34 - dịch vụ lẻ (giá riêng theo model)
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 1, 1, 150000, NOW(), NOW()),
                                                                                                                         (2, 1, 5, 80000, NOW(), NOW()),
                                                                                                                         (2, 1, 9, 110000, NOW(), NOW()),
                                                                                                                         (2, 1, 13, 90000, NOW(), NOW());

-- Gói tiêu chuẩn (1,200,000 VND) cho VF 8
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 2, 1, 140000, NOW(), NOW()),
                                                                                                                         (2, 2, 2, 170000, NOW(), NOW()),
                                                                                                                         (2, 2, 3, 110000, NOW(), NOW()),
                                                                                                                         (2, 2, 4, 130000, NOW(), NOW()),
                                                                                                                         (2, 2, 5, 70000, NOW(), NOW()),
                                                                                                                         (2, 2, 9, 100000, NOW(), NOW()),
                                                                                                                         (2, 2, 10, 200000, NOW(), NOW()),
                                                                                                                         (2, 2, 12, 140000, NOW(), NOW());

-- Gói cao cấp (2,000,000 VND) cho VF 8
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (2, 4, 1, 220000, NOW(), NOW()),
                                                                                                                         (2, 4, 7, 260000, NOW(), NOW()),
                                                                                                                         (2, 4, 10, 180000, NOW(), NOW());

-- VF 8 - dịch vụ lẻ (giá cao hơn vì là model cao cấp hơn)
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 1, 1, 180000, NOW(), NOW()),
                                                                                                                         (3, 1, 5, 90000, NOW(), NOW()),
                                                                                                                         (3, 1, 9, 130000, NOW(), NOW()),
                                                                                                                         (3, 1, 13, 110000, NOW(), NOW());

-- Gói tiêu chuẩn (1,400,000 VND) cho VF 9
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 2, 1, 170000, NOW(), NOW()),
                                                                                                                         (3, 2, 2, 200000, NOW(), NOW()),
                                                                                                                         (3, 2, 3, 130000, NOW(), NOW()),
                                                                                                                         (3, 2, 4, 160000, NOW(), NOW()),
                                                                                                                         (3, 2, 5, 80000, NOW(), NOW()),
                                                                                                                         (3, 2, 9, 120000, NOW(), NOW()),
                                                                                                                         (3, 2, 10, 230000, NOW(), NOW()),
                                                                                                                         (3, 2, 12, 170000, NOW(), NOW());

-- Gói cao cấp (2,500,000 VND) cho VF 9
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (3, 4, 1, 260000, NOW(), NOW()),
                                                                                                                         (3, 4, 7, 310000, NOW(), NOW()),
                                                                                                                         (3, 4, 10, 210000, NOW(), NOW());

-- VF 9 - dịch vụ lẻ (giá cao nhất vì là model cao cấp nhất)
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 1, 1, 100000, NOW(), NOW()),
                                                                                                                         (4, 1, 5, 60000, NOW(), NOW()),
                                                                                                                         (4, 1, 9, 80000, NOW(), NOW()),
                                                                                                                         (4, 1, 13, 60000, NOW(), NOW());

-- Gói tiêu chuẩn (800,000 VND) cho VF 5
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 2, 1, 95000, NOW(), NOW()),
                                                                                                                         (4, 2, 2, 120000, NOW(), NOW()),
                                                                                                                         (4, 2, 3, 75000, NOW(), NOW()),
                                                                                                                         (4, 2, 4, 100000, NOW(), NOW()),
                                                                                                                         (4, 2, 5, 50000, NOW(), NOW()),
                                                                                                                         (4, 2, 9, 70000, NOW(), NOW()),
                                                                                                                         (4, 2, 10, 150000, NOW(), NOW());

-- Gói cao cấp (1,600,000 VND) cho VF 5
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (4, 4, 1, 150000, NOW(), NOW()),
                                                                                                                         (4, 4, 7, 180000, NOW(), NOW()),
                                                                                                                         (4, 4, 10, 120000, NOW(), NOW());

-- VF 5 - dịch vụ lẻ (giá thấp nhất vì là entry level)
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 1, 1, 110000, NOW(), NOW()),
                                                                                                                         (5, 1, 5, 65000, NOW(), NOW()),
                                                                                                                         (5, 1, 9, 85000, NOW(), NOW()),
                                                                                                                         (5, 1, 13, 65000, NOW(), NOW());

-- Gói tiêu chuẩn (850,000 VND) cho VF 6
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 2, 1, 105000, NOW(), NOW()),
                                                                                                                         (5, 2, 2, 135000, NOW(), NOW()),
                                                                                                                         (5, 2, 3, 85000, NOW(), NOW()),
                                                                                                                         (5, 2, 4, 115000, NOW(), NOW()),
                                                                                                                         (5, 2, 5, 55000, NOW(), NOW()),
                                                                                                                         (5, 2, 9, 75000, NOW(), NOW()),
                                                                                                                         (5, 2, 10, 170000, NOW(), NOW());

-- Gói cao cấp (1,700,000 VND) cho VF 6
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
                                                                                                                         (5, 4, 1, 170000, NOW(), NOW()),
                                                                                                                         (5, 4, 7, 210000, NOW(), NOW()),
                                                                                                                         (5, 4, 10, 140000, NOW(), NOW());

-- VF 6 - dịch vụ lẻ (giá mid-range)
INSERT IGNORE INTO model_package_items (vehicle_model_id, service_package_id, service_item_id, price, create_at, update_at) VALUES
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

