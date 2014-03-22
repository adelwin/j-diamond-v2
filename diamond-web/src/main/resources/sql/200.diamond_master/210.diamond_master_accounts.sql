/**
 *	Table Name: DIAMOND_CORE_LOOKUPS
 */
DROP TABLE DIAMOND_MASTER_ACCOUNTS

CREATE TABLE DIAMOND_MASTER_ACCOUNTS (
	account_id					VARCHAR(32)		NOT NULL,
	account_name				VARCHAR(255)	NOT NULL,
	account_description	VARCHAR(1024)	NOT NULL,
	account_currency		VARCHAR(32)		NOT NULL,
	account_status			VARCHAR(32)		NOT NULL,
	attr1								VARCHAR(255),
	attr2								VARCHAR(255),
	attr3								VARCHAR(255),
	attr4								VARCHAR(255),
	create_by						VARCHAR(32)		NOT NULL,
	create_date					DATETIME			NOT NULL,
	update_by						VARCHAR(32)	,
	update_date					DATETIME	,
	status							VARCHAR(32)		NOT NULL,
	PRIMARY KEY					(account_id)
);
