create database ticket_system;
use ticket_system;

create table user (
    user_id int(8) NOT NULL primary key,
    user_name varchar(20) NOT NULL,
    password varchar(16),
    age int(3),
    sex varchar(2),
    isAdmin boolean
);

create table station (
    station_no int(6) NOT NULL primary key,
    station_name varchar(20) NOT NULL,
    station_rank varchar(3)
);


create table timetable (
    trip_no varchar(4) NOT NULL,
    station_no int(6),
    arrive_time Time,
    leave_time Time,
    distence int,
    primary key(trip_no, station_no),
    foreign key(station_no) references station(station_no)
);

create table ticket (
    ticket_no int(8) NOT NULL primary key,
    trip_no varchar(4) NOT NULL,
    ticket_date date NOT NULL,
    departure_station_no int(6) NOT NULL,
    terminal_station_no int(6) NOT NULL,
    total_count int,
    remaining_count int,
    foreign key(trip_no) references timetable(trip_no),
    foreign key(departure_station_no) references station(station_no),
    foreign key(terminal_station_no) references station(station_no)
);

create table purchasing (
    user_id int(8) NOT NULL,
    ticket_no int(8) NOT NULL,
    amount int default 0,
    primary key(user_id, ticket_no),
    foreign key(user_id) references user(user_id),
    foreign key(ticket_no) references ticket(ticket_no)
);


CREATE INDEX IX_ticket_trip_no ON ticket(trip_no);

-- 检查插入的数据是否已经存在,避免抛出异常给用户
DELIMITER ||
drop function if exists check_user_name ||
create function check_user_name (
    register_user_name varchar(20)
)
    returns INT
    READS SQL DATA
    BEGIN
        DECLARE isResult INT default 0;
        select count(*) from user where user_name = register_user_name into isResult;
        RETURN isResult;
    END ||
DELIMITER ;
-- test
select check_user_name("13349074");



-- 注册 输入用户信息，输出是否注册成功
DELIMITER ||
drop procedure if exists proc_register_user ||
create procedure proc_register_user(
    in register_user_name varchar(20),
    in register_nick_name varchar(20),
    in register_user_pwd  varchar(20),
    in register_user_age  int,
    in register_user_sex  varchar(3),
    out register_user_output boolean)
    BEGIN
        IF(check_user_name(register_user_name) > 0) THEN set register_user_output = false;
        ELSE
            insert into user values (
                register_user_name,
                register_nick_name,
                register_user_pwd,
                register_user_age,
                register_user_sex,
                0);
            IF(check_user_name(register_user_name) = 1) THEN set register_user_output = true; END IF;
            select * from user where user_name = register_user_name;
        END IF;
    END ||
DELIMITER ;


-- 插入或求改购买记录的存储过程
-- TN为临时变量，表示符合要求的票的个数
DELIMITER ||
drop procedure if exists proc_insert_pus ||
create procedure proc_insert_pus(
    in name varchar(20),
    in t_no int,
    in tamount int
)
BEGIN
    DECLARE TN int;
    select count(*) from purchasing
        where purchasing.ticket_no = t_no and purchasing.user_name = name into TN;
    IF TN = 0 THEN
        insert into purchasing values (name, t_no, tamount);
    ELSE
        update purchasing set amount = amount + tamount
            where purchasing.ticket_no = t_no and purchasing.user_name = name;
    END IF;

END ||
DELIMITER ;