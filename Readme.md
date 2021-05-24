# Ball.com applicatie draaien

## Stap 1: Run docker-compose up --force-recreate --build
## Stap 2: (Mogelijk) bugfixing
Voor enkele microservices moeten databases los aangemaakt worden. Hiervoor wordt een een bash script gedraaid bij het opstarten van de SQL Server service. Bij docker performance problemen kan het zijn dat SQL server nog niet klaar is wanneer de databases aangemaakt worden. Als dit het geval is, dan vallen de Support en Shipping services eruit. Om dit op te lossen moeten de databases los aangemaakt worden. Ook kan het zijn dat services uitvallen wanneer RabbitMQ traag opstart. 
#### 1. Connect met de SQL server instance die op localhost,1434 draait
#### 2. Zoek de .sql bestanden in de folder 'Database' folder in het project
#### 3. Run de create-sql scripts en populate-db scripts
#### 4. Herstart alle services die offline zijn