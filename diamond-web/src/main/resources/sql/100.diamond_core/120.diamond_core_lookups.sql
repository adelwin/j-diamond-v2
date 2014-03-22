/**
 *	Table Name: DIAMOND_CORE_LOOKUPS
 */
DROP TABLE DIAMOND_CORE_LOOKUPS;

CREATE TABLE DIAMOND_CORE_LOOKUPS (
	lookup_id		VARCHAR(32)	NOT NULL,
	lookup_type		VARCHAR(255)NOT NULL,
	lookup_code		VARCHAR(255)NOT NULL,
	lookup_value	VARCHAR(255)NOT NULL,
	is_default		VARCHAR(32)	,
	sequence_no		NUMERIC		,
	attr1			VARCHAR(255),
	attr2			VARCHAR(255),
	attr3			VARCHAR(255),
	attr4			VARCHAR(255),
	create_by		VARCHAR(32)	NOT NULL,
	create_date		DATETIME	NOT NULL,
	update_by		VARCHAR(32)	,
	update_date		DATETIME	,
	status			VARCHAR(32)	NOT NULL,
	PRIMARY KEY  (lookup_id)
);
