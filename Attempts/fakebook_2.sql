
CREATE TABLE Profile (
	USER_ID	            INTEGER UNIQUE,
	FIRST_NAME          CHAR(30) NOT NULL,
	LAST_NAME           CHAR(30) NOT NULL,
	YEAR_OF_BIRTH       INTEGER,
	MONTH_OF_BIRTH      INTEGER,
	DAY_OF_BIRTH	    INTEGER,
	GENDER              CHAR(30),
	PRIMARY KEY (USER_ID)
);

CREATE TABLE Friends (
    USER1_ID            INTEGER UNIQUE,
	USER2_ID    	    INTEGER UNIQUE,
	PRIMARY KEY (USER1_ID, USER2_ID), 
    FOREIGN KEY (USER1_ID) REFERENCES Profile,
);

CREATE TABLE Location (
    CITY                CHAR(30),
    STATE               CHAR(30),
    COUNTRY             CHAR(30),
    PRIMARY KEY (CITY)
);

CREATE TABLE Current_Location (
    USER_ID            INTEGER UNIQUE,
    CURRENT_CITY       CHAR(30),
    PRIMARY KEY (USER_ID, CURRENT_CITY),
    FOREIGN KEY (USER_ID) REFERENCES Profile,
    FOREIGN KEY (CURRENT_CITY) REFERENCES Location
);

CREATE TABLE Hometown_Location (
    USER_ID             INTEGER UNIQUE,
    HOMETOWN_CITY       CHAR(30),
    FOREIGN KEY (USER_ID) REFERENCES Profile,
    FOREIGN KEY (HOMETOWN_CITY) REFERENCES Location
);

CREATE TABLE College (
	INSTITUTION_NAME	CHAR(30),
	PROGRAM_YEAR    	INTEGER,
	PROGRAM_CONCENTRATION   CHAR(30),
	PROGRAM_DEGREE	    CHAR(30),
	PRIMARY KEY (INSTITUTION_NAME)
);

CREATE TABLE Attended_College (
    USER_ID             INTEGER,
    INSTITUTION_NAME    CHAR(30),
    PRIMARY KEY (USER_ID, INSTITUTION_NAME),
    FOREIGN KEY (USER_ID) REFERENCES Profile
    FOREIGN KEY (INSTITUTION_NAME) REFERENCES College,
);





CREATE TABLE Photo (
	PHOTO_ID        	INTEGER UNIQUE,
	PHOTO_CAPTION   	CHAR(30),
	PHOTO_CREATED_TIME	INTEGER NOT NULL,
	PHOTO_MODIFIED_TIME	INTEGER NOT NULL,
	PHOTO_LINK      	INTEGER UNIQUE,
	PRIMARY KEY(PHOTO_ID)
);

CREATE TABLE Photo_Tags (
	PHOTO_ID        	INTEGER UNIQUE,
	TAG_SUBJECT_ID  	INTEGER UNIQUE,
	TAG_X_COORDINATE	INTEGER NOT NULL,
	TAG_Y_COORDINATE	INTEGER NOT NULL,
	PRIMARY KEY (PHOTO_ID),
    FOREIGN KEY (PHOTO_ID) REFERENCES Photo,
    FOREIGN KEY (TAG_SUBJECT_ID) REFERENCES Profile
);

CREATE TABLE Album (
	ALBUM_ID        	INTEGER UNIQUE,
	OWNER_ID        	INTEGER UNIQUE,
	COVER_PHOTO_ID  	INTEGER UNIQUE,
	ALBUM_NAME      	CHAR(30) NOT NULL,
	ALBUM_CREATED_TIME	INTEGER NOT NULL,
	ALBUM_MODIFIED_TIME	INTEGER NOT NULL,
	ALBUM_LINK      	INTEGER UNIQUE,
	ALBUM_VISIBILITY	CHAR(30) NOT NULL,
	PRIMARY KEY (ALBUM_ID),
    FOREIGN KEY (OWNER_ID) REFERENCES Profile,
    FOREIGN KEY (COVER_PHOTO_ID) REFERENCES Photo
);

CREATE TABLE Album_Contains (
    PHOTO_ID            INTEGER UNIQUE,
    ALBUM_ID            INTEGER UNIQUE,
    PRIMARY KEY (PHOTO_ID, ALBUM_ID),
    FOREIGN KEY (PHOTO_ID) REFERENCES Photo,
    FOREIGN KEY (ALBUM_ID) REFERENCES Album,
);

CREATE TABLE Photo_Comment (
    PHOTO_ID            INTEGER UNIQUE,
    COMMENTOR_ID        INTEGER UNIQUE,
    PRIMARY KEY (PHOTO_ID, COMMENTOR_ID),
    FOREIGN KEY (PHOTO_ID) REFERENCES Photo,
    FOREIGN KEY (COMMENTOR_ID) REFERENCES Profile
);




CREATE TABLE Event (
	EVENT_ID        	INTEGER UNIQUE,
	EVENT_CREATOR_ID   	INTEGER UNIQUE,
	EVENT_NAME      	CHAR(30) NOT NULL,
	EVENT_TAGLINE       CHAR(30),
	EVENT_DESCRIPTION	CHAR(100),
	EVENT_HOST      	INTEGER NOT NULL,
	EVENT_TYPE	        INTEGER NOT NULL,
	EVENT_SUBTYPE   	INTEGER NOT NULL,
	EVENT_LOCATION  	CHAR(30),
	EVENT_CITY      	CHAR(30),
	EVENT_STATE     	CHAR(30),
	EVENT_COUNTRY   	CHAR(30),
	EVENT_START_TIME	INTEGER NOT NULL,
	EVENT_END_TIME  	INTEGER NOT NULL,
	PRIMARY KEY (EVENT_ID),
    FOREIGN KEY (EVENT_CREATOR_ID) REFERENCES Profile,
    FOREIGN KEY (EVENT_CITY) REFERENCES Location
);
;
;
;
;