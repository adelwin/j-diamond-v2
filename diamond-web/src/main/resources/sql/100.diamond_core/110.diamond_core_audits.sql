/**
 *	Table Name: DIAMOND_CORE_AUDITS
 */
DROP TABLE DIAMOND_CORE_AUDITS;

CREATE TABLE DIAMOND_CORE_AUDITS (
	audit_id		VARCHAR(32)	NOT NULL,
	audit_type		VARCHAR(32)	NOT NULL,
	audit_pk		VARCHAR(32)	NOT NULL,
	auditor			VARCHAR(32)	NOT NULL,
	audit_date		DATETIME	NOT NULL,
	before_value	VARCHAR(999),
	table_name		VARCHAR(255)NOT NULL,
	PRIMARY KEY		(audit_id)
);

