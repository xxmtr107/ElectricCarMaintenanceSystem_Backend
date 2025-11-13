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