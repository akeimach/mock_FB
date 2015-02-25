-bash: /afs/umich.edu/user/a/k/akeimach/Public/.profile: No such file or directory
-bash-4.1$ module load oracle/muscle
-bash-4.1$ sqlplus

SQL*Plus: Release 10.2.0.3.0 - Production on Sat Jan 24 22:43:26 2015

Copyright (c) 1982, 2006, Oracle.  All Rights Reserved.

Enter user-name: akeimach
Enter password: 
ERROR:
ORA-28001: the password has expired


Changing password for akeimach
New password: 
Retype new password: 
Password changed

Connected to:
Oracle Database 11g Enterprise Edition Release 11.2.0.4.0 - 64bit Production
With the Partitioning, OLAP, Data Mining and Real Application Testing options

SQL> CREATE TABLE USER_INFORMATION AS (SELECT * FROM SHRAVYAK.PUBLIC_USER_INFORMATION);

Table created.

SQL> CREATE TABLE ARE_FRIENDS AS (SELECT * FROM SHRAVYAK.PUBLIC_ARE_FRIENDS);

Table created.

SQL> DESC USER_INFORMATION;
 Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 USER_ID					    VARCHAR2(100)
 FIRST_NAME					    VARCHAR2(100)
 LAST_NAME					    VARCHAR2(100)
 YEAR_OF_BIRTH					    NUMBER(38)
 MONTH_OF_BIRTH 				    NUMBER(38)
 DAY_OF_BIRTH					    NUMBER(38)
 GENDER 					    VARCHAR2(100)
 CURRENT_CITY					    VARCHAR2(100)
 CURRENT_STATE					    VARCHAR2(100)
 CURRENT_COUNTRY				    VARCHAR2(100)
 HOMETOWN_CITY					    VARCHAR2(100)
 HOMETOWN_STATE 				    VARCHAR2(100)
 HOMETOWN_COUNTRY				    VARCHAR2(100)
 INSTITUTION_NAME				    VARCHAR2(100)
 PROGRAM_YEAR					    NUMBER(38)
 PROGRAM_CONCENTRATION				    CHAR(100)
 PROGRAM_DEGREE 				    VARCHAR2(100)

SQL> DESC ARE_FRIENDS
 Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 USER1_ID					    VARCHAR2(100)
 USER2_ID					    VARCHAR2(100)

SQL> CREATE TABLE PHOTO_INFORMATION AS (SELECT * FROM SHRAVYAK.PUBLIC_PHOTO_INFORMATION);

Table created.

SQL> DESC PHOTO_INFORMATION;
 Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 ALBUM_ID					    VARCHAR2(100)
 OWNER_ID					    VARCHAR2(100)
 COVER_PHOTO_ID 				    VARCHAR2(100)
 ALBUM_NAME					    VARCHAR2(100)
 ALBUM_CREATED_TIME				    TIMESTAMP(6)
 ALBUM_MODIFIED_TIME				    TIMESTAMP(6)
 ALBUM_LINK					    VARCHAR2(2000)
 ALBUM_VISIBILITY				    VARCHAR2(100)
 PHOTO_ID					    VARCHAR2(100)
 PHOTO_CAPTION					    VARCHAR2(2000)
 PHOTO_CREATED_TIME				    TIMESTAMP(6)
 PHOTO_MODIFIED_TIME				    TIMESTAMP(6)
 PHOTO_LINK					    VARCHAR2(2000)

SQL> CREATE TABLE TAG_INFORMATION AS (SELECT * FROM SHRAVYAK.TAG_INFORMATION);
CREATE TABLE TAG_INFORMATION AS (SELECT * FROM SHRAVYAK.TAG_INFORMATION)
                                                        *
ERROR at line 1:
ORA-00942: table or view does not exist


SQL> CREATE TABLE TAG_INFORMATION AS (SELECT * FROM SHRAVYAK.PUBLIC_TAG_INFORMATION);

Table created.

SQL> DESC TAG_INFORMATION;
 Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 PHOTO_ID					    VARCHAR2(100)
 TAG_SUBJECT_ID 				    VARCHAR2(100)
 TAG_CREATED_TIME				    TIMESTAMP(6)
 TAG_X_COORDINATE				    NUMBER
 TAG_Y_COORDINATE				    NUMBER

SQL> CREATE TABLE EVENT_INFORMATION AS (SELECT * FROM SHRAVYAK.PUBLIC_EVENT_INFORMATION)
  2  
SQL> CREATE TABLE EVENT_INFORMATION AS (SELECT * FROM SHRAVYAK.PUBLIC_EVENT_INFORMATION);

Table created.

SQL> DESC EVENT_INFORMATION;
 Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 EVENT_ID					    VARCHAR2(100)
 EVENT_CREATOR_ID				    VARCHAR2(100)
 EVENT_NAME					    VARCHAR2(100)
 EVENT_TAGLINE					    VARCHAR2(1000)
 EVENT_DESCRIPTION				    VARCHAR2(4000)
 EVENT_HOST					    VARCHAR2(100)
 EVENT_TYPE					    VARCHAR2(100)
 EVENT_SUBTYPE					    VARCHAR2(100)
 EVENT_LOCATION 				    VARCHAR2(200)
 EVENT_CITY					    VARCHAR2(100)
 EVENT_STATE					    VARCHAR2(100)
 EVENT_COUNTRY					    VARCHAR2(100)
 EVENT_START_TIME				    TIMESTAMP(6)
 EVENT_END_TIME 				    TIMESTAMP(6)

