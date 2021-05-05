IF DB_ID('shopping') IS NULL
BEGIN
	CREATE DATABASE shopping;
END;

/* Create shopping write database */
BEGIN
	USE shopping;

	DROP TABLE IF EXISTS basket;
	DROP TABLE IF EXISTS customer;
	DROP TABLE IF EXISTS product;
	DROP TABLE IF EXISTS shopping;

	CREATE TABLE product (
		[id] NVARCHAR(50),
		[name] NVARCHAR(50),
		[description] NVARCHAR(999),
		[price] DECIMAL(5,2),
		[amount] SMALLINT,
		[supplier] NVARCHAR(50),
		[brand] NVARCHAR(50),
		[details_viewed] SMALLINT,
		CONSTRAINT pk_product PRIMARY KEY (id)
	);

	CREATE TABLE customer (
		[email] NVARCHAR(50),
		CONSTRAINT pk_customer PRIMARY KEY (email)
	);

	CREATE TABLE basket (
		[amount] SMALLINT,
		product_id NVARCHAR(50),
		customer_id NVARCHAR(50),
		FOREIGN KEY (product_id) REFERENCES product(id) ON UPDATE CASCADE,
		FOREIGN KEY (customer_id) REFERENCES customer(email) ON UPDATE CASCADE
	);
END;

/* insert data */
BEGIN
	INSERT INTO product ([id], [name], [description], [price], [amount], [supplier], [brand], [details_viewed])
	VALUES ('55894a2e-6063-40a3-aa08-b29b994bd750', 'USB-C kabel', 
	'Samsung USB-C datakabel voor uw Samsung toestel met USB-C aansluiting. ' +
	'Gebruik deze datakabel om bijvoorbeeld uw Samsung apparaat te synchroniseren met uw computer of om op te laden. ' +
	'Deze Samsung datakabel heeft een USB-C aansluiting, duurzaam en van hoge kwaliteit.',
	13.49, 25, 'allekabels.nl', 'Samsung', 67);

	INSERT INTO product ([id], [name], [description], [price], [amount], [supplier], [brand], [details_viewed])
	VALUES ('b7ea538f-78ed-4ae7-8b7b-7f59e6f6709c', 'AirTag', 
	'Met de Apple AirTag hoef je nooit meer lang naar je spullen te zoeken. ' +
	'Je kan de AirTag bevestigen aan je sleutels, je huisdier, je koffer of je stopt hem bijvoorbeeld in je tas. ' +
	'De locatie van de AirTag verschijnt automatisch op de kaart in de zoek mijn- app, waar je ook al je andere Apple apparaten kan zien.',
	13.49, 25, 'ball.com', 'Apple', 23);
END;
