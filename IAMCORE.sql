CREATE SCHEMA IAMCORE;

SET SCHEMA IAMCORE;

CREATE TABLE IDENTITIES (
		UID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		EMAIL VARCHAR(50),
		NAME VARCHAR(50)
	);
	
INSERT INTO IDENTITIES(NAME,EMAIL) VALUES ('a','b');