create table employee (
    id int(11) not null auto_increment,
    name varchar(128) not null,
    age int(3) not null,
    sex enum('男', '女'),
    update_time datetime default current_timestamp,
    primary key (id)
);
insert into employee (name, age, sex) values ('田中　太郎', 20, '男');
insert into employee (name, age, sex) values ('鈴木　花子', 18, '女');
