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

/* insert data */
/*BEGIN
	INSERT INTO product ([id], [date], [event], [name], [description], [price], [amount], [supplier], [brand], [details_viewed])
	VALUES ('55894a2e-6063-40a3-aa08-b29b994bd750', '2021-05-19 21:33:41.9133333', 'created', 'USB-C kabel', 
	'Samsung USB-C datakabel voor uw Samsung toestel met USB-C aansluiting. ' +
	'Gebruik deze datakabel om bijvoorbeeld uw Samsung apparaat te synchroniseren met uw computer of om op te laden. ' +
	'Deze Samsung datakabel heeft een USB-C aansluiting, duurzaam en van hoge kwaliteit.',
	13.49, 25, 'allekabels.nl', 'Samsung', 67);

	INSERT INTO product ([id], [date], [event], [name], [description], [price], [amount], [supplier], [brand], [details_viewed])
	VALUES ('b7ea538f-78ed-4ae7-8b7b-7f59e6f6709c', '2021-05-19 21:33:41.9133333', 'created', 'AirTag', 
	'Met de Apple AirTag hoef je nooit meer lang naar je spullen te zoeken. ' +
	'Je kan de AirTag bevestigen aan je sleutels, je huisdier, je koffer of je stopt hem bijvoorbeeld in je tas. ' +
	'De locatie van de AirTag verschijnt automatisch op de kaart in de zoek mijn- app, waar je ook al je andere Apple apparaten kan zien.',
	13.49, 25, 'ball.com', 'Apple', 23);
END;*/
