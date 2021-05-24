IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'ballComAuth')
    BEGIN CREATE DATABASE [ballComAuth] END;

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'ballComSupport')
    BEGIN CREATE DATABASE [ballComSupport] END;

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'ballComShipping')
    BEGIN CREATE DATABASE [ballComShipping] END;

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'shopping-writedb')
    BEGIN CREATE DATABASE [shopping-writedb] END;

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'shopping-readdb')
    BEGIN CREATE DATABASE [shopping-readdb] END;

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

/* Create shopping write database */
BEGIN
	USE [shopping-readdb];

DROP TABLE IF EXISTS basket;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    [productId] NVARCHAR(50),
    [data] NVARCHAR(MAX),
    CONSTRAINT pk_product PRIMARY KEY ([productId])
    );

CREATE TABLE basket (
    [customerId] NVARCHAR(50),
    [data] NVARCHAR(MAX),
    CONSTRAINT pk_basket PRIMARY KEY ([customerId])
    );
END;
