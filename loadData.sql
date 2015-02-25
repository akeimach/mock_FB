--USER INFORMATION
--location
CREATE SEQUENCE LOC_SEQUENCE
START WITH 1
INCREMENT BY 1;
CREATE TRIGGER LOC_TRIGGER
BEFORE INSERT ON LOCATION
FOR EACH ROW
BEGIN
    SELECT LOC_SEQUENCE.NEXTVAL INTO :new.LOC_ID from dual;
END;
/
INSERT INTO LOCATION (CITY, STATE, COUNTRY)
SELECT DISTINCT HOMETOWN_CITY, HOMETOWN_STATE, HOMETOWN_COUNTRY FROM SHRAVYAK.PUBLIC_USER_INFORMATION
UNION 
SELECT DISTINCT CURRENT_CITY, CURRENT_STATE, CURRENT_COUNTRY FROM SHRAVYAK.PUBLIC_USER_INFORMATION
UNION 
SELECT DISTINCT EVENT_CITY, EVENT_STATE, EVENT_COUNTRY FROM SHRAVYAK.PUBLIC_EVENT_INFORMATION;
--profile
INSERT INTO PROFILE (USER_ID, FIRST_NAME, LAST_NAME, YEAR_OF_BIRTH, MONTH_OF_BIRTH, DAY_OF_BIRTH, GENDER, HOMETOWN_LOC_ID, CURRENT_LOC_ID)
SELECT DISTINCT LOAD_USER.USER_ID, LOAD_USER.FIRST_NAME, LOAD_USER.LAST_NAME, LOAD_USER.YEAR_OF_BIRTH, LOAD_USER.MONTH_OF_BIRTH, LOAD_USER.DAY_OF_BIRTH, LOAD_USER.GENDER, HOME.LOC_ID, CURR.LOC_ID FROM SHRAVYAK.PUBLIC_USER_INFORMATION LOAD_USER, LOCATION HOME, LOCATION CURR WHERE LOAD_USER.HOMETOWN_CITY = HOME.CITY AND LOAD_USER.HOMETOWN_STATE = HOME.STATE AND LOAD_USER.HOMETOWN_COUNTRY = HOME.COUNTRY AND LOAD_USER.CURRENT_CITY = CURR.CITY AND LOAD_USER.CURRENT_STATE = CURR.STATE AND LOAD_USER.CURRENT_COUNTRY = CURR.COUNTRY;
--college
CREATE SEQUENCE PROG_SEQUENCE
START WITH 1
INCREMENT BY 1;
CREATE TRIGGER PROG_TRIGGER
BEFORE INSERT ON COLLEGE
FOR EACH ROW
BEGIN
    SELECT PROG_SEQUENCE.NEXTVAL INTO :new.PROG_ID from dual;
END;
/
INSERT INTO COLLEGE (INSTITUTION_NAME, PROG_YEAR, PROG_CONCENTRATION, PROG_DEGREE)
SELECT DISTINCT INSTITUTION_NAME, PROGRAM_YEAR, PROGRAM_CONCENTRATION, PROGRAM_DEGREE FROM SHRAVYAK.PUBLIC_USER_INFORMATION;
--user college
INSERT INTO USER_COLLEGE (USER_ID, PROG_ID)
SELECT DISTINCT LOAD_USER.USER_ID, LOAD_EDU.PROG_ID FROM SHRAVYAK.PUBLIC_USER_INFORMATION LOAD_USER, COLLEGE LOAD_EDU WHERE LOAD_USER.INSTITUTION_NAME = LOAD_EDU.INSTITUTION_NAME AND LOAD_USER.PROGRAM_YEAR = LOAD_EDU.PROG_YEAR AND LOAD_USER.PROGRAM_CONCENTRATION = LOAD_EDU.PROG_CONCENTRATION AND LOAD_USER.PROGRAM_DEGREE = LOAD_EDU.PROG_DEGREE;
--user friends
INSERT INTO USER_FRIENDS (USER1_ID, USER2_ID)
SELECT DISTINCT USER1_ID, USER2_ID FROM SHRAVYAK.PUBLIC_ARE_FRIENDS;
--no user messages

--PHOTOS
--photo
INSERT INTO PHOTO (PHOTO_ID, ALBUM_ID, PHOTO_CAPTION, PHOTO_CREATED_TIME, PHOTO_MODIFIED_TIME, PHOTO_LINK)
SELECT DISTINCT PHOTO_ID, ALBUM_ID, PHOTO_CAPTION, PHOTO_CREATED_TIME, PHOTO_MODIFIED_TIME, PHOTO_LINK FROM SHRAVYAK.PUBLIC_PHOTO_INFORMATION;
--album
INSERT INTO ALBUM (ALBUM_ID, OWNER_ID, COVER_PHOTO_ID, ALBUM_NAME, ALBUM_CREATED_TIME, ALBUM_MODIFIED_TIME, ALBUM_LINK, ALBUM_VISIBILITY)
SELECT DISTINCT ALBUM_ID, OWNER_ID, COVER_PHOTO_ID, ALBUM_NAME, ALBUM_CREATED_TIME, ALBUM_MODIFIED_TIME, ALBUM_LINK, ALBUM_VISIBILITY FROM SHRAVYAK.PUBLIC_PHOTO_INFORMATION;
--photo tag
INSERT INTO PHOTO_TAG (PHOTO_ID, TAG_SUBJECT_ID, TAG_X_COORDINATE, TAG_Y_COORDINATE)
SELECT DISTINCT PHOTO_ID, TAG_SUBJECT_ID, TAG_X_COORDINATE, TAG_Y_COORDINATE FROM SHRAVYAK.PUBLIC_TAG_INFORMATION;
--no photo comments

--EVENTS
--event
INSERT INTO EVENT (EVENT_ID, EVENT_CREATOR_ID, EVENT_NAME, EVENT_TAGLINE, EVENT_DESCRIPTION, EVENT_HOST, EVENT_TYPE, EVENT_SUBTYPE, EVENT_LOCATION, EVENT_LOC_ID, EVENT_START_TIME, EVENT_END_TIME)
SELECT DISTINCT LOAD.EVENT_ID, LOAD.EVENT_CREATOR_ID, LOAD.EVENT_NAME, LOAD.EVENT_TAGLINE, LOAD.EVENT_DESCRIPTION, LOAD.EVENT_HOST, LOAD.EVENT_TYPE, LOAD.EVENT_SUBTYPE, LOAD.EVENT_LOCATION, LOC.LOC_ID, LOAD.EVENT_START_TIME, LOAD.EVENT_END_TIME FROM SHRAVYAK.PUBLIC_EVENT_INFORMATION LOAD, LOCATION LOC WHERE LOAD.EVENT_CITY = LOC.CITY AND LOAD.EVENT_STATE = LOC.STATE AND LOAD.EVENT_COUNTRY = LOC.COUNTRY;
--no event participants

