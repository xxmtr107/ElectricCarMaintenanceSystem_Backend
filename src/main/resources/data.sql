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
('AC-VALVE-VFE34', N'A/C expansion valve VFe34', 1100000.00,   2, 1, 9, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
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
('WPR-BLD-VF9', N'Wiper blade set VF 9', 780000.00, 20, 5, 4, NOW(), 'SYSTEM', NOW(), 'SYSTEM'), -- Thêm (ước tính)

-- [THÊM MỚI]
-- === SPARE PARTS CHO HẠNG MỤC BỊ THIẾU (Ống phanh, Gỉ sét) - THEO TỪNG MODEL ===
('BRK-HOSE-VF3', N'Bộ ống dầu phanh VF 3', 500000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VF3', N'Hóa chất phủ gầm VF 3 (Lon)', 300000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-HOSE-VF5', N'Bộ ống dầu phanh VF 5', 550000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VF5', N'Hóa chất phủ gầm VF 5 (Lon)', 300000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-HOSE-VF6', N'Bộ ống dầu phanh VF 6', 570000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VF6', N'Hóa chất phủ gầm VF 6 (Lon)', 350000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-HOSE-VF7', N'Bộ ống dầu phanh VF 7', 580000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VF7', N'Hóa chất phủ gầm VF 7 (Lon)', 350000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-HOSE-VFE34', N'Bộ ống dầu phanh VFe34', 600000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VFE34', N'Hóa chất phủ gầm VFe34 (Lon)', 400000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-HOSE-VF8', N'Bộ ống dầu phanh VF 8', 610000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VF8', N'Hóa chất phủ gầm VF 8 (Lon)', 400000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('BRK-HOSE-VF9', N'Bộ ống dầu phanh VF 9', 620000.00, 20, 5, 6, NOW(), 'SYSTEM', NOW(), 'SYSTEM'),
('CHEM-RUST-VF9', N'Hóa chất phủ gầm VF 9 (Lon)', 400000.00, 30, 10, 2, NOW(), 'SYSTEM', NOW(), 'SYSTEM');
-- [BẮT ĐẦU PHẦN THAY THẾ TOÀN BỘ PHẦN 9]


-- =================================================================
-- 9. MODEL_PACKAGE_ITEMS (Dữ liệu CHUẨN - ĐÃ SỬA LỖI TRỪ KHO VÀ FIX NULLS VÀ QUANTITY)
-- =================================================================

-- ---------------------------------
-- --- MODEL: VFe34 (model_id=1) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=81, BRK=82, COOL=83, TBOX=84, KEY=3, AC=94, LỐP=97, MÁ PHANH=86, GẠT MƯA=100
-- --- IDs Mới: ỐNG PHANH=149, GỈ SÉT=150
-- ---------------------------------

-- 9.1.A: Dữ liệu VFe34 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (1, 12000, 1, 670000, 'REPLACE', 81, 1), (1, 12000, 2, 50000, 'CHECK', null, 0), (1, 12000, 3, 50000, 'CHECK', null, 0), (1, 12000, 4, 30000, 'CHECK', null, 0), (1, 12000, 5, 30000, 'CHECK', null, 0), (1, 12000, 6, 40000, 'CHECK', null, 0), (1, 12000, 7, 50000, 'CHECK', null, 0), (1, 12000, 8, 50000, 'CHECK', null, 0), (1, 12000, 9, 40000, 'CHECK', null, 0), (1, 12000, 10, 50000, 'CHECK', null, 0), (1, 12000, 11, 50000, 'CHECK', null, 0), (1, 12000, 12, 40000, 'CHECK', null, 0), (1, 12000, 13, 40000, 'CHECK', null, 0), (1, 12000, 14, 50000, 'CHECK', null, 0), (1, 12000, 15, 40000, 'CHECK', null, 0), (1, 12000, 16, 50000, 'CHECK', null, 0), (1, 12000, 17, 50000, 'CHECK', null, 0), (1, 12000, 18, 30000, 'CHECK', null, 0), (1, 12000, 19, 40000, 'CHECK', null, 0), (1, 12000, 20, 30000, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (1, 24000, 1, 670000, 'REPLACE', 81, 1), (1, 24000, 2, 630000, 'REPLACE', 82, 1), (1, 24000, 3, 50000, 'CHECK', null, 0), (1, 24000, 4, 30000, 'CHECK', null, 0), (1, 24000, 5, 30000, 'CHECK', null, 0), (1, 24000, 6, 40000, 'CHECK', null, 0), (1, 24000, 7, 50000, 'CHECK', null, 0), (1, 24000, 8, 50000, 'CHECK', null, 0), (1, 24000, 9, 40000, 'CHECK', null, 0), (1, 24000, 10, 50000, 'CHECK', null, 0), (1, 24000, 11, 50000, 'CHECK', null, 0), (1, 24000, 12, 40000, 'CHECK', null, 0), (1, 24000, 13, 40000, 'CHECK', null, 0), (1, 24000, 14, 50000, 'CHECK', null, 0), (1, 24000, 15, 40000, 'CHECK', null, 0), (1, 24000, 16, 50000, 'CHECK', null, 0), (1, 24000, 17, 50000, 'CHECK', null, 0), (1, 24000, 18, 30000, 'CHECK', null, 0), (1, 24000, 19, 40000, 'CHECK', null, 0), (1, 24000, 20, 30000, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 145000, action_type = 'REPLACE', included_spare_part_id = 3, included_quantity = 1 WHERE vehicle_model_id = 1 AND milestone_km = 48000 AND service_item_id = 4;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1030000, action_type = 'REPLACE', included_spare_part_id = 83, included_quantity = 1 WHERE vehicle_model_id = 1 AND milestone_km = 60000 AND service_item_id = 6;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1400000, action_type = 'REPLACE', included_spare_part_id = 94, included_quantity = 1 WHERE vehicle_model_id = 1 AND milestone_km = 72000 AND service_item_id = 3;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 48000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 1, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 1 AND milestone_km = 60000;
UPDATE model_package_items SET price = 630000, action_type = 'REPLACE', included_spare_part_id = 82, included_quantity = 1 WHERE vehicle_model_id = 1 AND milestone_km = 120000 AND service_item_id = 2;
UPDATE model_package_items SET price = 1100000, action_type = 'REPLACE', included_spare_part_id = 84, included_quantity = 1 WHERE vehicle_model_id = 1 AND milestone_km = 120000 AND service_item_id = 5;

-- 9.1.B: Dữ liệu VFe34 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (1, 1, 2, 630000, 'REPLACE', 82, 1),
                                                                                                                                                     (1, 1, 3, 1400000, 'REPLACE', 94, 1),
                                                                                                                                                     (1, 1, 4, 145000, 'REPLACE', 3, 1),
                                                                                                                                                     (1, 1, 5, 1100000, 'REPLACE', 84, 1),
                                                                                                                                                     (1, 1, 6, 1030000, 'REPLACE', 83, 1),
                                                                                                                                                     (1, 1, 7, 12000000, 'REPLACE', 97, 4), -- (FIXED) Lốp (ID 97) * 4
                                                                                                                                                     (1, 1, 8, 2860000, 'REPLACE', 86, 1), -- (FIXED) Má phanh (ID 86 - F) + (ID 87 - R) -> Dùng 1 set F
                                                                                                                                                     (1, 1, 9, 800000, 'REPLACE', 149, 1), -- (FIXED) Ống phanh -> ID 149
                                                                                                                                                     (1, 1, 10, 3500000, 'REPLACE', 92, 1),
                                                                                                                                                     (1, 1, 11, 2900000, 'REPLACE', 89, 1),
                                                                                                                                                     (1, 1, 12, 3500000, 'REPLACE', 92, 1),
                                                                                                                                                     (1, 1, 13, 1150000, 'REPLACE', 90, 1),
                                                                                                                                                     (1, 1, 14, 1150000, 'REPLACE', 91, 1),
                                                                                                                                                     (1, 1, 15, 800000, 'REPLACE', 93, 1),
                                                                                                                                                     (1, 1, 16, 2100000, 'REPLACE', 96, 1),
                                                                                                                                                     (1, 1, 17, 7800000, 'REPLACE', 95, 1),
                                                                                                                                                     (1, 1, 18, 7800000, 'REPLACE', 95, 1),
                                                                                                                                                     (1, 1, 19, 4900000, 'REPLACE', 85, 1),
                                                                                                                                                     (1, 1, 20, 700000, 'REPLACE', 100, 1);


-- ---------------------------------
-- --- MODEL: VF 3 (model_id=2) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=1, BRK=2, GẠT MƯA=20, AC=14, LỐP=17, MÁ PHANH F=6
-- --- IDs Mới: ỐNG PHANH=141, GỈ SÉT=142
-- ---------------------------------

-- 9.2.A: Dữ liệu VF 3 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (2, 12000, 1, 610000, 'REPLACE', 1, 1), (2, 12000, 20, 30000, 'CHECK', null, 0), (2, 12000, 3, 40000, 'CHECK', null, 0), (2, 12000, 2, 40000, 'CHECK', null, 0), (2, 12000, 7, 40000, 'CHECK', null, 0), (2, 12000, 8, 40000, 'CHECK', null, 0), (2, 12000, 9, 30000, 'CHECK', null, 0), (2, 12000, 10, 40000, 'CHECK', null, 0), (2, 12000, 11, 40000, 'CHECK', null, 0), (2, 12000, 12, 30000, 'CHECK', null, 0), (2, 12000, 13, 30000, 'CHECK', null, 0), (2, 12000, 14, 40000, 'CHECK', null, 0), (2, 12000, 16, 50000, 'CHECK', null, 0), (2, 12000, 17, 40000, 'CHECK', null, 0), (2, 12000, 18, 20000, 'CHECK', null, 0), (2, 12000, 19, 30000, 'CHECK', null, 0), (2, 12000, 21, 30000, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 24000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
UPDATE model_package_items SET price = 580000, action_type = 'REPLACE', included_spare_part_id = 2, included_quantity = 1 WHERE vehicle_model_id = 2 AND milestone_km = 24000 AND service_item_id = 2;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
UPDATE model_package_items SET price = 580000, action_type = 'REPLACE', included_spare_part_id = 20, included_quantity = 1 WHERE vehicle_model_id = 2 AND milestone_km = 48000 AND service_item_id = 20;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 48000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1240000, action_type = 'REPLACE', included_spare_part_id = 14, included_quantity = 1 WHERE vehicle_model_id = 2 AND milestone_km = 108000 AND service_item_id = 3;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 2, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 2 AND milestone_km = 24000;

-- 9.2.B: Dữ liệu VF 3 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (2, 1, 20, 580000, 'REPLACE', 20, 1),
                                                                                                                                                     (2, 1, 3, 1240000, 'REPLACE', 14, 1),
                                                                                                                                                     (2, 1, 2, 580000, 'REPLACE', 2, 1),
                                                                                                                                                     (2, 1, 7, 7600000, 'REPLACE', 17, 4), -- (FIXED) Lốp (ID 17) * 4
                                                                                                                                                     (2, 1, 8, 2110000, 'REPLACE', 6, 1), -- (FIXED) Má phanh (ID 6 - F) + (ID 7 - R) -> Dùng 1 set F
                                                                                                                                                     (2, 1, 9, 720000, 'REPLACE', 141, 1), -- (FIXED) Ống phanh -> ID 141
                                                                                                                                                     (2, 1, 10, 2950000, 'REPLACE', 12, 1),
                                                                                                                                                     (2, 1, 11, 2350000, 'REPLACE', 9, 1),
                                                                                                                                                     (2, 1, 12, 2950000, 'REPLACE', 12, 1),
                                                                                                                                                     (2, 1, 13, 980000, 'REPLACE', 10, 1),
                                                                                                                                                     (2, 1, 14, 1020000, 'REPLACE', 11, 1),
                                                                                                                                                     (2, 1, 16, 1950000, 'REPLACE', 16, 1),
                                                                                                                                                     (2, 1, 17, 7200000, 'REPLACE', 15, 1),
                                                                                                                                                     (2, 1, 18, 7200000, 'REPLACE', 15, 1),
                                                                                                                                                     (2, 1, 19, 4500000, 'REPLACE', 5, 1),
                                                                                                                                                     (2, 1, 21, 500000, 'REPLACE', 142, 1), -- (FIXED) Gỉ sét gầm -> ID 142
                                                                                                                                                     (2, 1, 22, 1050000, 'REPLACE', 18, 1);


-- ---------------------------------
-- --- MODEL: VF 5 (model_id=3) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=21, BRK=22, TBOX=24, COOL=23, GẠT MƯA=40, AC=34, LỐP=37, MÁ PHANH F=26
-- --- IDs Mới: ỐNG PHANH=143, GỈ SÉT=144
-- ---------------------------------

-- 9.3.A: Dữ liệu VF 5 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (3, 12000, 1, 630000, 'REPLACE', 21, 1), (3, 12000, 20, 25000, 'CHECK', null, 0), (3, 12000, 3, 45000, 'CHECK', null, 0), (3, 12000, 5, 25000, 'CHECK', null, 0), (3, 12000, 6, 35000, 'CHECK', null, 0), (3, 12000, 2, 45000, 'CHECK', null, 0), (3, 12000, 7, 45000, 'CHECK', null, 0), (3, 12000, 22, 45000, 'CHECK', null, 0), (3, 12000, 8, 45000, 'CHECK', null, 0), (3, 12000, 9, 35000, 'CHECK', null, 0), (3, 12000, 10, 45000, 'CHECK', null, 0), (3, 12000, 23, 45000, 'CHECK', null, 0), (3, 12000, 24, 35000, 'CHECK', null, 0), (3, 12000, 25, 35000, 'CHECK', null, 0), (3, 12000, 26, 45000, 'CHECK', null, 0), (3, 12000, 27, 35000, 'CHECK', null, 0), (3, 12000, 16, 50000, 'CHECK', null, 0), (3, 12000, 17, 45000, 'CHECK', null, 0), (3, 12000, 18, 25000, 'CHECK', null, 0), (3, 12000, 19, 35000, 'CHECK', null, 0), (3, 12000, 21, 35000, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 24000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
UPDATE model_package_items SET price = 600000, action_type = 'REPLACE', included_spare_part_id = 22, included_quantity = 1 WHERE vehicle_model_id = 3 AND milestone_km = 24000 AND service_item_id = 2;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1080000, action_type = 'REPLACE', included_spare_part_id = 24, included_quantity = 1 WHERE vehicle_model_id = 3 AND milestone_km = 72000 AND service_item_id = 5;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 3, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 3 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1030000, action_type = 'REPLACE', included_spare_part_id = 23, included_quantity = 1 WHERE vehicle_model_id = 3 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1270000, action_type = 'REPLACE', included_spare_part_id = 34, included_quantity = 1 WHERE vehicle_model_id = 3 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.3.B: Dữ liệu VF 5 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (3, 1, 20, 650000, 'REPLACE', 40, 1),
                                                                                                                                                     (3, 1, 3, 1270000, 'REPLACE', 34, 1),
                                                                                                                                                     (3, 1, 5, 1080000, 'REPLACE', 24, 1),
                                                                                                                                                     (3, 1, 6, 1030000, 'REPLACE', 23, 1),
                                                                                                                                                     (3, 1, 2, 600000, 'REPLACE', 22, 1),
                                                                                                                                                     (3, 1, 7, 8400000, 'REPLACE', 37, 4), -- (FIXED) Lốp (ID 37) * 4
                                                                                                                                                     (3, 1, 22, 1060000, 'REPLACE', 38, 1),
                                                                                                                                                     (3, 1, 8, 2180000, 'REPLACE', 26, 1), -- (FIXED) Má phanh (ID 26 - F) + (ID 27 - R) -> Dùng 1 set F
                                                                                                                                                     (3, 1, 9, 750000, 'REPLACE', 143, 1), -- (FIXED) Ống phanh -> ID 143
                                                                                                                                                     (3, 1, 10, 3080000, 'REPLACE', 32, 1),
                                                                                                                                                     (3, 1, 23, 2420000, 'REPLACE', 29, 1),
                                                                                                                                                     (3, 1, 24, 3080000, 'REPLACE', 32, 1),
                                                                                                                                                     (3, 1, 25, 1020000, 'REPLACE', 30, 1),
                                                                                                                                                     (3, 1, 26, 1060000, 'REPLACE', 31, 1),
                                                                                                                                                     (3, 1, 27, 750000, 'REPLACE', 33, 1),
                                                                                                                                                     (3, 1, 16, 2020000, 'REPLACE', 36, 1),
                                                                                                                                                     (3, 1, 17, 7400000, 'REPLACE', 35, 1),
                                                                                                                                                     (3, 1, 18, 7400000, 'REPLACE', 35, 1),
                                                                                                                                                     (3, 1, 19, 4500000, 'REPLACE', 25, 1),
                                                                                                                                                     (3, 1, 21, 500000, 'REPLACE', 144, 1); -- (FIXED) Gỉ sét gầm -> ID 144

-- ---------------------------------
-- --- MODEL: VF 6 (model_id=4) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=41, BRK=42, COOL=43, TBOX=44, GẠT MƯA=45, AC=55, LỐP=58, MÁ PHANH F=47
-- --- IDs Mới: ỐNG PHANH=145, GỈ SÉT=146
-- ---------------------------------

-- 9.4.A: Dữ liệu VF 6 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (4, 12000, 1, 674000, 'REPLACE', 41, 1), (4, 12000, 20, 27500, 'CHECK', null, 0), (4, 12000, 3, 49500, 'CHECK', null, 0), (4, 12000, 5, 27500, 'CHECK', null, 0), (4, 12000, 6, 38500, 'CHECK', null, 0), (4, 12000, 2, 49500, 'CHECK', null, 0), (4, 12000, 7, 49500, 'CHECK', null, 0), (4, 12000, 22, 49500, 'CHECK', null, 0), (4, 12000, 8, 49500, 'CHECK', null, 0), (4, 12000, 9, 38500, 'CHECK', null, 0), (4, 12000, 10, 49500, 'CHECK', null, 0), (4, 12000, 23, 49500, 'CHECK', null, 0), (4, 12000, 24, 38500, 'CHECK', null, 0), (4, 12000, 25, 38500, 'CHECK', null, 0), (4, 12000, 26, 49500, 'CHECK', null, 0), (4, 12000, 27, 38500, 'CHECK', null, 0), (4, 12000, 16, 55000, 'CHECK', null, 0), (4, 12000, 17, 49500, 'CHECK', null, 0), (4, 12000, 18, 27500, 'CHECK', null, 0), (4, 12000, 19, 38500, 'CHECK', null, 0), (4, 12000, 21, 38500, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 24000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
UPDATE model_package_items SET price = 622000, action_type = 'REPLACE', included_spare_part_id = 42, included_quantity = 1 WHERE vehicle_model_id = 4 AND milestone_km = 24000 AND service_item_id = 2;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 870000, action_type = 'REPLACE', included_spare_part_id = 45, included_quantity = 1 WHERE vehicle_model_id = 4 AND milestone_km = 48000 AND service_item_id = 20;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1120000, action_type = 'REPLACE', included_spare_part_id = 44, included_quantity = 1 WHERE vehicle_model_id = 4 AND milestone_km = 72000 AND service_item_id = 5;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 48000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 4, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 4 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1068000, action_type = 'REPLACE', included_spare_part_id = 43, included_quantity = 1 WHERE vehicle_model_id = 4 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1350000, action_type = 'REPLACE', included_spare_part_id = 55, included_quantity = 1 WHERE vehicle_model_id = 4 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.4.B: Dữ liệu VF 6 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (4, 1, 20, 870000, 'REPLACE', 45, 1),
                                                                                                                                                     (4, 1, 3, 1350000, 'REPLACE', 55, 1),
                                                                                                                                                     (4, 1, 5, 1080000, 'REPLACE', 44, 1),
                                                                                                                                                     (4, 1, 6, 1050000, 'REPLACE', 43, 1),
                                                                                                                                                     (4, 1, 2, 620000, 'REPLACE', 42, 1),
                                                                                                                                                     (4, 1, 7, 10000000, 'REPLACE', 58, 4), -- (FIXED) Lốp (ID 58) * 4
                                                                                                                                                     (4, 1, 22, 1060000, 'REPLACE', 59, 1),
                                                                                                                                                     (4, 1, 8, 2430000, 'REPLACE', 47, 1), -- (FIXED) Má phanh (ID 47 - F) + (ID 48 - R) -> Dùng 1 set F
                                                                                                                                                     (4, 1, 9, 770000, 'REPLACE', 145, 1), -- (FIXED) Ống phanh -> ID 145
                                                                                                                                                     (4, 1, 10, 3200000, 'REPLACE', 53, 1),
                                                                                                                                                     (4, 1, 23, 2600000, 'REPLACE', 50, 1),
                                                                                                                                                     (4, 1, 24, 3200000, 'REPLACE', 53, 1),
                                                                                                                                                     (4, 1, 25, 1050000, 'REPLACE', 51, 1),
                                                                                                                                                     (4, 1, 26, 1090000, 'REPLACE', 52, 1),
                                                                                                                                                     (4, 1, 27, 770000, 'REPLACE', 54, 1),
                                                                                                                                                     (4, 1, 16, 2050000, 'REPLACE', 57, 1),
                                                                                                                                                     (4, 1, 17, 7500000, 'REPLACE', 56, 1),
                                                                                                                                                     (4, 1, 18, 7500000, 'REPLACE', 56, 1),
                                                                                                                                                     (4, 1, 19, 4900000, 'REPLACE', 46, 1),
                                                                                                                                                     (4, 1, 21, 600000, 'REPLACE', 146, 1); -- (FIXED) Gỉ sét gầm -> ID 146

-- ---------------------------------
-- --- MODEL: VF 7 (model_id=5) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=61, BRK=62, COOL=63, TBOX=64, GẠT MƯA=65, AC=75, LỐP=78, MÁ PHANH F=67
-- --- IDs Mới: ỐNG PHANH=147, GỈ SÉT=148
-- ---------------------------------

-- 9.5.A: Dữ liệu VF 7 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (5, 12000, 1, 674000, 'REPLACE', 61, 1), (5, 12000, 20, 27500, 'CHECK', null, 0), (5, 12000, 3, 49500, 'CHECK', null, 0), (5, 12000, 5, 27500, 'CHECK', null, 0), (5, 12000, 6, 38500, 'CHECK', null, 0), (5, 12000, 2, 49500, 'CHECK', null, 0), (5, 12000, 7, 49500, 'CHECK', null, 0), (5, 12000, 22, 49500, 'CHECK', null, 0), (5, 12000, 8, 49500, 'CHECK', null, 0), (5, 12000, 9, 38500, 'CHECK', null, 0), (5, 12000, 10, 49500, 'CHECK', null, 0), (5, 12000, 11, 49500, 'CHECK', null, 0), (5, 12000, 12, 38500, 'CHECK', null, 0), (5, 12000, 13, 38500, 'CHECK', null, 0), (5, 12000, 14, 49500, 'CHECK', null, 0), (5, 12000, 27, 38500, 'CHECK', null, 0), (5, 12000, 16, 55000, 'CHECK', null, 0), (5, 12000, 17, 49500, 'CHECK', null, 0), (5, 12000, 18, 27500, 'CHECK', null, 0), (5, 12000, 19, 38500, 'CHECK', null, 0), (5, 12000, 21, 38500, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 24000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
UPDATE model_package_items SET price = 622000, action_type = 'REPLACE', included_spare_part_id = 62, included_quantity = 1 WHERE vehicle_model_id = 5 AND milestone_km = 24000 AND service_item_id = 2;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 880000, action_type = 'REPLACE', included_spare_part_id = 65, included_quantity = 1 WHERE vehicle_model_id = 5 AND milestone_km = 48000 AND service_item_id = 20;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1120000, action_type = 'REPLACE', included_spare_part_id = 64, included_quantity = 1 WHERE vehicle_model_id = 5 AND milestone_km = 72000 AND service_item_id = 5;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 48000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 5, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 5 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1068000, action_type = 'REPLACE', included_spare_part_id = 63, included_quantity = 1 WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1390000, action_type = 'REPLACE', included_spare_part_id = 75, included_quantity = 1 WHERE vehicle_model_id = 5 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.5.B: Dữ liệu VF 7 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (5, 1, 20, 880000, 'REPLACE', 65, 1),
                                                                                                                                                     (5, 1, 3, 1390000, 'REPLACE', 75, 1),
                                                                                                                                                     (5, 1, 5, 1120000, 'REPLACE', 64, 1),
                                                                                                                                                     (5, 1, 6, 1068000, 'REPLACE', 63, 1),
                                                                                                                                                     (5, 1, 2, 622000, 'REPLACE', 62, 1),
                                                                                                                                                     (5, 1, 7, 11200000, 'REPLACE', 78, 4), -- (FIXED) Lốp (ID 78) * 4
                                                                                                                                                     (5, 1, 22, 1060000, 'REPLACE', 79, 1),
                                                                                                                                                     (5, 1, 8, 2450000, 'REPLACE', 67, 1), -- (FIXED) Má phanh (ID 67 - F) + (ID 68 - R) -> Dùng 1 set F
                                                                                                                                                     (5, 1, 9, 780000, 'REPLACE', 147, 1), -- (FIXED) Ống phanh -> ID 147
                                                                                                                                                     (5, 1, 10, 3250000, 'REPLACE', 73, 1),
                                                                                                                                                     (5, 1, 11, 2650000, 'REPLACE', 70, 1),
                                                                                                                                                     (5, 1, 12, 3250000, 'REPLACE', 73, 1),
                                                                                                                                                     (5, 1, 13, 1060000, 'REPLACE', 71, 1),
                                                                                                                                                     (5, 1, 14, 1100000, 'REPLACE', 72, 1),
                                                                                                                                                     (5, 1, 27, 780000, 'REPLACE', 74, 1),
                                                                                                                                                     (5, 1, 16, 2060000, 'REPLACE', 77, 1),
                                                                                                                                                     (5, 1, 17, 7600000, 'REPLACE', 76, 1),
                                                                                                                                                     (5, 1, 18, 7600000, 'REPLACE', 76, 1),
                                                                                                                                                     (5, 1, 19, 4900000, 'REPLACE', 66, 1),
                                                                                                                                                     (5, 1, 21, 600000, 'REPLACE', 148, 1); -- (FIXED) Gỉ sét gầm -> ID 148

-- ---------------------------------
-- --- MODEL: VF 8 (model_id=6) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=101, BRK=102, COOL=103, TBOX=104, GẠT MƯA=120, AC=113, LỐP=117, MÁ PHANH F=106
-- --- IDs Mới: ỐNG PHANH=151, GỈ SÉT=152
-- ---------------------------------

-- 9.6.A: Dữ liệu VF 8 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (6, 12000, 1, 680000, 'REPLACE', 101, 1), (6, 12000, 2, 50000, 'CHECK', null, 0), (6, 12000, 3, 50000, 'CHECK', null, 0), (6, 12000, 5, 30000, 'CHECK', null, 0), (6, 12000, 6, 40000, 'CHECK', null, 0), (6, 12000, 7, 50000, 'CHECK', null, 0), (6, 12000, 22, 50000, 'CHECK', null, 0), (6, 12000, 8, 50000, 'CHECK', null, 0), (6, 12000, 9, 40000, 'CHECK', null, 0), (6, 12000, 10, 50000, 'CHECK', null, 0), (6, 12000, 23, 50000, 'CHECK', null, 0), (6, 12000, 24, 40000, 'CHECK', null, 0), (6, 12000, 25, 40000, 'CHECK', null, 0), (6, 12000, 26, 50000, 'CHECK', null, 0), (6, 12000, 20, 30000, 'CHECK', null, 0), (6, 12000, 27, 40000, 'CHECK', null, 0), (6, 12000, 16, 50000, 'CHECK', null, 0), (6, 12000, 17, 50000, 'CHECK', null, 0), (6, 12000, 18, 30000, 'CHECK', null, 0), (6, 12000, 19, 40000, 'CHECK', null, 0), (6, 12000, 21, 40000, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 24000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 630000, action_type = 'REPLACE', included_spare_part_id = 102, included_quantity = 1 WHERE vehicle_model_id = 6 AND milestone_km = 24000 AND service_item_id = 2;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 900000, action_type = 'REPLACE', included_spare_part_id = 120, included_quantity = 1 WHERE vehicle_model_id = 6 AND milestone_km = 48000 AND service_item_id = 20;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1110000, action_type = 'REPLACE', included_spare_part_id = 104, included_quantity = 1 WHERE vehicle_model_id = 6 AND milestone_km = 72000 AND service_item_id = 5;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 48000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 12000;
UPDATE model_package_items SET price = 1410000, action_type = 'REPLACE', included_spare_part_id = 113, included_quantity = 1 WHERE vehicle_model_id = 6 AND milestone_km = 108000 AND service_item_id = 3;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 6, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 6 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1040000, action_type = 'REPLACE', included_spare_part_id = 103, included_quantity = 1 WHERE vehicle_model_id = 6 AND milestone_km = 120000 AND service_item_id = 6;

-- 9.6.B: Dữ liệu VF 8 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (6, 1, 2, 630000, 'REPLACE', 102, 1),
                                                                                                                                                     (6, 1, 3, 1410000, 'REPLACE', 113, 1),
                                                                                                                                                     (6, 1, 5, 1110000, 'REPLACE', 104, 1),
                                                                                                                                                     (6, 1, 6, 1040000, 'REPLACE', 103, 1),
                                                                                                                                                     (6, 1, 7, 14600000, 'REPLACE', 117, 4), -- (FIXED) Lốp (ID 117) * 4
                                                                                                                                                     (6, 1, 22, 1060000, 'REPLACE', 118, 1),
                                                                                                                                                     (6, 1, 8, 2880000, 'REPLACE', 106, 1), -- (FIXED) Má phanh (ID 106 - F) + (ID 107 - R) -> Dùng 1 set F
                                                                                                                                                     (6, 1, 9, 810000, 'REPLACE', 151, 1), -- (FIXED) Ống phanh -> ID 151
                                                                                                                                                     (6, 1, 10, 3550000, 'REPLACE', 111, 1),
                                                                                                                                                     (6, 1, 23, 2950000, 'REPLACE', 108, 1),
                                                                                                                                                     (6, 1, 24, 3550000, 'REPLACE', 111, 1),
                                                                                                                                                     (6, 1, 25, 1110000, 'REPLACE', 109, 1),
                                                                                                                                                     (6, 1, 26, 1160000, 'REPLACE', 110, 1),
                                                                                                                                                     (6, 1, 20, 900000, 'REPLACE', 120, 1),
                                                                                                                                                     (6, 1, 27, 810000, 'REPLACE', 112, 1),
                                                                                                                                                     (6, 1, 16, 2110000, 'REPLACE', 115, 1),
                                                                                                                                                     (6, 1, 17, 7900000, 'REPLACE', 114, 1),
                                                                                                                                                     (6, 1, 18, 7900000, 'REPLACE', 114, 1),
                                                                                                                                                     (6, 1, 19, 4900000, 'REPLACE', 105, 1),
                                                                                                                                                     (6, 1, 21, 700000, 'REPLACE', 152, 1); -- (FIXED) Gỉ sét gầm -> ID 152

-- ---------------------------------
-- --- MODEL: VF 9 (model_id=7) ---
-- --- IDs Phụ tùng (từ Phần 8): FLT=121, BRK=122, COOL=123, TBOX=124, GẠT MƯA=140, AC=133, LỐP=137, MÁ PHANH F=126
-- --- IDs Mới: ỐNG PHANH=153, GỈ SÉT=154
-- ---------------------------------

-- 9.7.A: Dữ liệu VF 9 theo lịch trình
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (7, 12000, 1, 782500, 'REPLACE', 121, 1), (7, 12000, 2, 57500, 'CHECK', null, 0), (7, 12000, 3, 57500, 'CHECK', null, 0), (7, 12000, 5, 34500, 'CHECK', null, 0), (7, 12000, 6, 46000, 'CHECK', null, 0), (7, 12000, 7, 57500, 'CHECK', null, 0), (7, 12000, 22, 57500, 'CHECK', null, 0), (7, 12000, 8, 57500, 'CHECK', null, 0), (7, 12000, 9, 46000, 'CHECK', null, 0), (7, 12000, 10, 57500, 'CHECK', null, 0), (7, 12000, 23, 57500, 'CHECK', null, 0), (7, 12000, 24, 46000, 'CHECK', null, 0), (7, 12000, 25, 46000, 'CHECK', null, 0), (7, 12000, 26, 57500, 'CHECK', null, 0), (7, 12000, 20, 34500, 'CHECK', null, 0), (7, 12000, 27, 46000, 'CHECK', null, 0), (7, 12000, 16, 57500, 'CHECK', null, 0), (7, 12000, 17, 57500, 'CHECK', null, 0), (7, 12000, 18, 34500, 'CHECK', null, 0), (7, 12000, 19, 46000, 'CHECK', null, 0), (7, 12000, 21, 46000, 'CHECK', null, 0);
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 24000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
UPDATE model_package_items SET price = 667500, action_type = 'REPLACE', included_spare_part_id = 122, included_quantity = 1 WHERE vehicle_model_id = 7 AND milestone_km = 24000 AND service_item_id = 2;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 36000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 48000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 952500, action_type = 'REPLACE', included_spare_part_id = 140, included_quantity = 1 WHERE vehicle_model_id = 7 AND milestone_km = 48000 AND service_item_id = 20;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 60000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 72000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1180000, action_type = 'REPLACE', included_spare_part_id = 124, included_quantity = 1 WHERE vehicle_model_id = 7 AND milestone_km = 72000 AND service_item_id = 5;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 84000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 96000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 48000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 108000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 12000;
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) SELECT 7, 120000, service_item_id, price, action_type, included_spare_part_id, included_quantity FROM model_package_items WHERE vehicle_model_id = 7 AND milestone_km = 24000;
UPDATE model_package_items SET price = 1102500, action_type = 'REPLACE', included_spare_part_id = 123, included_quantity = 1 WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 6;
UPDATE model_package_items SET price = 1514500, action_type = 'REPLACE', included_spare_part_id = 133, included_quantity = 1 WHERE vehicle_model_id = 7 AND milestone_km = 120000 AND service_item_id = 3;

-- 9.7.B: Dữ liệu VF 9 "Nâng cấp" (Mốc 1km)
INSERT INTO model_package_items (vehicle_model_id, milestone_km, service_item_id, price, action_type, included_spare_part_id, included_quantity) VALUES
                                                                                                                                                     (7, 1, 2, 667500, 'REPLACE', 122, 1),
                                                                                                                                                     (7, 1, 3, 1514500, 'REPLACE', 133, 1),
                                                                                                                                                     (7, 1, 5, 1180000, 'REPLACE', 124, 1),
                                                                                                                                                     (7, 1, 6, 1102500, 'REPLACE', 123, 1),
                                                                                                                                                     (7, 1, 7, 17000000, 'REPLACE', 137, 4), -- (FIXED) Lốp (ID 137) * 4
                                                                                                                                                     (7, 1, 22, 1060000, 'REPLACE', 138, 1),
                                                                                                                                                     (7, 1, 8, 2900000, 'REPLACE', 126, 1), -- (FIXED) Má phanh (ID 126 - F) + (ID 127 - R) -> Dùng 1 set F
                                                                                                                                                     (7, 1, 9, 820000, 'REPLACE', 153, 1), -- (FIXED) Ống phanh -> ID 153
                                                                                                                                                     (7, 1, 10, 3600000, 'REPLACE', 131, 1),
                                                                                                                                                     (7, 1, 23, 3000000, 'REPLACE', 128, 1),
                                                                                                                                                     (7, 1, 24, 3600000, 'REPLACE', 131, 1),
                                                                                                                                                     (7, 1, 25, 1120000, 'REPLACE', 129, 1),
                                                                                                                                                     (7, 1, 26, 1170000, 'REPLACE', 130, 1),
                                                                                                                                                     (7, 1, 20, 952500, 'REPLACE', 140, 1),
                                                                                                                                                     (7, 1, 27, 820000, 'REPLACE', 132, 1),
                                                                                                                                                     (7, 1, 16, 2120000, 'REPLACE', 135, 1),
                                                                                                                                                     (7, 1, 17, 8000000, 'REPLACE', 134, 1),
                                                                                                                                                     (7, 1, 18, 8000000, 'REPLACE', 134, 1),
                                                                                                                                                     (7, 1, 19, 4900000, 'REPLACE', 125, 1),
                                                                                                                                                     (7, 1, 21, 700000, 'REPLACE', 154, 1); -- (FIXED) Gỉ sét gầm -> ID 154

-- [KẾT THÚC PHẦN THAY THẾ]                                                                                                                                             (7, 1, 21, 700000, 'REPLACE', null, 0); -- (Giá Gỉ sét gầm - Ước tính 700k) - KO CÓ PART