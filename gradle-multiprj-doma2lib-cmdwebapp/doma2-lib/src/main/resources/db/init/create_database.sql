create database if not exists sampledb character set utf8mb4 collate utf8mb4_0900_as_cs;

create user 'sampledb_user'@'%' identified by 'xxxxxxxx';
grant all privileges ON sampledb.* to 'sampledb_user'@'%' with grant option;
flush privileges;
