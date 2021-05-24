BEGIN
	IF DB_ID('shopping-writedb') IS NULL
	CREATE DATABASE [shopping-writedb];
END;

/* Create shopping write database */
BEGIN
	USE [shopping-writedb];

	DROP TABLE IF EXISTS basket;
	DROP TABLE IF EXISTS customer;
	DROP TABLE IF EXISTS product;

	CREATE TABLE product (
		[productId] NVARCHAR(50),
		[date] DATETIME2 DEFAULT GETDATE(),
		[event] NVARCHAR(100),
		[data] NVARCHAR(MAX)
		CONSTRAINT pk_product PRIMARY KEY ([productId], [date])
	);

	CREATE TABLE basket (
		[customerId] NVARCHAR(50),
		[date] DATETIME2 DEFAULT GETDATE(),
		[event] NVARCHAR(100),
		[data] NVARCHAR(MAX),
		CONSTRAINT pk_basket PRIMARY KEY ([customerId], [date])
	);
END;
