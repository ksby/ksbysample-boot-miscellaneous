create table employee (
    id int(11) not null auto_increment,
    name varchar(128) not null,
    primary key (id)
);
insert into employee (name) values ('tanaka taro');
insert into employee (name) values ('suzuki hanako');
