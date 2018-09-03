create table user_info_t(
id bigint(20) primary key auto_increment,
user_name varchar(50) not null unique,
real_name varchar(50) not null,
password varchar(50) not null,
permission varchar(100),
status int(2) default 0,
create_time date,
modify_time date
);

create table user_parser_record_t(
id bigint(20) primary key auto_increment,
user_sign varchar(50) not null,
file_name varchar(50) not null,
status int(2) default 0,
create_time date,
modify_time date
);

alter table html_parser_record_t add file_sign bigint(20) DEFAULT 0;

alter table html_parser_record_t modify keywords_map mediumtext;
