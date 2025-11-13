-- V1__Create_Initial_Schema.sql
-- Initial database schema for Vehicle Maintenance System

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;

-- =============================================
-- Table: service_centers
-- =============================================
CREATE TABLE IF NOT EXISTS `service_centers` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                                 `name` varchar(255) DEFAULT NULL,
    `address` varchar(255) DEFAULT NULL,
    `city` varchar(100) DEFAULT NULL,
    `district` varchar(100) DEFAULT NULL,
    `phone` varchar(255) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: users
-- =============================================
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `password` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `full_name` varchar(255) NOT NULL,
    `phone` varchar(255) NOT NULL,
    `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
    `role` enum('ADMIN','CUSTOMER','STAFF','TECHNICIAN') DEFAULT NULL,
    `role_id` bigint NOT NULL,
    `center_id` bigint DEFAULT NULL,
    `last_login` datetime(6) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_username` (`username`),
    UNIQUE KEY `UK_email` (`email`),
    KEY `FKnwuxsl4ux127j20jj0yakgp8g` (`center_id`),
    CONSTRAINT `FKnwuxsl4ux127j20jj0yakgp8g` FOREIGN KEY (`center_id`) REFERENCES `service_centers` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: vehicle_models
-- =============================================
CREATE TABLE IF NOT EXISTS `vehicle_models` (
                                                `id` bigint NOT NULL AUTO_INCREMENT,
                                                `name` varchar(255) NOT NULL,
    `model_year` varchar(255) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKp5maaq9pv6glmk4nebv0mu6vi` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: vehicles
-- =============================================
CREATE TABLE IF NOT EXISTS `vehicles` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `license_plate` varchar(255) NOT NULL,
    `vin` varchar(255) DEFAULT NULL,
    `current_km` int DEFAULT NULL,
    `purchase_year` date DEFAULT NULL,
    `customer_id` bigint NOT NULL,
    `model_id` bigint NOT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK9vovnbiegxevdhqfcwvp2g8pj` (`license_plate`),
    KEY `FKgqjc8pmwiyjslyyf2dnkpdgcs` (`customer_id`),
    KEY `FKe4n3vwcrfkcfpyp8g0jsqlr4d` (`model_id`),
    CONSTRAINT `FKe4n3vwcrfkcfpyp8g0jsqlr4d` FOREIGN KEY (`model_id`) REFERENCES `vehicle_models` (`id`),
    CONSTRAINT `FKgqjc8pmwiyjslyyf2dnkpdgcs` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: service_items
-- =============================================
CREATE TABLE IF NOT EXISTS `service_items` (
                                               `id` bigint NOT NULL AUTO_INCREMENT,
                                               `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
    `description` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK2sjc2unhcd6wet5ce6ebgrkfv` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: spare_parts
-- =============================================
CREATE TABLE IF NOT EXISTS `spare_parts` (
                                             `id` bigint NOT NULL AUTO_INCREMENT,
                                             `part_number` varchar(255) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    `category_id` bigint NOT NULL,
    `category_code` varchar(255) DEFAULT NULL,
    `category_name` varchar(255) DEFAULT NULL,
    `unit_price` decimal(10,2) NOT NULL,
    `quantity_in_stock` int NOT NULL,
    `minimum_stock_level` int NOT NULL DEFAULT 10,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKse9rxewv9r3wcqefv7jvbp19w` (`part_number`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: model_package_items
-- =============================================
CREATE TABLE IF NOT EXISTS `model_package_items` (
                                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                                     `vehicle_model_id` bigint NOT NULL,
                                                     `service_item_id` bigint DEFAULT NULL,
                                                     `milestone_km` int NOT NULL,
                                                     `action_type` enum('CHECK','REPLACE') NOT NULL,
    `included_spare_part_id` bigint DEFAULT NULL,
    `included_quantity` int DEFAULT 1,
    `price` decimal(38,2) NOT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKrgbj58oaqqpo4k9r2s9rve6lp` (`vehicle_model_id`),
    KEY `FKmwgnb62y5hw61q76rsiefdmvj` (`service_item_id`),
    KEY `FKppp3ucljewxfc9gdck83jdr12` (`included_spare_part_id`),
    CONSTRAINT `FKmwgnb62y5hw61q76rsiefdmvj` FOREIGN KEY (`service_item_id`) REFERENCES `service_items` (`id`),
    CONSTRAINT `FKppp3ucljewxfc9gdck83jdr12` FOREIGN KEY (`included_spare_part_id`) REFERENCES `spare_parts` (`id`),
    CONSTRAINT `FKrgbj58oaqqpo4k9r2s9rve6lp` FOREIGN KEY (`vehicle_model_id`) REFERENCES `vehicle_models` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: service_packages
-- =============================================
CREATE TABLE IF NOT EXISTS `service_packages` (
                                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                                  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
    `description` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKchi9s9s0lxqdmla9ebqhlp34x` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: appointments
-- =============================================
CREATE TABLE IF NOT EXISTS `appointments` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `customer_id` bigint DEFAULT NULL,
                                              `vehicle_id` bigint DEFAULT NULL,
                                              `center_id` bigint DEFAULT NULL,
                                              `technician_id` bigint DEFAULT NULL,
                                              `service_package_id` bigint DEFAULT NULL,
                                              `service_package_name` varchar(255) DEFAULT NULL,
    `appointment_date` datetime(6) NOT NULL,
    `milestone_km` int DEFAULT NULL,
    `estimated_cost` decimal(10,2) NOT NULL,
    `status` enum('PENDING','CONFIRMED','WAITING_FOR_APPROVAL','COMPLETED','CANCELLED') DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK4q5rt20vvnkv7eohwq22l3ayy` (`customer_id`),
    KEY `FK8rj1bo0yghp7xxocvsdxts3dd` (`center_id`),
    KEY `FKi31x63g3sb93cwo03xuexx6qa` (`technician_id`),
    KEY `FKalpncq8pxtwld2wmgw4sxct70` (`vehicle_id`),
    KEY `FKo9pu4xrn0xv4tmlbaqdi9hjme` (`service_package_id`),
    CONSTRAINT `FK4q5rt20vvnkv7eohwq22l3ayy` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK8rj1bo0yghp7xxocvsdxts3dd` FOREIGN KEY (`center_id`) REFERENCES `service_centers` (`id`),
    CONSTRAINT `FKalpncq8pxtwld2wmgw4sxct70` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`),
    CONSTRAINT `FKi31x63g3sb93cwo03xuexx6qa` FOREIGN KEY (`technician_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKo9pu4xrn0xv4tmlbaqdi9hjme` FOREIGN KEY (`service_package_id`) REFERENCES `service_packages` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: appointment_service_details
-- =============================================
CREATE TABLE IF NOT EXISTS `appointment_service_details` (
                                                             `id` bigint NOT NULL AUTO_INCREMENT,
                                                             `appointment_id` bigint NOT NULL,
                                                             `service_item_id` bigint NOT NULL,
                                                             `action_type` enum('CHECK','REPLACE') NOT NULL,
    `price` decimal(10,2) NOT NULL,
    `customer_approved` bit(1) NOT NULL,
    `technician_notes` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKnuf5bsi81se32nr3a2496ivd3` (`appointment_id`),
    KEY `FK3lrxsf9895h2ri8u6mbo0nool` (`service_item_id`),
    CONSTRAINT `FK3lrxsf9895h2ri8u6mbo0nool` FOREIGN KEY (`service_item_id`) REFERENCES `service_items` (`id`),
    CONSTRAINT `FKnuf5bsi81se32nr3a2496ivd3` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: maintenance_records
-- =============================================
CREATE TABLE IF NOT EXISTS `maintenance_records` (
                                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                                     `appointment_id` bigint DEFAULT NULL,
                                                     `odometer` int DEFAULT NULL,
                                                     `performed_at` datetime(6) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK39789tp9ugvby3a8icnhyfho7` (`appointment_id`),
    CONSTRAINT `FKck40sg41uu66rsx8b0m5ees2r` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: part_usages
-- =============================================
CREATE TABLE IF NOT EXISTS `part_usages` (
                                             `id` bigint NOT NULL AUTO_INCREMENT,
                                             `record_id` bigint NOT NULL,
                                             `spare_part_id` bigint NOT NULL,
                                             `quantity_used` int NOT NULL,
                                             `total_price` decimal(38,2) NOT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKfgo023iyenuya3hg50o9j5k8l` (`record_id`),
    KEY `FKj3d4m6ia43gs1hah9yu0eal86` (`spare_part_id`),
    CONSTRAINT `FKfgo023iyenuya3hg50o9j5k8l` FOREIGN KEY (`record_id`) REFERENCES `maintenance_records` (`id`),
    CONSTRAINT `FKj3d4m6ia43gs1hah9yu0eal86` FOREIGN KEY (`spare_part_id`) REFERENCES `spare_parts` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: invoices
-- =============================================
CREATE TABLE IF NOT EXISTS `invoices` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `record_id` bigint DEFAULT NULL,
                                          `center_id` bigint DEFAULT NULL,
                                          `total_amount` decimal(10,2) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK728va3etsjsc8uwm6h4xr01ax` (`record_id`),
    KEY `FKt5j9ruw0ypel5smwl5rcheh51` (`center_id`),
    CONSTRAINT `FK4c3jv4rvr6tts7tmuk1cxdmyt` FOREIGN KEY (`record_id`) REFERENCES `maintenance_records` (`id`),
    CONSTRAINT `FKt5j9ruw0ypel5smwl5rcheh51` FOREIGN KEY (`center_id`) REFERENCES `service_centers` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: payments
-- =============================================
CREATE TABLE IF NOT EXISTS `payments` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `invoice_id` bigint NOT NULL,
                                          `amount` decimal(38,2) DEFAULT NULL,
    `method` varchar(255) DEFAULT NULL,
    `transaction_code` varchar(255) DEFAULT NULL,
    `status` enum('UN_PAID','PAID','FAILED','CANCELLED') DEFAULT NULL,
    `payment_date` datetime(6) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK8inpv30544qjykcwa6ck7pusy` (`transaction_code`),
    KEY `FKrbqec6be74wab8iifh8g3i50i` (`invoice_id`),
    CONSTRAINT `FKrbqec6be74wab8iifh8g3i50i` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: chat_rooms
-- =============================================
CREATE TABLE IF NOT EXISTS `chat_rooms` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `name` varchar(255) NOT NULL,
    `status` enum('PENDING','ACTIVE','CLOSED') NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: chatroom_members
-- =============================================
CREATE TABLE IF NOT EXISTS `chatroom_members` (
                                                  `room_id` bigint NOT NULL,
                                                  `user_id` bigint NOT NULL,
                                                  PRIMARY KEY (`room_id`,`user_id`),
    KEY `FKbdf2bya1s7msnnm20r98ahmcc` (`user_id`),
    CONSTRAINT `FK1hiuwgi48nqqwh63l25gri9th` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`),
    CONSTRAINT `FKbdf2bya1s7msnnm20r98ahmcc` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: chat_messages
-- =============================================
CREATE TABLE IF NOT EXISTS `chat_messages` (
                                               `id` bigint NOT NULL AUTO_INCREMENT,
                                               `room_id` bigint NOT NULL,
                                               `sender_id` bigint NOT NULL,
                                               `content` text NOT NULL,
                                               `timestamp` datetime(6) NOT NULL,
    `status` enum('SENT','READ') NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKhalwepod3944695ji0suwoqb9` (`room_id`),
    KEY `FKgiqeap8ays4lf684x7m0r2729` (`sender_id`),
    CONSTRAINT `FKgiqeap8ays4lf684x7m0r2729` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKhalwepod3944695ji0suwoqb9` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: email_records
-- =============================================
CREATE TABLE IF NOT EXISTS `email_records` (
                                               `id` bigint NOT NULL AUTO_INCREMENT,
                                               `email` varchar(255) NOT NULL,
    `type` enum('PAYMENT','APPOINTMENT_DATE','KM') NOT NULL,
    `appointment_id` bigint DEFAULT NULL,
    `payment_id` bigint DEFAULT NULL,
    `vehicle_id` bigint DEFAULT NULL,
    `current_km` int DEFAULT NULL,
    `payment_status` enum('Success','Not_Success') DEFAULT NULL,
    `sent_time` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: invalidated_token
-- =============================================
CREATE TABLE IF NOT EXISTS `invalidated_token` (
                                                   `id` varchar(255) NOT NULL,
    `expiry_time` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: roles (unused but exists in schema)
-- =============================================
CREATE TABLE IF NOT EXISTS `roles` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `name` varchar(255) NOT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- Table: part_categories (unused but exists in schema)
-- =============================================
CREATE TABLE IF NOT EXISTS `part_categories` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                                 `code` varchar(255) DEFAULT NULL,
    `name` varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `create_at` timestamp NULL DEFAULT NULL,
    `update_at` timestamp NULL DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

