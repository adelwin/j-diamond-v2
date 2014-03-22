/**
 *	Table Name: DIAMOND_CORE_MAILS
 */
DROP TABLE DIAMOND_CORE_MAILS;

CREATE TABLE DIAMOND_CORE_MAILS (
	mail_id				VARCHAR(32)	NOT NULL,
	mail_address		VARCHAR(255)NOT NULL,
	user_id				VARCHAR(32) NOT NULL,
	attr1				VARCHAR(255),
	attr2				VARCHAR(255),
	attr3				VARCHAR(255),
	attr4				VARCHAR(255),
	create_by			VARCHAR(32)	NOT NULL,
	create_Date			DATETIME	NOT NULL,
	update_by			VARCHAR(32)	,
	update_date			DATETIME	,
	status				VARCHAR(32)	NOT NULL,
	PRIMARY KEY			(mail_id)
);
