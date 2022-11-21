DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS accounts;

CREATE TABLE customer
(
    customer_id   INT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL,
    mobile_number VARCHAR(20)  NOT NULL,
    create_dt     date,
    CONSTRAINT pk_customer PRIMARY KEY (customer_id)
);

CREATE TABLE accounts
(
    account_number BIGINT AUTO_INCREMENT NOT NULL,
    customer_id    INT          NOT NULL,
    account_type   VARCHAR(100) NOT NULL,
    branch_address VARCHAR(200) NOT NULL,
    create_dt      date,
    CONSTRAINT pk_accounts PRIMARY KEY (account_number)
);

INSERT INTO customer (name, email, `mobile_number`, `create_dt`)
VALUES ('Densoft', 'tutor@densoft.com', '078945566', CURDATE());

INSERT INTO accounts (`customer_id`, `account_number`, `account_type`, `branch_address`, `create_dt`)
VALUES (1, 132432435, 'Savings', '123 Main Street, New york', CURDATE())