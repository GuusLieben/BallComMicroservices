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
