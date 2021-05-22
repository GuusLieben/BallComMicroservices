IF DB_ID('shopping-readdb') IS NULL
BEGIN
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