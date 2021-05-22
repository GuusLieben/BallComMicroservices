BEGIN
	IF DB_ID('shopping-readdb') IS NULL
	CREATE DATABASE [shopping-readdb];
END;

/* Create shopping write database */
BEGIN
	USE [shopping-readdb];

	DROP TABLE IF EXISTS basket;
	DROP TABLE IF EXISTS customer;
	DROP TABLE IF EXISTS product;

	CREATE TABLE product (
		[productId] NVARCHAR(50),
		[data] NVARCHAR(999),
		CONSTRAINT pk_product PRIMARY KEY ([productId])
	);

	CREATE TABLE basket (
		[customerId] NVARCHAR(50),
		[data] NVARCHAR(999),
		CONSTRAINT pk_basket PRIMARY KEY ([customerId])
	);
END;

BEGIN
	INSERT INTO product ([productId], [data])
	VALUES ('b7ea538f-78ed-4ae7-8b7b-7f59e6f6709c', '{ "productId": "b7ea538f-78ed-4ae7-8b7b-7f59e6f6709c", "name": "Apple Watch", "amount": 43 }');

INSERT INTO product ([productId], [data])
	VALUES ('55894a2e-6063-40a3-aa08-b29b994bd750', '{ "productId": "55894a2e-6063-40a3-aa08-b29b994bd750", "name": "USB-C kabel", "amount": 43 }');
END;