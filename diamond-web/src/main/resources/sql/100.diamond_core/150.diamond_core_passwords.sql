/**
 *	Table Name: DIAMOND_CORE_PASSWORDS
 */
DROP TABLE DIAMOND_CORE_PASSWORDS;

CREATE TABLE DIAMOND_CORE_PASSWORDS (
	password_id			VARCHAR(32)	NOT NULL,
	password_value		VARCHAR(255)NOT NULL,
	password_salt		VARCHAR(255)NOT NULL,
	login_attempt		NUMERIC		NOT NULL,
	user_id				VARCHAR(32)	NOT NULL,
	password_status		VARCHAR(32)	NOT NULL,
	attr1				VARCHAR(255),
	attr2				VARCHAR(255),
	attr3				VARCHAR(255),
	attr4				VARCHAR(255),
	create_by			VARCHAR(32)	NOT NULL,
	create_Date			DATETIME	NOT NULL,
	update_by			VARCHAR(32)	,
	update_date			DATETIME	,
	status				VARCHAR(32)	NOT NULL,
	PRIMARY KEY			(password_id)
);
