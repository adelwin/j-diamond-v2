/**
 *	Table Name: DIAMOND_CORE_USERS
 */
DROP TABLE DIAMOND_CORE_USERS;

CREATE TABLE DIAMOND_CORE_USERS (
	user_id			VARCHAR(32)	NOT NULL,
	user_code		VARCHAR(32)	NOT NULL,
	user_name		VARCHAR(255)NOT NULL,
	user_role_id	VARCHAR(32)	NOT NULL,
	supervisor_id	VARCHAR(32)	,
	user_status		VARCHAR(32)	NOT NULL,
	attr1			VARCHAR(255),
	attr2			VARCHAR(255),
	attr3			VARCHAR(255),
	attr4			VARCHAR(255),
	create_by		VARCHAR(32)	NOT NULL,
	create_Date		DATETIME	NOT NULL,
	update_by		VARCHAR(32)	,
	update_date		DATETIME	,
	status			VARCHAR(32)	NOT NULL,
	PRIMARY KEY		(user_id)
);
