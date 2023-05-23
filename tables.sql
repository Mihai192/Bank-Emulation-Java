CREATE TABLE `User` (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(60) NOT NULL,
    email varchar(255) NOT NULL,
    pass varchar(60) NOT NULL,
	role int NOT NULL,
   	cnp varchar(16) NOT NULL,
    adresa varchar(255),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE BankAccount (
    id int NOT NULL AUTO_INCREMENT,
    balance int NOT NULL,
    name varchar(255) NOT NULL,
    iban varchar(60) NOT NULL,
    currency varchar(40) NOT NULL,
    userid int NOT NULL,
	creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE Card (
 	id int NOT NULL AUTO_INCREMENT,
    card_number VARCHAR(16) NOT NULL,
    cvc int NOT NULL,
    userid int NOT NULL,
	bankaccountid int,
    expiration_date TIMESTAMP,
	added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);



CREATE TABLE Beneficiary (
	id int NOT NULL AUTO_INCREMENT,
	self_id int NOT NULL,
	beneficiary_id int NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE Transfer (
	id int NOT NULL AUTO_INCREMENT,
	sender_id int NOT NULL,
	recipient_id int NOT NULL,
	currency VARCHAR(40) NOT NULL,
	amount int NOT NULL,
	action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);