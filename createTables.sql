--USER INFORMATION
CREATE TABLE LOCATION (
    LOC_ID              INTEGER,
    CITY                VARCHAR2(100),
    STATE               VARCHAR2(100),
    COUNTRY             VARCHAR2(100),
    PRIMARY KEY (LOC_ID),
    UNIQUE (CITY, STATE, COUNTRY)
);
CREATE TABLE PROFILE (
	USER_ID             VARCHAR2(100),
    FIRST_NAME          VARCHAR2(100) NOT NULL,
    LAST_NAME           VARCHAR2(100) NOT NULL,
    YEAR_OF_BIRTH       NUMBER(38),
    MONTH_OF_BIRTH      NUMBER(38),
    DAY_OF_BIRTH        NUMBER(38),
    GENDER              VARCHAR2(100),
    HOMETOWN_LOC_ID     INTEGER,
    CURRENT_LOC_ID      INTEGER,
	PRIMARY KEY (USER_ID),
    FOREIGN KEY (HOMETOWN_LOC_ID) REFERENCES LOCATION,
    FOREIGN KEY (CURRENT_LOC_ID) REFERENCES LOCATION
);
CREATE TABLE COLLEGE (
    PROG_ID             INTEGER,
    INSTITUTION_NAME    VARCHAR2(100),
    PROG_YEAR           NUMBER(38),
    PROG_CONCENTRATION  CHAR(100),
    PROG_DEGREE         VARCHAR2(100),
	PRIMARY KEY (PROG_ID),
    UNIQUE (INSTITUTION_NAME, PROG_YEAR, PROG_CONCENTRATION, PROG_DEGREE)
);
CREATE TABLE USER_COLLEGE (
    USER_ID             VARCHAR2(100),
    PROG_ID             INTEGER,
    PRIMARY KEY (USER_ID, PROG_ID),
    FOREIGN KEY (USER_ID) REFERENCES PROFILE,
    FOREIGN KEY (PROG_ID) REFERENCES COLLEGE
);
CREATE TABLE USER_FRIENDS (
    USER1_ID            VARCHAR2(100),
	USER2_ID    	    VARCHAR2(100),
	PRIMARY KEY (USER1_ID, USER2_ID), 
    FOREIGN KEY (USER1_ID) REFERENCES PROFILE,
    FOREIGN KEY (USER2_ID) REFERENCES PROFILE
);
CREATE TABLE USER_MESSAGE (
    SENDER_ID           VARCHAR2(100),
    RECEIVER_ID         VARCHAR2(100),
    SENT_TIME           TIMESTAMP(6),
    MESSAGE_CONTENT     VARCHAR(4000),
    PRIMARY KEY (SENDER_ID, RECEIVER_ID, SENT_TIME),
    FOREIGN KEY (SENDER_ID) REFERENCES PROFILE,
    FOREIGN KEY (RECEIVER_ID) REFERENCES PROFILE
);

--PHOTOS
CREATE TABLE PHOTO (
    PHOTO_ID			VARCHAR2(100),
    ALBUM_ID            VARCHAR2(100) NOT NULL,
    PHOTO_CAPTION		VARCHAR2(2000),
    PHOTO_CREATED_TIME	TIMESTAMP(6) NOT NULL,
    PHOTO_MODIFIED_TIME	TIMESTAMP(6) NOT NULL,
    PHOTO_LINK          VARCHAR2(2000) NOT NULL,
    PRIMARY KEY (PHOTO_ID)
);
CREATE TABLE ALBUM (
    ALBUM_ID			VARCHAR2(100),
    OWNER_ID			VARCHAR2(100) NOT NULL,
    COVER_PHOTO_ID      VARCHAR2(100) NOT NULL,
    ALBUM_NAME			VARCHAR2(100) NOT NULL,
    ALBUM_CREATED_TIME	TIMESTAMP(6) NOT NULL,
    ALBUM_MODIFIED_TIME	TIMESTAMP(6) NOT NULL,
    ALBUM_LINK		    VARCHAR2(2000) NOT NULL,
    ALBUM_VISIBILITY	VARCHAR2(100) NOT NULL,
    PRIMARY KEY (ALBUM_ID),
    FOREIGN KEY (OWNER_ID) REFERENCES PROFILE,
    FOREIGN KEY (COVER_PHOTO_ID) REFERENCES PHOTO
);
ALTER TABLE PHOTO ADD CONSTRAINT PHOTO_REF_ALBUM
    FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM
    INITIALLY DEFERRED DEFERRABLE;
CREATE TABLE PHOTO_TAG (
    PHOTO_ID			VARCHAR2(100),
    TAG_SUBJECT_ID 	    VARCHAR2(100),
    TAG_X_COORDINATE	NUMBER NOT NULL,
    TAG_Y_COORDINATE	NUMBER NOT NULL,
    PRIMARY KEY (PHOTO_ID, TAG_SUBJECT_ID),
    FOREIGN KEY (PHOTO_ID) REFERENCES PHOTO,
    FOREIGN KEY (TAG_SUBJECT_ID) REFERENCES PROFILE
);
CREATE TABLE PHOTO_COMMENTS (
    PHOTO_ID            VARCHAR2(100),
    COMMENTOR_ID        VARCHAR2(100),
    COMMENT_TIME        TIMESTAMP(6),
    COMMENT_TEXT        VARCHAR2(4000),
    PRIMARY KEY (PHOTO_ID, COMMENTOR_ID),
    FOREIGN KEY (PHOTO_ID) REFERENCES PHOTO,
    FOREIGN KEY (COMMENTOR_ID) REFERENCES PROFILE
);

--EVENTS
CREATE TABLE EVENT (
    EVENT_ID			VARCHAR2(100),
    EVENT_CREATOR_ID	VARCHAR2(100),
    EVENT_NAME			VARCHAR2(100) NOT NULL,
    EVENT_TAGLINE		VARCHAR2(1000),
    EVENT_DESCRIPTION	VARCHAR2(4000),
    EVENT_HOST			VARCHAR2(100) NOT NULL,
    EVENT_TYPE			VARCHAR2(100) NOT NULL,
    EVENT_SUBTYPE		VARCHAR2(100) NOT NULL,
    EVENT_LOCATION 		VARCHAR2(200),
    EVENT_LOC_ID   		INTEGER,
    EVENT_START_TIME	TIMESTAMP(6) NOT NULL,
    EVENT_END_TIME 		TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (EVENT_ID),
    FOREIGN KEY (EVENT_CREATOR_ID) REFERENCES PROFILE,
    FOREIGN KEY (EVENT_LOC_ID) REFERENCES LOCATION
);
CREATE TABLE EVENT_PARTICIPANTS (
    EVENT_ID            VARCHAR(100),
    PARTICIPANT_ID      VARCHAR(100),
    CONFIRMATION_STATUS VARCHAR(100),
    PRIMARY KEY (EVENT_ID, PARTICIPANT_ID),
    FOREIGN KEY (EVENT_ID) REFERENCES EVENT,
    FOREIGN KEY (PARTICIPANT_ID) REFERENCES PROFILE
);


