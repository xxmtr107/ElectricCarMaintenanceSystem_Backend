-- V1__Create_Initial_Schema.sql
-- Initial database schema for Vehicle Maintenance System

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;

-- =============================================
-- Table: service_centers
-- =============================================
CREATE TABLE service_centers
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    create_at  TIMESTAMP(0)          NULL,
    update_at  TIMESTAMP(0)          NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NULL,
    address    VARCHAR(255)          NULL,
    district   VARCHAR(100)          NULL,
    city       VARCHAR(100)          NULL,
    phone      VARCHAR(255)          NULL,
    CONSTRAINT pk_service_centers PRIMARY KEY (id)
);
-- =============================================
-- Table: users
-- =============================================
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT                   NOT NULL,
    create_at  TIMESTAMP(0)                            NULL,
    update_at  TIMESTAMP(0)                            NULL,
    created_by VARCHAR(255)                            NULL,
    updated_by VARCHAR(255)                            NULL,
    username   VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    email      VARCHAR(255)                            NOT NULL,
    password   VARCHAR(255)                            NOT NULL,
    phone      VARCHAR(255)                            NOT NULL,
    last_login datetime                                NULL,
    full_name  VARCHAR(255)                            NOT NULL,
    gender     VARCHAR(255)                            NULL,
    `role`     VARCHAR(255)                            NULL,
    center_id  BIGINT                                  NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT UK_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT UK_username UNIQUE (username);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CENTER FOREIGN KEY (center_id) REFERENCES service_centers (id);
-- =============================================
-- Table: vehicle_models
-- =============================================
CREATE TABLE vehicle_models
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    create_at  TIMESTAMP(0)          NULL,
    update_at  TIMESTAMP(0)          NULL,
    created_by VARCHAR(255)          NULL,
    updated_by VARCHAR(255)          NULL,
    name       VARCHAR(255)          NOT NULL,
    model_year VARCHAR(255)          NULL,
    CONSTRAINT pk_vehicle_models PRIMARY KEY (id)
);

ALTER TABLE vehicle_models
    ADD CONSTRAINT uc_vehicle_models_name UNIQUE (name);
-- =============================================
-- Table: vehicles
-- =============================================
CREATE TABLE vehicles
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    create_at     TIMESTAMP(0)          NULL,
    update_at     TIMESTAMP(0)          NULL,
    created_by    VARCHAR(255)          NULL,
    updated_by    VARCHAR(255)          NULL,
    license_plate VARCHAR(255)          NOT NULL,
    vin           VARCHAR(255)          NULL,
    current_km    INT                   NULL,
    purchase_year date                  NULL,
    customer_id   BIGINT                NOT NULL,
    model_id      BIGINT                NOT NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (id)
);

ALTER TABLE vehicles
    ADD CONSTRAINT uc_vehicles_license_plate UNIQUE (license_plate);

ALTER TABLE vehicles
    ADD CONSTRAINT FK_VEHICLES_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES users (id);

ALTER TABLE vehicles
    ADD CONSTRAINT FK_VEHICLES_ON_MODEL FOREIGN KEY (model_id) REFERENCES vehicle_models (id);
-- =============================================
-- Table: service_items
-- =============================================
CREATE TABLE service_items
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    create_at     TIMESTAMP(0)          NULL,
    update_at     TIMESTAMP(0)          NULL,
    created_by    VARCHAR(255)          NULL,
    updated_by    VARCHAR(255)          NULL,
    name          NVARCHAR(100)         NOT NULL,
    `description` NVARCHAR(200)         NULL,
    CONSTRAINT pk_service_items PRIMARY KEY (id)
);

ALTER TABLE service_items
    ADD CONSTRAINT uc_service_items_name UNIQUE (name);
-- =============================================
-- Table: spare_parts
-- =============================================
CREATE TABLE spare_parts
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    create_at           TIMESTAMP(0)          NULL,
    update_at           TIMESTAMP(0)          NULL,
    created_by          VARCHAR(255)          NULL,
    updated_by          VARCHAR(255)          NULL,
    part_number         VARCHAR(255)          NOT NULL,
    name                VARCHAR(255)          NULL,
    unit_price          DECIMAL(10, 2)        NOT NULL,
    quantity_in_stock   INT                   NOT NULL,
    minimum_stock_level INT DEFAULT 10        NOT NULL,
    category_name       VARCHAR(255)          NULL,
    category_code       VARCHAR(255)          NULL,
    CONSTRAINT pk_spare_parts PRIMARY KEY (id)
);

ALTER TABLE spare_parts
    ADD CONSTRAINT uc_spare_parts_part_number UNIQUE (part_number);
-- =============================================
-- Table: model_package_items
-- =============================================
CREATE TABLE model_package_items
(
    id                     BIGINT AUTO_INCREMENT NOT NULL,
    create_at              TIMESTAMP(0)          NULL,
    update_at              TIMESTAMP(0)          NULL,
    created_by             VARCHAR(255)          NULL,
    updated_by             VARCHAR(255)          NULL,
    price                  DECIMAL               NOT NULL,
    vehicle_model_id       BIGINT                NOT NULL,
    milestone_km           INT                   NOT NULL,
    service_item_id        BIGINT                NULL,
    action_type            VARCHAR(255)          NOT NULL,
    included_spare_part_id BIGINT                NULL,
    included_quantity      INT DEFAULT 1         NULL,
    CONSTRAINT pk_model_package_items PRIMARY KEY (id)
);

ALTER TABLE model_package_items
    ADD CONSTRAINT FK_MODEL_PACKAGE_ITEMS_ON_INCLUDED_SPARE_PART FOREIGN KEY (included_spare_part_id) REFERENCES spare_parts (id);

ALTER TABLE model_package_items
    ADD CONSTRAINT FK_MODEL_PACKAGE_ITEMS_ON_SERVICE_ITEM FOREIGN KEY (service_item_id) REFERENCES service_items (id);

ALTER TABLE model_package_items
    ADD CONSTRAINT FK_MODEL_PACKAGE_ITEMS_ON_VEHICLE_MODEL FOREIGN KEY (vehicle_model_id) REFERENCES vehicle_models (id);
-- =============================================
-- Table: appointments
-- =============================================
CREATE TABLE appointments
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    create_at            TIMESTAMP(0)          NULL,
    update_at            TIMESTAMP(0)          NULL,
    created_by           VARCHAR(255)          NULL,
    updated_by           VARCHAR(255)          NULL,
    appointment_date     datetime              NOT NULL,
    milestone_km         INT                   NULL,
    status               VARCHAR(255)          NULL,
    estimated_cost       DECIMAL(10, 2)        NOT NULL,
    customer_id          BIGINT                NULL,
    technician_id        BIGINT                NULL,
    vehicle_id           BIGINT                NULL,
    service_package_name VARCHAR(255)          NULL,
    center_id            BIGINT                NULL,
    CONSTRAINT pk_appointments PRIMARY KEY (id)
);

ALTER TABLE appointments
    ADD CONSTRAINT FK_APPOINTMENTS_ON_CENTER FOREIGN KEY (center_id) REFERENCES service_centers (id);

ALTER TABLE appointments
    ADD CONSTRAINT FK_APPOINTMENTS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES users (id);

ALTER TABLE appointments
    ADD CONSTRAINT FK_APPOINTMENTS_ON_TECHNICIAN FOREIGN KEY (technician_id) REFERENCES users (id);

ALTER TABLE appointments
    ADD CONSTRAINT FK_APPOINTMENTS_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicles (id);
-- =============================================
-- Table: appointment_service_details
-- =============================================
CREATE TABLE appointment_service_details
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    create_at         TIMESTAMP(0)          NULL,
    update_at         TIMESTAMP(0)          NULL,
    created_by        VARCHAR(255)          NULL,
    updated_by        VARCHAR(255)          NULL,
    appointment_id    BIGINT                NOT NULL,
    service_item_id   BIGINT                NOT NULL,
    action_type       VARCHAR(255)          NOT NULL,
    price             DECIMAL(10, 2)        NOT NULL,
    customer_approved BIT(1)                NOT NULL,
    technician_notes  NVARCHAR(255)         NULL,
    CONSTRAINT pk_appointment_service_details PRIMARY KEY (id)
);

ALTER TABLE appointment_service_details
    ADD CONSTRAINT FK_APPOINTMENT_SERVICE_DETAILS_ON_APPOINTMENT FOREIGN KEY (appointment_id) REFERENCES appointments (id);

ALTER TABLE appointment_service_details
    ADD CONSTRAINT FK_APPOINTMENT_SERVICE_DETAILS_ON_SERVICE_ITEM FOREIGN KEY (service_item_id) REFERENCES service_items (id);
-- =============================================
-- Table: maintenance_records
-- =============================================
CREATE TABLE maintenance_records
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    create_at      TIMESTAMP(0)          NULL,
    update_at      TIMESTAMP(0)          NULL,
    created_by     VARCHAR(255)          NULL,
    updated_by     VARCHAR(255)          NULL,
    odometer       INT                   NULL,
    performed_at   datetime              NULL,
    appointment_id BIGINT                NULL,
    CONSTRAINT pk_maintenance_records PRIMARY KEY (id)
);

ALTER TABLE maintenance_records
    ADD CONSTRAINT uc_maintenance_records_appointment UNIQUE (appointment_id);

ALTER TABLE maintenance_records
    ADD CONSTRAINT FK_MAINTENANCE_RECORDS_ON_APPOINTMENT FOREIGN KEY (appointment_id) REFERENCES appointments (id);
-- =============================================
-- Table: part_usages
-- =============================================
CREATE TABLE part_usages
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    create_at     TIMESTAMP(0)          NULL,
    update_at     TIMESTAMP(0)          NULL,
    created_by    VARCHAR(255)          NULL,
    updated_by    VARCHAR(255)          NULL,
    quantity_used INT                   NOT NULL,
    total_price   DECIMAL               NOT NULL,
    spare_part_id BIGINT                NOT NULL,
    record_id     BIGINT                NOT NULL,
    CONSTRAINT pk_part_usages PRIMARY KEY (id)
);

ALTER TABLE part_usages
    ADD CONSTRAINT FK_PART_USAGES_ON_RECORD FOREIGN KEY (record_id) REFERENCES maintenance_records (id);

ALTER TABLE part_usages
    ADD CONSTRAINT FK_PART_USAGES_ON_SPARE_PART FOREIGN KEY (spare_part_id) REFERENCES spare_parts (id);
-- =============================================
-- Table: invoices
-- =============================================
CREATE TABLE invoices
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    create_at    TIMESTAMP(0)          NULL,
    update_at    TIMESTAMP(0)          NULL,
    created_by   VARCHAR(255)          NULL,
    updated_by   VARCHAR(255)          NULL,
    total_amount DECIMAL(10, 2)        NULL,
    status       VARCHAR(255)          NULL,
    record_id    BIGINT                NULL,
    center_id    BIGINT                NULL,
    CONSTRAINT pk_invoices PRIMARY KEY (id)
);

ALTER TABLE invoices
    ADD CONSTRAINT uc_invoices_record UNIQUE (record_id);

ALTER TABLE invoices
    ADD CONSTRAINT FK_INVOICES_ON_CENTER FOREIGN KEY (center_id) REFERENCES service_centers (id);

ALTER TABLE invoices
    ADD CONSTRAINT FK_INVOICES_ON_RECORD FOREIGN KEY (record_id) REFERENCES maintenance_records (id);
-- =============================================
-- Table: payments
-- =============================================
CREATE TABLE payments
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    create_at        TIMESTAMP(0)          NULL,
    update_at        TIMESTAMP(0)          NULL,
    created_by       VARCHAR(255)          NULL,
    updated_by       VARCHAR(255)          NULL,
    method           VARCHAR(255)          NULL,
    amount           DECIMAL               NULL,
    payment_date     datetime              NULL,
    status           VARCHAR(255)          NULL,
    transaction_code VARCHAR(255)          NULL,
    invoice_id       BIGINT                NOT NULL,
    CONSTRAINT pk_payments PRIMARY KEY (id)
);

ALTER TABLE payments
    ADD CONSTRAINT uc_payments_transaction_code UNIQUE (transaction_code);

ALTER TABLE payments
    ADD CONSTRAINT FK_PAYMENTS_ON_INVOICE FOREIGN KEY (invoice_id) REFERENCES invoices (id);
-- =============================================
-- Table: chat_rooms
-- =============================================
CREATE TABLE chat_rooms
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    name   VARCHAR(255)          NOT NULL,
    status VARCHAR(20)           NOT NULL,
    CONSTRAINT pk_chat_rooms PRIMARY KEY (id)
);

CREATE TABLE chatroom_members
(
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_chatroom_members PRIMARY KEY (room_id, user_id)
);

ALTER TABLE chatroom_members
    ADD CONSTRAINT fk_chamem_on_chat_room FOREIGN KEY (room_id) REFERENCES chat_rooms (id);

ALTER TABLE chatroom_members
    ADD CONSTRAINT fk_chamem_on_user FOREIGN KEY (user_id) REFERENCES users (id);
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
CREATE TABLE chat_messages
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    sender_id BIGINT                NOT NULL,
    room_id   BIGINT                NOT NULL,
    content   TEXT                  NOT NULL,
    timestamp datetime              NOT NULL,
    status    VARCHAR(10)           NOT NULL,
    CONSTRAINT pk_chat_messages PRIMARY KEY (id)
);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_ROOM FOREIGN KEY (room_id) REFERENCES chat_rooms (id);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);
-- =============================================
-- Table: email_records
-- =============================================
CREATE TABLE email_records
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    email          VARCHAR(255)          NOT NULL,
    type           VARCHAR(255)          NOT NULL,
    sent_time      datetime              NULL,
    current_km     INT                   NULL,
    appointment_id BIGINT                NULL,
    payment_id     BIGINT                NULL,
    vehicle_id     BIGINT                NULL,
    payment_status VARCHAR(255)          NULL,
    CONSTRAINT pk_email_records PRIMARY KEY (id)
);
-- =============================================
-- Table: invalidated_token
-- =============================================
CREATE TABLE invalidated_token
(
    id          VARCHAR(255) NOT NULL,
    expiry_time datetime     NULL,
    CONSTRAINT pk_invalidatedtoken PRIMARY KEY (id)
);