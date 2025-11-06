-- ========================================
-- 1. ROLES (Không thay đổi)
-- ========================================
INSERT IGNORE INTO roles (name, create_at, created_by) VALUES
                                                           ('CUSTOMER', NOW(), 'SYSTEM'),
                                                           ('TECHNICIAN', NOW(), 'SYSTEM'),
                                                           ('STAFF', NOW(), 'SYSTEM'),
                                                           ('ADMIN', NOW(), 'SYSTEM');

-- ========================================
-- 2. USERS (Cập nhật: Bỏ username, SĐT là unique)
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
-- 3. SERVICE_PACKAGES (Các mốc bảo dưỡng - Không thay đổi)
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
-- 4. SERVICE_ITEMS (Cập nhật: Dựa trên Master List 27 hạng mục)
-- =================================================================
INSERT INTO service_items (id, name, description) VALUES
                                                      (1, N'Lọc gió điều hòa', N'Thay thế hoặc vệ sinh lọc gió cabin'),
                                                      (2, N'Dầu phanh', N'Kiểm tra hoặc thay thế dầu phanh'),
                                                      (3, N'Hệ thống điều hòa', N'Kiểm tra, vệ sinh, bảo dưỡng hệ thống điều hòa'),
                                                      (4, N'Pin chìa khóa điều khiển', N'Kiểm tra hoặc thay pin chìa khóa (VFe34)'),
                                                      (5, N'Pin bộ T-Box', N'Kiểm tra hoặc thay pin T-Box'),
                                                      (6, N'Nước làm mát cho Pin/động cơ điện', N'Kiểm tra hoặc thay/bổ sung nước làm mát'),
                                                      (7, N'Lốp (áp suất, độ mòn, đảo và cân bằng lốp)', N'Kiểm tra lốp, đảo lốp, cân bằng động'),
                                                      (8, N'Má phanh và đĩa phanh', N'Kiểm tra độ mòn má phanh, đĩa phanh'),
                                                      (9, N'Đường ống, đầu nối hệ thống phanh', N'Kiểm tra rò rỉ, nứt vỡ đường ống phanh'),
                                                      (10, N'Bộ dẫn động (động cơ điện và hộp số)', N'Kiểm tra tình trạng vận hành'),
                                                      (11, N'Hệ thống treo', N'Kiểm tra giảm xóc, lò xo, liên kết'),
                                                      (12, N'Trục truyền động', N'Kiểm tra trục truyền động, khớp vạn năng'),
                                                      (13, N'Khớp cầu', N'Kiểm tra khớp cầu, rotuyn'),
                                                      (14, N'Thước lái và khớp nối cầu', N'Kiểm tra hệ thống lái'),
                                                      (15, N'Đường ống làm mát', N'Kiểm tra rò rỉ, nứt vỡ đường ống (VFe34)'),
                                                      (16, N'Pin / Pin điện áp cao', N'Kiểm tra tình trạng pin, quạt làm mát pin'),
                                                      (17, N'Dây cáp của hệ thống điện áp cao', N'Kiểm tra dây cáp điện cao áp'),
                                                      (18, N'Cổng sạc', N'Kiểm tra, vệ sinh cổng sạc'),
                                                      (19, N'Ắc quy 12V / Tình trạng ắc quy 12V', N'Kiểm tra tình trạng ắc quy 12V'),
                                                      (20, N'Gạt nước mưa / Nước rửa kính', N'Kiểm tra và bổ sung nước rửa kính, thay gạt mưa nếu cần'),
                                                      (21, N'Kiểm tra gỉ sét / ăn mòn dưới gầm', N'Kiểm tra tổng thể gầm xe'),
                                                      (22, N'Vành (hư hỏng, biến dạng và các vết nứt)', N'Kiểm tra vành xe'),
                                                      (23, N'Cầu xe và hệ thống treo', N'Kiểm tra cầu xe, hệ thống treo'),
                                                      (24, N'Trục dẫn động và cao su chống bụi', N'Kiểm tra trục và cao su chắn bụi'),
                                                      (25, N'Khớp cầu hệ thống treo', N'Kiểm tra khớp cầu hệ thống treo'),
                                                      (26, N'Cơ cấu lái và khớp cầu', N'Kiểm tra cơ cấu lái'),
                                                      (27, N'Kiểm tra ống nước làm mát', N'Kiểm tra ống nước làm mát (VF5-9)');

-- =================================================================
-- 5. VEHICLE_MODELS (Không thay đổi)
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
-- 6. SERVICE CENTER (Không thay đổi)
-- =================================================================
INSERT IGNORE INTO service_centers (name, address, district, city, phone)
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
-- 7. PART-CATEGORIES (Không thay đổi)
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
-- 8. SPARE-PARTS (Không thay đổi)
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
('WPR-BLD-VF5', N'Wiper blade set VF 5', 500000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Thêm (ước tính)

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
('WPR-BLD-VFE34', N'Wiper blade set VFe34', 550000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Thêm (ước tính)

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
('WPR-BLD-VF8', N'Wiper blade set VF 8', 750000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Thêm (ước tính)

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
('WPR-PUMP-VF9', N'Washer fluid pump VF 9', 540000.00, 5, 2, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('WPR-BLD-VF9', N'Wiper blade set VF 9', 780000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'); -- Thêm (ước tính)

-- =================================================================
-- 9. MODEL_PACKAGE_ITEMS (Dữ liệu CHUẨN)
-- 9.x.A: Dữ liệu theo lịch trình chuẩn (Dùng khi Đặt lịch)
-- 9.x.B: Dữ liệu giá nâng cấp REPLACE (Dùng khi Tech Upgrade, đặt ở mốc 1km)
-- =================================================================

-- ---------------------------------
-- --- MODEL: VFe34 (model_id=1) ---
-- --- 20 Hạng mục (Items: 1-20) ---
-- ---------------------------------

-- 9.1.A: Dữ liệu VFe34 theo lịch trình (từ ảnh image_bf8e3c.jpg)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (1, 12000, 1, 670000, 'REPLACE'), (1, 12000, 2, 50000, 'CHECK'), (1, 12000, 3, 50000, 'CHECK'), (1, 12000, 4, 30000, 'CHECK'), (1, 12000, 5, 30000, 'CHECK'), (1, 12000, 6, 40000, 'CHECK'), (1, 12000, 7, 50000, 'CHECK'), (1, 12000, 8, 50000, 'CHECK'), (1, 12000, 9, 40000, 'CHECK'), (1, 12000, 10, 50000, 'CHECK'), (1, 12000, 11, 50000, 'CHECK'), (1, 12000, 12, 40000, 'CHECK'), (1, 12000, 13, 40000, 'CHECK'), (1, 12000, 14, 50000, 'CHECK'), (1, 12000, 15, 40000, 'CHECK'), (1, 12000, 16, 50000, 'CHECK'), (1, 12000, 17, 50000, 'CHECK'), (1, 12000, 18, 30000, 'CHECK'), (1, 12000, 19, 40000, 'CHECK'), (1, 12000, 20, 30000, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (1, 24000, 1, 670000, 'REPLACE'), (1, 24000, 2, 630000, 'REPLACE'), (1, 24000, 3, 50000, 'CHECK'), (1, 24000, 4, 30000, 'CHECK'), (1, 24000, 5, 30000, 'CHECK'), (1, 24000, 6, 40000, 'CHECK'), (1, 24000, 7, 50000, 'CHECK'), (1, 24000, 8, 50000, 'CHECK'), (1, 24000, 9, 40000, 'CHECK'), (1, 24000, 10, 50000, 'CHECK'), (1, 24000, 11, 50000, 'CHECK'), (1, 24000, 12, 40000, 'CHECK'), (1, 24000, 13, 40000, 'CHECK'), (1, 24000, 14, 50000, 'CHECK'), (1, 24000, 15, 40000, 'CHECK'), (1, 24000, 16, 50000, 'CHECK'), (1, 24000, 17, 50000, 'CHECK'), (1, 24000, 18, 30000, 'CHECK'), (1, 24000, 19, 40000, 'CHECK'), (1, 24000, 20, 30000, 'CHECK');
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 145000, action_type = 'REPLACE' WHERE vehicle_model_id = 1 AND milestone_km = 48000 AND service_item_id = 4;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1030000, action_type = 'REPLACE' WHERE vehicle_model_id = 1 AND milestone_km = 60000 AND service_item_id = 6;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1400000, action_type = 'REPLACE' WHERE vehicle_model_id = 1 AND milestone_km = 72000 AND service_item_id = 3;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 48000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 1, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 60000; -- Dựa trên mốc 60k (có thay nước làm mát)
UPDATE model_package_items SET price = 630000, action_type = 'REPLACE' WHERE vehicle_model_id = 1 AND milestone_km = 120000 AND service_item_id = 2; -- Thêm thay dầu phanh
UPDATE model_package_items SET price = 1100000, action_type = 'REPLACE' WHERE vehicle_model_id = 1 AND milestone_km = 120000 AND service_item_id = 5; -- Thêm thay pin T-Box

-- 9.1.B: Dữ liệu VFe34 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (1, 1, 2, 630000, 'REPLACE'),    -- (Giá Dầu phanh từ mốc 24k)
                                                                                                          (1, 1, 3, 1400000, 'REPLACE'),   -- (Giá HĐH từ mốc 72k)
                                                                                                          (1, 1, 4, 145000, 'REPLACE'),    -- (Giá Pin chìa khóa từ mốc 48k)
                                                                                                          (1, 1, 5, 1100000, 'REPLACE'),   -- (Giá Pin T-Box từ mốc 120k)
                                                                                                          (1, 1, 6, 1030000, 'REPLACE'),   -- (Giá Nước làm mát từ mốc 60k)
                                                                                                          (1, 1, 7, 3150000, 'REPLACE'),   -- (Giá Lốp WHL-TIRE-VFE34 2950k + Công 200k)
                                                                                                          (1, 1, 8, 1580000, 'REPLACE'),   -- (Giá Má phanh BRK-PAD-F-VFE34 1380k + Công 200k)
                                                                                                          (1, 1, 9, 800000, 'REPLACE'),    -- (Giá Ống phanh - Ước tính 600k + Công 200k)
                                                                                                          (1, 1, 10, 3500000, 'REPLACE'),  -- (Giá Bộ dẫn động DRV-AXLE-VFE34 3000k + Công 500k)
                                                                                                          (1, 1, 11, 2900000, 'REPLACE'),  -- (Giá Hệ thống treo SUS-SHOCK-F-VFE34 2500k + Công 400k)
                                                                                                          (1, 1, 12, 3500000, 'REPLACE'),  -- (Giá Trục truyền động DRV-AXLE-VFE34 3000k + Công 500k)
                                                                                                          (1, 1, 13, 1150000, 'REPLACE'),  -- (Giá Khớp cầu SUS-BALLJ-VFE34 900k + Công 250k)
                                                                                                          (1, 1, 14, 1150000, 'REPLACE'),  -- (Giá Thước lái SUS-TIEROD-VFE34 850k + Công 300k)
                                                                                                          (1, 1, 15, 800000, 'REPLACE'),   -- (Giá Ống làm mát COOL-HOSE-VFE34 600k + Công 200k)
                                                                                                          (1, 1, 16, 2100000, 'REPLACE'),  -- (Giá Pin HV-FUSE-VFE34 1600k + Công 500k)
                                                                                                          (1, 1, 17, 7800000, 'REPLACE'),  -- (Giá Dây cáp HV-CABLE-VFE34 6800k + Công 1000k)
                                                                                                          (1, 1, 18, 7800000, 'REPLACE'),  -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (1, 1, 19, 4900000, 'REPLACE'),  -- (Giá Ắc quy BAT-12V-VFE34 4700k + Công 200k)
                                                                                                          (1, 1, 20, 700000, 'REPLACE');   -- (Giá Gạt mưa WPR-BLD-VFE34 550k + Công 150k)


-- ---------------------------------
-- --- MODEL: VF 3 (model_id=2) ---
-- --- 17 Hạng mục (Items: 1, 2, 3, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 21) ---
-- ---------------------------------

-- 9.2.A: Dữ liệu VF 3 theo lịch trình (từ ảnh image_bf8e44.jpg)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (2, 12000, 1, 610000, 'REPLACE'), (2, 12000, 20, 30000, 'CHECK'), (2, 12000, 3, 40000, 'CHECK'), (2, 12000, 2, 40000, 'CHECK'), (2, 12000, 7, 40000, 'CHECK'), (2, 12000, 8, 40000, 'CHECK'), (2, 12000, 9, 30000, 'CHECK'), (2, 12000, 10, 40000, 'CHECK'), (2, 12000, 11, 40000, 'CHECK'), (2, 12000, 12, 30000, 'CHECK'), (2, 12000, 13, 30000, 'CHECK'), (2, 12000, 14, 40000, 'CHECK'), (2, 12000, 16, 50000, 'CHECK'), (2, 12000, 17, 40000, 'CHECK'), (2, 12000, 18, 20000, 'CHECK'), (2, 12000, 19, 30000, 'CHECK'), (2, 12000, 21, 30000, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 24000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
UPDATE model_package_items SET price = 580000, action_type = 'REPLACE' WHERE vehicle_model_id = 2 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
UPDATE model_package_items SET price = 580000, action_type = 'REPLACE' WHERE vehicle_model_id = 2 AND milestone_km = 48000 AND service_item_id = 20;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 48000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1240000, action_type = 'REPLACE' WHERE vehicle_model_id = 2 AND milestone_km = 108000 AND service_item_id = 3;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 2, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;

-- 9.2.B: Dữ liệu VF 3 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (2, 1, 20, 580000, 'REPLACE'), -- (Giá Gạt mưa WPR-BLD-VF3 480k + Công 100k)
                                                                                                          (2, 1, 3, 1240000, 'REPLACE'), -- (Giá HĐH AC-VALVE-VF3 990k + Công 250k)
                                                                                                          (2, 1, 2, 580000, 'REPLACE'), -- (Giá Dầu phanh FLD-BRK-VF3 380k + Công 200k)
                                                                                                          (2, 1, 7, 2050000, 'REPLACE'), -- (Giá Lốp WHL-TIRE-VF3 1850k + Công 200k)
                                                                                                          (2, 1, 8, 1190000, 'REPLACE'), -- (Giá Má phanh BRK-PAD-F-VF3 990k + Công 200k)
                                                                                                          (2, 1, 9, 720000, 'REPLACE'), -- (Giá Ống phanh - Ước tính 520k + Công 200k)
                                                                                                          (2, 1, 10, 2950000, 'REPLACE'), -- (Giá Bộ dẫn động DRV-AXLE-VF3 2450k + Công 500k)
                                                                                                          (2, 1, 11, 2350000, 'REPLACE'), -- (Giá HT treo SUS-SHOCK-F-VF3 1950k + Công 400k)
                                                                                                          (2, 1, 12, 2950000, 'REPLACE'), -- (Giá Trục C/S DRV-AXLE-VF3 2450k + Công 500k)
                                                                                                          (2, 1, 13, 980000, 'REPLACE'), -- (Giá Khớp cầu SUS-BALLJ-VF3 780k + Công 200k)
                                                                                                          (2, 1, 14, 1020000, 'REPLACE'), -- (Giá Thước lái SUS-TIEROD-VF3 720k + Công 300k)
                                                                                                          (2, 1, 16, 1950000, 'REPLACE'), -- (Giá Pin HV-FUSE-VF3 1450k + Công 500k)
                                                                                                          (2, 1, 17, 7200000, 'REPLACE'), -- (Giá Dây cáp HV-CABLE-VF3 6200k + Công 1000k)
                                                                                                          (2, 1, 18, 7200000, 'REPLACE'), -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (2, 1, 19, 4500000, 'REPLACE'), -- (Giá Ắc quy BAT-12V-VF3 4300k + Công 200k)
                                                                                                          (2, 1, 21, 500000, 'REPLACE'), -- (Giá Gỉ sét gầm - Ước tính 500k)
                                                                                                          (2, 1, 22, 1050000, 'REPLACE'); -- (Giá Vành WHL-TPMS-VF3 850k + Công 200k)


-- ---------------------------------
-- --- MODEL: VF 5 (model_id=3) ---
-- --- 21 Hạng mục (Items: 1, 2, 3, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27) ---
-- ---------------------------------

-- 9.3.A: Dữ liệu VF 5 theo lịch trình (từ ảnh image_bf8e62.png)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (3, 12000, 1, 630000, 'REPLACE'), (3, 12000, 20, 25000, 'CHECK'), (3, 12000, 3, 45000, 'CHECK'), (3, 12000, 5, 25000, 'CHECK'), (3, 12000, 6, 35000, 'CHECK'), (3, 12000, 2, 45000, 'CHECK'), (3, 12000, 7, 45000, 'CHECK'), (3, 12000, 22, 45000, 'CHECK'), (3, 12000, 8, 45000, 'CHECK'), (3, 12000, 9, 35000, 'CHECK'), (3, 12000, 10, 45000, 'CHECK'), (3, 12000, 23, 45000, 'CHECK'), (3, 12000, 24, 35000, 'CHECK'), (3, 12000, 25, 35000, 'CHECK'), (3, 12000, 26, 45000, 'CHECK'), (3, 12000, 27, 35000, 'CHECK'), (3, 12000, 16, 50000, 'CHECK'), (3, 12000, 17, 45000, 'CHECK'), (3, 12000, 18, 25000, 'CHECK'), (3, 12000, 19, 35000, 'CHECK'), (3, 12000, 21, 35000, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 24000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
UPDATE model_package_items SET price = 600000, action_type = 'REPLACE' WHERE vehicle_model_id = 3 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1080000, action_type = 'REPLACE' WHERE vehicle_model_id = 3 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 3, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1030000, action_type = 'REPLACE' WHERE vehicle_model_id = 3 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1270000, action_type = 'REPLACE' WHERE vehicle_model_id = 3 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.3.B: Dữ liệu VF 5 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (3, 1, 20, 650000, 'REPLACE'), -- (Giá Gạt mưa WPR-BLD-VF5 500k + Công 150k)
                                                                                                          (3, 1, 3, 1270000, 'REPLACE'), -- (Giá HĐH AC-VALVE-VF5 1020k + Công 250k)
                                                                                                          (3, 1, 5, 1080000, 'REPLACE'), -- (Giá Pin T-Box BAT-TBOX-VF5 680k + Công 400k)
                                                                                                          (3, 1, 6, 1030000, 'REPLACE'), -- (Giá Nước làm mát FLD-COOL-VF5 650k + Công 380k)
                                                                                                          (3, 1, 2, 600000, 'REPLACE'), -- (Giá Dầu phanh FLD-BRK-VF5 380k + Công 220k)
                                                                                                          (3, 1, 7, 2250000, 'REPLACE'), -- (Giá Lốp WHL-TIRE-VF5 2050k + Công 200k)
                                                                                                          (3, 1, 22, 1060000, 'REPLACE'), -- (Giá Vành WHL-TPMS-VF5 860k + Công 200k)
                                                                                                          (3, 1, 8, 1220000, 'REPLACE'), -- (Giá Má phanh BRK-PAD-F-VF5 1020k + Công 200k)
                                                                                                          (3, 1, 9, 750000, 'REPLACE'), -- (Giá Ống phanh - Ước tính 550k + Công 200k)
                                                                                                          (3, 1, 10, 3080000, 'REPLACE'), -- (Giá Bộ dẫn động DRV-AXLE-VF5 2580k + Công 500k)
                                                                                                          (3, 1, 23, 2420000, 'REPLACE'), -- (Giá Cầu xe/HT treo SUS-SHOCK-F-VF5 2020k + Công 400k)
                                                                                                          (3, 1, 24, 3080000, 'REPLACE'), -- (Giá Trục C/S DRV-AXLE-VF5 2580k + Công 500k)
                                                                                                          (3, 1, 25, 1020000, 'REPLACE'), -- (Giá Khớp cầu SUS-BALLJ-VF5 820k + Công 200k)
                                                                                                          (3, 1, 26, 1060000, 'REPLACE'), -- (Giá Cơ cấu lái SUS-TIEROD-VF5 760k + Công 300k)
                                                                                                          (3, 1, 27, 750000, 'REPLACE'), -- (Giá Ống làm mát COOL-HOSE-VF5 550k + Công 200k)
                                                                                                          (3, 1, 16, 2020000, 'REPLACE'), -- (Giá Pin HV-FUSE-VF5 1520k + Công 500k)
                                                                                                          (3, 1, 17, 7400000, 'REPLACE'), -- (Giá Dây cáp HV-CABLE-VF5 6400k + Công 1000k)
                                                                                                          (3, 1, 18, 7400000, 'REPLACE'), -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (3, 1, 19, 4500000, 'REPLACE'), -- (Giá Ắc quy BAT-12V-VF5 4300k + Công 200k)
                                                                                                          (3, 1, 21, 500000, 'REPLACE'); -- (Giá Gỉ sét gầm - Ước tính 500k)

-- ---------------------------------
-- --- MODEL: VF 6 (model_id=4) ---
-- --- 21 Hạng mục (Items: 1, 2, 3, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27) ---
-- ---------------------------------

-- 9.4.A: Dữ liệu VF 6 theo lịch trình (từ ảnh image_bf8e7e.png)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (4, 12000, 1, 674000, 'REPLACE'), (4, 12000, 20, 27500, 'CHECK'), (4, 12000, 3, 49500, 'CHECK'), (4, 12000, 5, 27500, 'CHECK'), (4, 12000, 6, 38500, 'CHECK'), (4, 12000, 2, 49500, 'CHECK'), (4, 12000, 7, 49500, 'CHECK'), (4, 12000, 22, 49500, 'CHECK'), (4, 12000, 8, 49500, 'CHECK'), (4, 12000, 9, 38500, 'CHECK'), (4, 12000, 10, 49500, 'CHECK'), (4, 12000, 23, 49500, 'CHECK'), (4, 12000, 24, 38500, 'CHECK'), (4, 12000, 25, 38500, 'CHECK'), (4, 12000, 26, 49500, 'CHECK'), (4, 12000, 27, 38500, 'CHECK'), (4, 12000, 16, 55000, 'CHECK'), (4, 12000, 17, 49500, 'CHECK'), (4, 12000, 18, 27500, 'CHECK'), (4, 12000, 19, 38500, 'CHECK'), (4, 12000, 21, 38500, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 24000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
UPDATE model_package_items SET price = 622000, action_type = 'REPLACE' WHERE vehicle_model_id = 4 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 870000, action_type = 'REPLACE' WHERE vehicle_model_id = 4 AND milestone_km = 48000 AND service_item_id = 20;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1120000, action_type = 'REPLACE' WHERE vehicle_model_id = 4 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 48000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 4, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1068000, action_type = 'REPLACE' WHERE vehicle_model_id = 4 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1350000, action_type = 'REPLACE' WHERE vehicle_model_id = 4 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.4.B: Dữ liệu VF 6 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (4, 1, 20, 870000, 'REPLACE'), -- (Giá Gạt mưa WPR-BLD-VF6 720k + Công 150k)
                                                                                                          (4, 1, 3, 1350000, 'REPLACE'), -- (Giá HĐH AC-VALVE-VF6 1050k + Công 300k)
                                                                                                          (4, 1, 5, 1080000, 'REPLACE'), -- (Giá Pin T-Box BAT-TBOX-VF6 680k + Công 400k)
                                                                                                          (4, 1, 6, 1050000, 'REPLACE'), -- (Giá Nước làm mát FLD-COOL-VF6 650k + Công 400k)
                                                                                                          (4, 1, 2, 620000, 'REPLACE'), -- (Giá Dầu phanh FLD-BRK-VF6 380k + Công 240k)
                                                                                                          (4, 1, 7, 2650000, 'REPLACE'), -- (Giá Lốp WHL-TIRE-VF6 2450k + Công 200k)
                                                                                                          (4, 1, 22, 1060000, 'REPLACE'), -- (Giá Vành WHL-TPMS-VF6 860k + Công 200k)
                                                                                                          (4, 1, 8, 1350000, 'REPLACE'), -- (Giá Má phanh BRK-PAD-F-VF6 1150k + Công 200k)
                                                                                                          (4, 1, 9, 770000, 'REPLACE'), -- (Giá Ống phanh - Ước tính 570k + Công 200k)
                                                                                                          (4, 1, 10, 3200000, 'REPLACE'), -- (Giá Bộ dẫn động DRV-AXLE-VF6 2700k + Công 500k)
                                                                                                          (4, 1, 23, 2600000, 'REPLACE'), -- (Giá Cầu xe/HT treo SUS-SHOCK-F-VF6 2200k + Công 400k)
                                                                                                          (4, 1, 24, 3200000, 'REPLACE'), -- (Giá Trục C/S DRV-AXLE-VF6 2700k + Công 500k)
                                                                                                          (4, 1, 25, 1050000, 'REPLACE'), -- (Giá Khớp cầu SUS-BALLJ-VF6 850k + Công 200k)
                                                                                                          (4, 1, 26, 1090000, 'REPLACE'), -- (Giá Cơ cấu lái SUS-TIEROD-VF6 790k + Công 300k)
                                                                                                          (4, 1, 27, 770000, 'REPLACE'), -- (Giá Ống làm mát COOL-HOSE-VF6 570k + Công 200k)
                                                                                                          (4, 1, 16, 2050000, 'REPLACE'), -- (Giá Pin HV-FUSE-VF6 1550k + Công 500k)
                                                                                                          (4, 1, 17, 7500000, 'REPLACE'), -- (Giá Dây cáp HV-CABLE-VF6 6500k + Công 1000k)
                                                                                                          (4, 1, 18, 7500000, 'REPLACE'), -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (4, 1, 19, 4900000, 'REPLACE'), -- (Giá Ắc quy BAT-12V-VF6 4700k + Công 200k)
                                                                                                          (4, 1, 21, 600000, 'REPLACE'); -- (Giá Gỉ sét gầm - Ước tính 600k)

-- ---------------------------------
-- --- MODEL: VF 7 (model_id=5) ---
-- --- 21 Hạng mục (Items: 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 21, 22, 27) ---
-- ---------------------------------

-- 9.5.A: Dữ liệu VF 7 theo lịch trình (từ ảnh image_bf8e99.png)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (5, 12000, 1, 674000, 'REPLACE'), (5, 12000, 20, 27500, 'CHECK'), (5, 12000, 3, 49500, 'CHECK'), (5, 12000, 5, 27500, 'CHECK'), (5, 12000, 6, 38500, 'CHECK'), (5, 12000, 2, 49500, 'CHECK'), (5, 12000, 7, 49500, 'CHECK'), (5, 12000, 22, 49500, 'CHECK'), (5, 12000, 8, 49500, 'CHECK'), (5, 12000, 9, 38500, 'CHECK'), (5, 12000, 10, 49500, 'CHECK'), (5, 12000, 11, 49500, 'CHECK'), (5, 12000, 12, 38500, 'CHECK'), (5, 12000, 13, 38500, 'CHECK'), (5, 12000, 14, 49500, 'CHECK'), (5, 12000, 27, 38500, 'CHECK'), (5, 12000, 16, 55000, 'CHECK'), (5, 12000, 17, 49500, 'CHECK'), (5, 12000, 18, 27500, 'CHECK'), (5, 12000, 19, 38500, 'CHECK'), (5, 12000, 21, 38500, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 24000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
UPDATE model_package_items SET price = 622000, action_type = 'REPLACE' WHERE vehicle_model_id = 5 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 880000, action_type = 'REPLACE' WHERE vehicle_model_id = 5 AND milestone_km = 48000 AND service_item_id = 20;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1120000, action_type = 'REPLACE' WHERE vehicle_model_id = 5 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 48000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 5, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1068000, action_type = 'REPLACE' WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1390000, action_type = 'REPLACE' WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.5.B: Dữ liệu VF 7 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (5, 1, 20, 880000, 'REPLACE'), -- (Giá Gạt mưa WPR-BLD-VF7 730k + Công 150k)
                                                                                                          (5, 1, 3, 1390000, 'REPLACE'), -- (Giá HĐH AC-VALVE-VF7 1060k + Công 330k)
                                                                                                          (5, 1, 5, 1120000, 'REPLACE'), -- (Giá Pin T-Box BAT-TBOX-VF7 680k + Công 440k)
                                                                                                          (5, 1, 6, 1068000, 'REPLACE'), -- (Giá Nước làm mát FLD-COOL-VF7 650k + Công 418k)
                                                                                                          (5, 1, 2, 622000, 'REPLACE'), -- (Giá Dầu phanh FLD-BRK-VF7 380k + Công 242k)
                                                                                                          (5, 1, 7, 2950000, 'REPLACE'), -- (Giá Lốp WHL-TIRE-VF7 2750k + Công 200k)
                                                                                                          (5, 1, 22, 1060000, 'REPLACE'), -- (Giá Vành WHL-TPMS-VF7 860k + Công 200k)
                                                                                                          (5, 1, 8, 1360000, 'REPLACE'), -- (Giá Má phanh BRK-PAD-F-VF7 1160k + Công 200k)
                                                                                                          (5, 1, 9, 780000, 'REPLACE'), -- (Giá Ống phanh - Ước tính 580k + Công 200k)
                                                                                                          (5, 1, 10, 3250000, 'REPLACE'), -- (Giá Bộ dẫn động DRV-AXLE-VF7 2750k + Công 500k)
                                                                                                          (5, 1, 11, 2650000, 'REPLACE'), -- (Giá HT treo SUS-SHOCK-F-VF7 2250k + Công 400k)
                                                                                                          (5, 1, 12, 3250000, 'REPLACE'), -- (Giá Trục C/S DRV-AXLE-VF7 2750k + Công 500k)
                                                                                                          (5, 1, 13, 1060000, 'REPLACE'), -- (Giá Khớp cầu SUS-BALLJ-VF7 860k + Công 200k)
                                                                                                          (5, 1, 14, 1100000, 'REPLACE'), -- (Giá Thước lái SUS-TIEROD-VF7 800k + Công 300k)
                                                                                                          (5, 1, 27, 780000, 'REPLACE'), -- (Giá Ống làm mát COOL-HOSE-VF7 580k + Công 200k)
                                                                                                          (5, 1, 16, 2060000, 'REPLACE'), -- (Giá Pin HV-FUSE-VF7 1560k + Công 500k)
                                                                                                          (5, 1, 17, 7600000, 'REPLACE'), -- (Giá Dây cáp HV-CABLE-VF7 6600k + Công 1000k)
                                                                                                          (5, 1, 18, 7600000, 'REPLACE'), -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (5, 1, 19, 4900000, 'REPLACE'), -- (Giá Ắc quy BAT-12V-VF7 4700k + Công 200k)
                                                                                                          (5, 1, 21, 600000, 'REPLACE'); -- (Giá Gỉ sét gầm - Ước tính 600k)

-- ---------------------------------
-- --- MODEL: VF 8 (model_id=6) ---
-- --- 21 Hạng mục (Items: 1, 2, 3, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27) ---
-- ---------------------------------

-- 9.6.A: Dữ liệu VF 8 theo lịch trình (từ ảnh image_bf8e9f.png)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (6, 12000, 1, 680000, 'REPLACE'), (6, 12000, 2, 50000, 'CHECK'), (6, 12000, 3, 50000, 'CHECK'), (6, 12000, 5, 30000, 'CHECK'), (6, 12000, 6, 40000, 'CHECK'), (6, 12000, 7, 50000, 'CHECK'), (6, 12000, 22, 50000, 'CHECK'), (6, 12000, 8, 50000, 'CHECK'), (6, 12000, 9, 40000, 'CHECK'), (6, 12000, 10, 50000, 'CHECK'), (6, 12000, 23, 50000, 'CHECK'), (6, 12000, 24, 40000, 'CHECK'), (6, 12000, 25, 40000, 'CHECK'), (6, 12000, 26, 50000, 'CHECK'), (6, 12000, 20, 30000, 'CHECK'), (6, 12000, 27, 40000, 'CHECK'), (6, 12000, 16, 50000, 'CHECK'), (6, 12000, 17, 50000, 'CHECK'), (6, 12000, 18, 30000, 'CHECK'), (6, 12000, 19, 40000, 'CHECK'), (6, 12000, 21, 40000, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 24000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 630000, action_type = 'REPLACE' WHERE vehicle_model_id = 6 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 900000, action_type = 'REPLACE' WHERE vehicle_model_id = 6 AND milestone_km = 48000 AND service_item_id = 20;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1110000, action_type = 'REPLACE' WHERE vehicle_model_id = 6 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 48000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1410000, action_type = 'REPLACE' WHERE vehicle_model_id = 6 AND milestone_km = 108000 AND service_item_id = 3;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 6, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1040000, action_type = 'REPLACE' WHERE vehicle_model_id = 6 AND milestone_km = 120000 AND service_item_id = 6;

-- 9.6.B: Dữ liệu VF 8 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (6, 1, 2, 630000, 'REPLACE'), -- (Giá REPLACE của mốc 24k)
                                                                                                          (6, 1, 3, 1410000, 'REPLACE'), -- (Giá REPLACE của mốc 108k)
                                                                                                          (6, 1, 5, 1110000, 'REPLACE'), -- (Giá REPLACE của mốc 72k)
                                                                                                          (6, 1, 6, 1040000, 'REPLACE'), -- (Giá REPLACE của mốc 120k)
                                                                                                          (6, 1, 7, 3800000, 'REPLACE'), -- (Giá Lốp WHL-TIRE-VF8 3600k + Công 200k)
                                                                                                          (6, 1, 22, 1060000, 'REPLACE'), -- (Giá Vành WHL-TPMS-VF8 860k + Công 200k)
                                                                                                          (6, 1, 8, 1590000, 'REPLACE'), -- (Giá Má phanh BRK-PAD-F-VF8 1390k + Công 200k)
                                                                                                          (6, 1, 9, 810000, 'REPLACE'), -- (Giá Ống phanh - Ước tính 610k + Công 200k)
                                                                                                          (6, 1, 10, 3550000, 'REPLACE'), -- (Giá Bộ dẫn động DRV-AXLE-VF8 3050k + Công 500k)
                                                                                                          (6, 1, 23, 2950000, 'REPLACE'), -- (Giá Cầu xe/HT treo SUS-SHOCK-F-VF8 2550k + Công 400k)
                                                                                                          (6, 1, 24, 3550000, 'REPLACE'), -- (Giá Trục C/S DRV-AXLE-VF8 3050k + Công 500k)
                                                                                                          (6, 1, 25, 1110000, 'REPLACE'), -- (Giá Khớp cầu SUS-BALLJ-VF8 910k + Công 200k)
                                                                                                          (6, 1, 26, 1160000, 'REPLACE'), -- (Giá Cơ cấu lái SUS-TIEROD-VF8 860k + Công 300k)
                                                                                                          (6, 1, 20, 900000, 'REPLACE'), -- (Giá Gạt mưa WPR-BLD-VF8 750k + Công 150k)
                                                                                                          (6, 1, 27, 810000, 'REPLACE'), -- (Giá Ống làm mát COOL-HOSE-VF8 610k + Công 200k)
                                                                                                          (6, 1, 16, 2110000, 'REPLACE'), -- (Giá Pin HV-FUSE-VF8 1610k + Công 500k)
                                                                                                          (6, 1, 17, 7900000, 'REPLACE'), -- (Giá Dây cáp HV-CABLE-VF8 6900k + Công 1000k)
                                                                                                          (6, 1, 18, 7900000, 'REPLACE'), -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (6, 1, 19, 4900000, 'REPLACE'), -- (Giá Ắc quy BAT-12V-VF8 4700k + Công 200k)
                                                                                                          (6, 1, 21, 700000, 'REPLACE'); -- (Giá Gỉ sét gầm - Ước tính 700k)

-- ---------------------------------
-- --- MODEL: VF 9 (model_id=7) ---
-- --- 21 Hạng mục (Items: 1, 2, 3, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27) ---
-- ---------------------------------

-- 9.7.A: Dữ liệu VF 9 theo lịch trình (từ ảnh image_bf8ec0.jpg)
-- Mốc 12000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (7, 12000, 1, 782500, 'REPLACE'), (7, 12000, 2, 57500, 'CHECK'), (7, 12000, 3, 57500, 'CHECK'), (7, 12000, 5, 34500, 'CHECK'), (7, 12000, 6, 46000, 'CHECK'), (7, 12000, 7, 57500, 'CHECK'), (7, 12000, 22, 57500, 'CHECK'), (7, 12000, 8, 57500, 'CHECK'), (7, 12000, 9, 46000, 'CHECK'), (7, 12000, 10, 57500, 'CHECK'), (7, 12000, 23, 57500, 'CHECK'), (7, 12000, 24, 46000, 'CHECK'), (7, 12000, 25, 46000, 'CHECK'), (7, 12000, 26, 57500, 'CHECK'), (7, 12000, 20, 34500, 'CHECK'), (7, 12000, 27, 46000, 'CHECK'), (7, 12000, 16, 57500, 'CHECK'), (7, 12000, 17, 57500, 'CHECK'), (7, 12000, 18, 34500, 'CHECK'), (7, 12000, 19, 46000, 'CHECK'), (7, 12000, 21, 46000, 'CHECK');
-- Mốc 24000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 24000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
UPDATE model_package_items SET price = 667500, action_type = 'REPLACE' WHERE vehicle_model_id = 7 AND milestone_km = 24000 AND service_item_id = 2;
-- Mốc 36000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 36000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 48000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 48000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 952500, action_type = 'REPLACE' WHERE vehicle_model_id = 7 AND milestone_km = 48000 AND service_item_id = 20;
-- Mốc 60000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 60000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 72000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 72000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1180000, action_type = 'REPLACE' WHERE vehicle_model_id = 7 AND milestone_km = 72000 AND service_item_id = 5;
-- Mốc 84000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 84000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 96000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 96000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 48000;
-- Mốc 108000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 108000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
-- Mốc 120000km
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) SELECT 7, 120000, service_item_id, price, action_type FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1102500, action_type = 'REPLACE' WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1514500, action_type = 'REPLACE' WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.7.B: Dữ liệu VF 9 "Nâng cấp" (Đặt tại Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type) VALUES
                                                                                                          (7, 1, 2, 667500, 'REPLACE'), -- (Giá REPLACE của mốc 24k)
                                                                                                          (7, 1, 3, 1514500, 'REPLACE'), -- (Giá REPLACE của mốc 120k)
                                                                                                          (7, 1, 5, 1180000, 'REPLACE'), -- (Giá REPLACE của mốc 72k)
                                                                                                          (7, 1, 6, 1102500, 'REPLACE'), -- (Giá REPLACE của mốc 120k)
                                                                                                          (7, 1, 7, 4400000, 'REPLACE'), -- (Giá Lốp WHL-TIRE-VF9 4200k + Công 200k)
                                                                                                          (7, 1, 22, 1060000, 'REPLACE'), -- (Giá Vành WHL-TPMS-VF9 860k + Công 200k)
                                                                                                          (7, 1, 8, 1600000, 'REPLACE'), -- (Giá Má phanh BRK-PAD-F-VF9 1400k + Công 200k)
                                                                                                          (7, 1, 9, 820000, 'REPLACE'), -- (Giá Ống phanh - Ước tính 620k + Công 200k)
                                                                                                          (7, 1, 10, 3600000, 'REPLACE'), -- (Giá Bộ dẫn động DRV-AXLE-VF9 3100k + Công 500k)
                                                                                                          (7, 1, 23, 3000000, 'REPLACE'), -- (Giá Cầu xe/HT treo SUS-SHOCK-F-VF9 2600k + Công 400k)
                                                                                                          (7, 1, 24, 3600000, 'REPLACE'), -- (Giá Trục C/S DRV-AXLE-VF9 3100k + Công 500k)
                                                                                                          (7, 1, 25, 1120000, 'REPLACE'), -- (Giá Khớp cầu SUS-BALLJ-VF9 920k + Công 200k)
                                                                                                          (7, 1, 26, 1170000, 'REPLACE'), -- (Giá Cơ cấu lái SUS-TIEROD-VF9 870k + Công 300k)
                                                                                                          (7, 1, 20, 952500, 'REPLACE'), -- (Giá Gạt mưa WPR-BLD-VF9 780k + Công 172.5k)
                                                                                                          (7, 1, 27, 820000, 'REPLACE'), -- (Giá Ống làm mát COOL-HOSE-VF9 620k + Công 200k)
                                                                                                          (7, 1, 16, 2120000, 'REPLACE'), -- (Giá Pin HV-FUSE-VF9 1620k + Công 500k)
                                                                                                          (7, 1, 17, 8000000, 'REPLACE'), -- (Giá Dây cáp HV-CABLE-VF9 7000k + Công 1000k)
                                                                                                          (7, 1, 18, 8000000, 'REPLACE'), -- (Giá Cổng sạc - Dùng giá Dây cáp)
                                                                                                          (7, 1, 19, 4900000, 'REPLACE'), -- (Giá Ắc quy BAT-12V-VF9 4700k + Công 200k)
                                                                                                          (7, 1, 21, 700000, 'REPLACE'); -- (Giá Gỉ sét gầm - Ước tính 700k)