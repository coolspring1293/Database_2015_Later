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




# mysqldump -u root -pcoolspring ticket_system > /Users/liuw53/Desktop/ticket_system_backup.sql

insert into station values
(000001, "北京", "CEN"), 
(000002, "山海关", "COM"), 
(000003, "沈阳", "COM"), 
(000004, "长春", "COM"), 
(000005, "哈尔滨", "TER"), 
(000006, "石家庄", "COM"), 
(000007, "郑州", "EXC"), 
(000008, "武汉", "COM"), 
(000009, "长沙", "COM"), 
(000010, "株洲", "EXC"), 
(000011, "广州", "COM"), 
(000012, "深圳", "TER"), 
(000013, "天津", "COM"), 
(000014, "济南", "COM"), 
(000015, "徐州", "EXC"), 
(000016, "南京", "COM"), 
(000017, "上海", "TER"), 
(000018, "包头", "COM"), 
(000019, "兰州", "EXC"), 
(000020, "西宁", "COM"), 
(000021, "喇萨", "TER"), 
(000022, "乌鲁木齐", "TER"), 
(000023, "连云港", "TER"), 
(000024, "西安", "COM"), 
(000025, "宝鸡", "EXC"), 
(000026, "成都", "COM"), 
(000027, "昆明", "TER"), 
(000028, "杭州", "COM"), 
(000029, "贵阳", "COM");

insert into user values
(13349074, "liuw53", "1234",  21,  "M", 1),
(13349073, "liusy7", "1234" , 20,  "M", 0),
(13349134, "xiezhh3", "1234", 21,  "M", 0),
(13349076, "liuzh26", "1234", 20,  "M", 0);

insert into timetable values
("G381", 000001, NULL, '7:53', 0), 
("G381", 000013, '8:38', '8:40', 136), 
("G381", 000002, '9:51', '9:53', 315), 
("G381", 000003, '12:25', '12:28', 703), 
("G381", 000004, '13:40', '13:43', 1003), 
("G381", 000005, '14:59', NULL, 1249);

insert into ticket values
(1, 'G381', '2016-01-01', 000001, 000013, 100, 100), 
(2, 'G381', '2016-01-01', 000001, 000002, 100, 100), 
(3, 'G381', '2016-01-01', 000001, 000003, 100, 100), 
(4, 'G381', '2016-01-01', 000001, 000004, 100, 100), 
(5, 'G381', '2016-01-01', 000001, 000005, 100, 100), 
(6, 'G381', '2016-01-01', 000002, 000005, 100, 100);
//表示车票编号为6，G381次列车，从山海关(000002)到哈尔滨(000005),共100张票,余票100张

select * from ticket where departure_station_no = 1 and terminal_station_no = 5;
select * from timetable where trip_no = 'G381' order by count(leave_time) desc; 

ALTER TABLE timetable RENAME distence column TO distance;

# 需要完成以下几个Transactions
# 0.用户注册：输入用户名和密码，
-- 用户名注意查重，成功添加到User表中，id就是添加的次序，在外部java代码中实现
-- 用户自己查询买了哪些票<车票信息，张数>
# 1.买票：
-- 输入<用户名，Ticket唯一确定编号，张数> 
-- 输出<是否买票成功>
-- 功能：remaining票数目对应减少，不能小于零张
-- 买票结果存在purchasing表中
-- 自定义错误代码：没有足够的票，冲突等；
# 2.车次查询
-- 输入<车次编号>，查询<中途站，到站时间，发车时间，停靠时间，票价（距离*price per km）>等,添加“买票”接口（外部代码）
-- 输出增加<站序列>，按照距离排序即可
# 3.车站查询
-- 输入始发站、终点站（编号*）,[车次（可以为空）],输出之间的全部车次信息
-- 买票接口，直接可以返回ticket的编号ticket_no
-- *可以从外部java代码解决站名->编号的映射
# 4.管理员修改票数目
-- 检查用户是否是管理员用户
-- 查询【同上】
-- 可以修改总共票数，不能小于卖出的票数
-- 输出修改是否成功
# 5.超级管理员
-- 程序员自己-直接修改数据库-怎么改就怎么改

-- 用户名查重
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
select check_user_name("liuw53");

-- 注册
DELIMITER ||
drop procedure if exists proc_register_user ||
create procedure proc_register_user(
    in register_user_id   int,
    in register_user_name varchar(20),
    in register_user_pwd  varchar(10),
    in register_user_age  int,
    in register_user_sex  varchar(3),
    out register_user_output boolean)
    BEGIN
        IF(check_user_name(register_user_name) > 0) THEN set register_user_output = false;
        ELSE
            insert into user values (
                register_user_id,
                register_user_name,
                register_user_pwd,  
                register_user_age,  
                register_user_sex, 
                0);
            IF(check_user_name(register_user_name) = 1) THEN set register_user_output = true; END IF;
            select * from user where user_id = register_user_id;
        END IF;

    END ||

DELIMITER ;

set @isReg = false;
call proc_register_user(13349110, 'wangk9', 'wangke', 19, 'F', @isReg);
select @isReg;
set @isReg2 = false;
call proc_register_user(13349069, 'liuhm', 'liuhaimei', 20, 'F', @isReg2);
select @isReg2;


