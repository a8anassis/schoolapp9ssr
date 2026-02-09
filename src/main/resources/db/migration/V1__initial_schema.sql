CREATE TABLE regions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT pk_regions PRIMARY KEY (id),
    CONSTRAINT uk_regions_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE teachers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uuid BINARY(16) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    vat VARCHAR(255) NOT NULL,
    region_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    CONSTRAINT pk_teachers PRIMARY KEY (id),
    CONSTRAINT uk_teachers_uuid UNIQUE (uuid),
    CONSTRAINT uk_teachers_vat UNIQUE (vat),
    CONSTRAINT fk_teachers_regions FOREIGN KEY (region_id)
        REFERENCES regions(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_teachers_region_id (region_id),
    INDEX idx_teachers_lastname (lastname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;