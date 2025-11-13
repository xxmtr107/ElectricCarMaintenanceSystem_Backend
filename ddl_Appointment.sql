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