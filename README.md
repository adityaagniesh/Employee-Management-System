# Employee Management System

A simple **Employee Management System** built with **Spring Boot** that performs CRUD operations and uses **Spring Security** for role-based access control. Passwords are hashed using **BCrypt**.

## Features
- Create, Read, Update, Delete (CRUD) employees
- Role-based access control (`ROLE_ADMIN`, `ROLE_USER`)
- Authentication and authorization via Spring Security
- Password hashing with BCrypt
- Clean Thymeleaf/Bootstrap frontend (or REST endpoints if used as API)
- Example seeding for admin/user accounts (optional)

## Tech stack
- Java 21
- Spring Boot
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Thymeleaf (if using server-side UI)
- Database: H2 (dev) / MySQL (production)
- Build: Maven (or Gradle)
- Frontend: Bootstrap 5 (optional, included in templates)

# sql scripts
CREATE DATABASE  IF NOT EXISTS `employee_directory`;
USE `employee_directory`;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Data for table `employee`
--

INSERT INTO `employee` VALUES 
	(1,'firstName','lastName','example@gmail.com'),
	(2,'Jhon','Doe','jhon@gmail.com');

-- src/main/resources/schema.sql

CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);


