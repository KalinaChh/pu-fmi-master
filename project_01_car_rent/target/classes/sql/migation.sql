CREATE TABLE td_cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    location VARCHAR(50) NOT NULL,
    price_per_day DECIMAL(10, 2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE td_clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    age INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    has_accidents BOOLEAN DEFAULT FALSE
);


CREATE TABLE td_offers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    car_id INT NOT NULL,
    rental_days INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    is_accepted BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES td_clients(id),
    FOREIGN KEY (car_id) REFERENCES td_cars(id)
);