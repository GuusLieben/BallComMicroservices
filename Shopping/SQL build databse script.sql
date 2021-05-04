USE shopping;

DROP TABLE IF EXISTS basket;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS shopping;

CREATE TABLE product (
	[id] NVARCHAR(100),
	[name] NVARCHAR(50),
	[description] NVARCHAR(999),
	[price] DECIMAL(5,2),
	[amount] SMALLINT,
	[supplier] NVARCHAR(50),
	[brand] NVARCHAR(50),
	CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE customer (
	[email] NVARCHAR(50),
	CONSTRAINT pk_customer PRIMARY KEY (email)
);

CREATE TABLE basket (
	[amount] SMALLINT,
	product_id NVARCHAR(100),
	customer_id NVARCHAR(50),
	FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (customer_id) REFERENCES customer(email)
);