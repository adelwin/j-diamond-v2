/**
 *	Table Name: DIAMOND_CORE_USER_ROLES
 */
DROP TABLE DIAMOND_CORE_USER_ROLES;

CREATE TABLE DIAMOND_CORE_USER_ROLES (
	user_role_id	VARCHAR(32)	NOT NULL,
	user_role_name	VARCHAR(255)NOT NULL,
	login_level		VARCHAR(32)	NOT NULL,
	attr1			VARCHAR(255),
	attr2			VARCHAR(255),
	attr3			VARCHAR(255),
	attr4			VARCHAR(255),
	create_by		VARCHAR(32)	NOT NULL,
	create_date		DATETIME	NOT NULL,
	update_by		VARCHAR(32)	,
	update_date		DATETIME	,
	status			VARCHAR(32)	NOT NULL,
	PRIMARY KEY		(user_role_id)
);

