//use this code to create the database 

CREATE DATABASE car_rental;

CREATE TABLE available_cars (
    id INT(11) AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
    name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
    hp INT(11) NULL,
    transmission VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
    category VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
    car_statut INT(20) NULL,
    image_url VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL
);

CREATE TABLE users (
    id INT(11) AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    email VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    password VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    address VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_type VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
    image_url VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL
);
