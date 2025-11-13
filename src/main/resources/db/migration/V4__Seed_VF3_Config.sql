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

