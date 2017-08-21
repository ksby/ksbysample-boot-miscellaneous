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
    survey VARCHAR(1) NOT NULL,
    update_date DATETIME NOT NULL
);
