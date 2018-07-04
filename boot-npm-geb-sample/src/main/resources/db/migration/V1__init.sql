CREATE TABLE INQUIRY_DATA
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    lastname VARCHAR(20) NOT NULL,
    firstname VARCHAR(20) NOT NULL,
    lastkana VARCHAR(20) NOT NULL,
    firstkana VARCHAR(20) NOT NULL,
    sex VARCHAR(1) NOT NULL,
    age INT NOT NULL,
    job VARCHAR(1),
    zipcode1 VARCHAR(3) NOT NULL,
    zipcode2 VARCHAR(4) NOT NULL,
    address VARCHAR(256) NOT NULL,
    tel1 VARCHAR(5),
    tel2 VARCHAR(4),
    tel3 VARCHAR(4),
    email VARCHAR(256),
    type1 VARCHAR(1) NOT NULL,
    type2 VARCHAR(1),
    inquiry TEXT NOT NULL,
    survey VARCHAR(1),
    update_date DATETIME NOT NULL
);

CREATE TABLE SURVEY_OPTIONS
(
  group_name VARCHAR(16) NOT NULL,
  item_value VARCHAR(1) NOT NULL,
  item_name VARCHAR(64) NOT NULL,
  item_order INT NOT NULL,
  CONSTRAINT PK_SURVEY_OPTIONS PRIMARY KEY (group_name, item_value)
);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '1', '選択肢１だけ長くしてみる', 1);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '2', '選択肢２', 2);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '3', '選択肢３', 3);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '4', '選択肢４', 4);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '5', '選択肢５が少し長い', 5);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '6', '選択肢６', 6);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '7', '選択肢７', 7);
INSERT INTO SURVEY_OPTIONS VALUES ('survey', '8', '８', 8);
