CREATE TABLE persons(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(150) UNIQUE,
    phone VARCHAR(20) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB;

CREATE TABLE users(
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE','INACTIVE','DELETED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role ENUM('ADMIN','TECHNICIAN','RECEPTION'),

    deleted_at TIMESTAMP DEFAULT NULL,
    INDEX (status),
    INDEX (created_at),
    CONSTRAINT fk_users_person FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE

)ENGINE=InnoDB;

CREATE TABLE clients(
    id BIGINT PRIMARY KEY,
    customer_code VARCHAR(50),
    credit_limit DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMP DEFAULT NULL,
    INDEX (created_at),
    CONSTRAINT fk_clients_person FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE devices(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (created_at)
)ENGINE=InnoDB;

CREATE TABLE customer_devices(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    client_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,

    serial_number VARCHAR(100),
    imei VARCHAR(50),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (client_id),
    CONSTRAINT fk_customer_devices_clients FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_device_id_devices FOREIGN KEY (device_id) REFERENCES devices(id)
)ENGINE=InnoDB;

CREATE TABLE service_orders(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    folio VARCHAR(50) UNIQUE,

    customer_device_id BIGINT NOT NULL,

    problem_description TEXT,
    diagnosis TEXT,

    status ENUM('RECEIVED','IN_DIAGNOSIS','WAITING_PARTS','IN_REPAIR','WAITING_AUTHORIZATION','READY','DELIVERED','NOT_REPAIRABLE') DEFAULT 'RECEIVED',

    assigned_to BIGINT NULL,

    received_at TIMESTAMP,
    estimated_delivery TIMESTAMP,
    delivered_at TIMESTAMP NULL,

    created_by BIGINT NULL,
    created_by_snapshot VARCHAR(150),

    estimated_cost DECIMAL(10,2),

    INDEX (status),
    CONSTRAINT fk_customer_device_id FOREIGN KEY (customer_device_id) REFERENCES customer_devices(id),

    CONSTRAINT fk_assigned_to_users FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL
)ENGINE=InnoDB;

CREATE TABLE service_order_history(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    service_order_id BIGINT,
    status ENUM('RECEIVED','IN_DIAGNOSIS','WAITING_PARTS','IN_REPAIR','WAITING_AUTHORIZATION','READY','DELIVERED','NOT_REPAIRABLE') DEFAULT 'RECEIVED',
    notes TEXT,

    changed_by BIGINT,
    changed_by_snapshot VARCHAR(150),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (status),
    INDEX (created_at),
    CONSTRAINT fk_service_order_id_service_orders FOREIGN KEY (service_order_id) REFERENCES service_orders(id)
)ENGINE=InnoDB;

CREATE TABLE parts_products(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(150),
    sku VARCHAR(100) UNIQUE,

    item_type ENUM('PRODUCT','PART') NOT NULL,

    price DECIMAL(10,2),
    cost DECIMAL(10,2),
    is_for_sale TINYINT(1) NOT NULL DEFAULT 1,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NULL,

    INDEX (created_at),
    CONSTRAINT fk_products_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
)ENGINE=InnoDB;

CREATE TABLE sales(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    sale_folio VARCHAR(50) UNIQUE,
    client_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    total DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING','PAID','CANCELLED') DEFAULT 'PENDING',
    sale_type ENUM('CASH','CREDIT') NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (client_id),
    INDEX (user_id),
    INDEX (status),
    INDEX (created_at),
    CONSTRAINT fk_sales_client_id_clients FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_user_id_users FOREIGN KEY (user_id) REFERENCES users(id)
)ENGINE=InnoDB;

CREATE TABLE sale_parts_products(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    sale_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    INDEX (sale_id),
    INDEX (item_id),
    CONSTRAINT fk_sale_id_sales FOREIGN KEY (sale_id) REFERENCES sales(id),
    CONSTRAINT fk_parts_products_items FOREIGN KEY (item_id) REFERENCES parts_products(id)
)ENGINE=InnoDB;

CREATE TABLE client_credits(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    client_id BIGINT NOT NULL,
    sale_id BIGINT NOT NULL,

    total_amount DECIMAL(10,2) NOT NULL,
    balance DECIMAL(10,2) NOT NULL,

    status ENUM('ACTIVE','PAID','OVERDUE') DEFAULT 'ACTIVE',

    due_date DATE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (client_id),
    INDEX (status),
    INDEX (created_at),
    CONSTRAINT fk_client_credits_client_id_clients FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_client_credits_sale_id_sales FOREIGN KEY (sale_id) REFERENCES sales(id)
)ENGINE=InnoDB;

CREATE TABLE payments(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    credit_id BIGINT NULL,
    sale_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,

    amount DECIMAL(10,2) NOT NULL,

    payment_method ENUM('CASH','CARD','TRANSFER','CREDIT') NOT NULL,

    reference VARCHAR(100), -- folio bancario, terminal, etc.

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (client_id),
    INDEX (created_at),
    INDEX (sale_id),
    INDEX (credit_id),
    CONSTRAINT fk_payments_sale_id_sales FOREIGN KEY (sale_id) REFERENCES sales(id),
    CONSTRAINT fk_payments_client_id_clients FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_payments_credit_id_client_credits FOREIGN KEY (credit_id) REFERENCES client_credits(id)
)ENGINE=InnoDB;

CREATE TABLE returns(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    sale_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,

    return_type ENUM('WARRANTY','REFUND','EXCHANGE') NOT NULL,

    status ENUM('RECEIVED','UNDER_REVIEW','APPROVED','REJECTED','RESOLVED'),

    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (client_id),
    INDEX (status),
    INDEX (created_at),
    CONSTRAINT fk_returns_sale_id_sales FOREIGN KEY (sale_id) REFERENCES sales(id),
    CONSTRAINT fk_returns_client_id_clients FOREIGN KEY (client_id) REFERENCES clients(id)
)ENGINE=InnoDB;

CREATE TABLE return_items(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    return_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    reason TEXT,

    CONSTRAINT fk_return_items_return_id_returns FOREIGN KEY (return_id) REFERENCES returns(id),
    CONSTRAINT fk_return_items_item_id_parts_products FOREIGN KEY (item_id) REFERENCES parts_products(id)
)ENGINE=InnoDB;

CREATE TABLE warranty_process(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    return_id BIGINT NOT NULL,

    service_order_id BIGINT NULL,

    resolution ENUM('REPAIRED','REPLACED','REFUNDED','NOT_APPLICABLE'),

    completed_at TIMESTAMP,

    CONSTRAINT fk_warranty_process_return_id_returns FOREIGN KEY (return_id) REFERENCES returns(id),
    CONSTRAINT fk_warranty_process_service_order_id_service_orders FOREIGN KEY (service_order_id) REFERENCES service_orders(id)
)ENGINE=InnoDB;

CREATE TABLE service_order_parts(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    service_order_id BIGINT NOT NULL,
    part_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    status ENUM('REQUESTED','ORDERED','RECEIVED','USED'),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (status),
    INDEX (created_at),
    CONSTRAINT service_order_parts_service_order_id_service_orders FOREIGN KEY (service_order_id) REFERENCES service_orders(id),
    CONSTRAINT fk_service_order_parts_part_id_parts_products FOREIGN KEY (part_id) REFERENCES parts_products(id)
)ENGINE=InnoDB;

CREATE TABLE service_order_images(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    service_order_id BIGINT NOT NULL,

    image_path VARCHAR(500) NOT NULL, -- ruta o URL
    image_type ENUM('RECEPTION','DIAGNOSIS','REPAIR','DELIVERY') NOT NULL,

    description VARCHAR(255),

    taken_by BIGINT NULL,
    taken_by_snapshot VARCHAR(150),

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (service_order_id) REFERENCES service_orders(id),
    FOREIGN KEY (taken_by) REFERENCES users(id) ON DELETE SET NULL,

    INDEX idx_order (service_order_id),
    INDEX idx_type (image_type)
)ENGINE=InnoDB;

CREATE TABLE service_order_authorization(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    service_order_id BIGINT NOT NULL,

    estimated_cost DECIMAL(10,2),
    notes TEXT,

    authorization_status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    authorized_at TIMESTAMP NULL,

    client_response TEXT, -- "acepto", "no quiero reparar", etc.

    responded_at TIMESTAMP NULL,

    created_by BIGINT NULL,
    created_by_snapshot VARCHAR(150),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (authorization_status),
    INDEX (created_at),
    CONSTRAINT fk_service_order_authorization_service_order_id_service_orders FOREIGN KEY (service_order_id) REFERENCES service_orders(id)
)ENGINE=InnoDB;

CREATE TABLE service_order_products_parts(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    service_order_id BIGINT NOT NULL,
    part_product_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    usage_type ENUM('CONSUMED','SOLD'),

    INDEX (service_order_id),
    INDEX (part_product_id),
    CONSTRAINT fk_service_order_products_parts_service_order_id_service_orders FOREIGN KEY (service_order_id) REFERENCES service_orders(id),
    CONSTRAINT fk_service_order_products_parts_part_product_id_parts_products FOREIGN KEY (part_product_id) REFERENCES parts_products(id)
)ENGINE=InnoDB;

CREATE TABLE warehouses(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (created_at)
)ENGINE=InnoDB;

CREATE TABLE product_stock(
    product_id BIGINT,
    warehouse_id BIGINT,

    stock INT NOT NULL DEFAULT 0,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (product_id, warehouse_id),

    CONSTRAINT fk_product_stock_product_id_parts_products FOREIGN KEY (product_id) REFERENCES parts_products(id),
    CONSTRAINT fk_product_stock_warehouse_id_warehouses FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
)ENGINE=InnoDB;


CREATE TABLE inventory_movements(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    warehouse_id BIGINT NOT NULL,
    reference_type ENUM('SALE','PURCHASE','SERVICE','ADJUSTMENT','RETURN'),
    reference_id BIGINT,

    product_id BIGINT NOT NULL,
    movement_type ENUM('IN','OUT','ADJUSTMENT') NOT NULL,
    quantity INT NOT NULL,

    -- usuario responsable
    created_by BIGINT NULL,
    created_by_snapshot VARCHAR(150),

    reference VARCHAR(255), -- factura, nota, etc.

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (created_at),
    INDEX idx_product (product_id),
    INDEX idx_warehouse (warehouse_id),
    INDEX idx_reference (reference_type, reference_id),
    CONSTRAINT fk_movements_product FOREIGN KEY (product_id) REFERENCES parts_products(id),

    CONSTRAINT fk_movements_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,

    CONSTRAINT fk_inventory_movements_warehouse_id_warehouses FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
)ENGINE=InnoDB;

CREATE TABLE audit_log(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NULL,              -- puede ser NULL si el usuario ya no existe
    user_snapshot VARCHAR(255),       -- nombre/email en el momento

    action ENUM('INSERT','UPDATE','DELETE','SELECT'),
    table_name VARCHAR(100),
    record_id BIGINT,

    old_values JSON NULL,
    new_values JSON NULL,

    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX (user_id),
    INDEX idx_audit_table (table_name),
    INDEX idx_audit_record (record_id),
    INDEX idx_audit_date (created_at)
)ENGINE=InnoDB;