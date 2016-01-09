create database ticket_system;
use ticket_system;

create table user (
    user_name varchar(20) NOT NULL primary key,
    nick_name varchar(20) NOT NULL, 
    password varchar(20),
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
    distance int,
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
    user_name varchar(20) NOT NULL,
    ticket_no int(8) NOT NULL,
    amount int default 0,
    primary key(user_name, ticket_no),
    foreign key(user_name) references user(user_name),
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
("13349074", "liuw53", "1234",  21,  "M", 1),
("13349073", "liusy7", "1234" , 20,  "M", 0),
("13349134", "xiezhh3", "1234", 21,  "M", 0),
("13349076", "liuzh26", "1234", 20,  "M", 0);

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

insert into timetable values
("D1", 000001, NULL, '18:08', 0), 
("D1", 000003, '23:20', NULL, 703);

insert into ticket values
(7, 'D1', '2016-01-01', 000001, 000003, 50, 50);

-- 表示车票编号为6，G381次列车，从山海关(000002)到哈尔滨(000005),共100张票,余票100张

-- select * from ticket where departure_station_no = 1 and terminal_station_no = 5;
-- select * from timetable where trip_no = 'G381' order by count(leave_time) desc; 

-- ALTER TABLE timetable RENAME distence column TO distance;

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

-- 用户名查重:避免报错
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



-- 注册
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

set @isReg = false;
call proc_register_user("13349110", 'wangk47', 'wangke', 19, 'F', @isReg);
select @isReg;
set @isReg2 = false;
call proc_register_user("13349069", 'liuhm', 'liuhaimei', 20, 'F', @isReg2);
select @isReg2;
----------------------------------------------------------------------------------------
-- NEW AND 
----------------------------------------------------------------------------------------
-- Log in input user_name, password 登陆，输入用户名和密码，输出一个boolean值
DELIMITER ||
drop function if exists log_in_by_user_name_psw ||
create function log_in_by_user_name_psw (
    tmp_user_name varchar(20),
    tmp_password  varchar(20)
)
    returns int
    READS SQL DATA
    BEGIN
        DECLARE isResult int default false;
        select count(*) from user where tmp_user_name = user.user_name and tmp_password = user.password into isResult;
        RETURN isResult;
    END ||
DELIMITER ;


-- just a test
select log_in_by_user_name_psw('13349074', '1234');



-- 修改用户数据，如果用户有一项没有修改的话，如sex = '', 则不修改；
-- 输入用户名，旧密码，新密码，新昵称，新年龄，新性别
DELIMITER ||
drop procedure if exists proc_change_user_info ||
create procedure proc_change_user_info(
    in  chp_user_name    varchar(20),
    in  old_pwd          varchar(20),
    in  new_pwd          varchar(20),
    in  new_nick_name    varchar(20),
    in  new_age          int(3),
    in  new_sex          varchar(3),
    out is_changed       boolean   )
    BEGIN
        DECLARE isExist INT default 0;
        set is_changed = false;
        select count(*) from user where user_name = chp_user_name and password = old_pwd into isExist;
        IF(isExist > 0) THEN
            update user set password = new_pwd where user.user_name = chp_user_name and password = old_pwd;

            -- IF(new_nick_name <> NULL) THEN
                 update user set nick_name = new_nick_name where user.user_name = chp_user_name;
            -- END IF;
            -- IF(new_age <> NULL) THEN
                 update user set age = new_age where user.user_name = chp_user_name;
            -- END IF;
            -- IF(new_sex <> NULL) THEN
                 update user set sex = new_sex where user.user_name = chp_user_name;
            -- END IF;
            set is_changed = true;
        END IF;
    END ||
DELIMITER ;



set @isCha = false;

call proc_change_user_info (
    '13349074',
    '12345',
    '12345',
    'liuw53',
    22,
    'M',
    @isCha
);


-----------------------------------------------------------------------

------------------------------------------------------------------------

select @isFD, @tmp_id;

-- 修改全部的用户信息


-- 删除用户信息

-- 对于管理员账户，
DELIMITER ||
drop procedure if exists proc_delete_user ||
create procedure proc_delete_user(
    in  delete_user_name varchar(20),
    out is_delete        boolean   )
    BEGIN
        DECLARE isE int default 0;
        set is_delete = false;
        select count(*) from user where user_name = delete_user_name into isE;
        IF(isE > 0) THEN 
            delete from user where delete_user_name = user_name;
            set is_delete = true;
        END IF;
    END ||
DELIMITER ;

call proc_register_user("13349069", 'liuhm', 'liuhaimei', 20, 'F', @isReg2);
call proc_delete_user('13349069', @idFD);

/*
-- 用户修改密码; 返回值有个小bug， 两次输入同样的数据会返回错误，因为检查的是后来的数量的变化
DELIMITER ||
drop procedure if exists proc_change_pwd ||
create procedure proc_change_pwd(
    in  chp_user_name varchar(20),
    in  old_pwd          varchar(16),
    in  new_pwd          varchar(16),
    out is_changed       boolean   )
    BEGIN
        DECLARE id INT default 0;
        DECLARE ct INT default 0;
        DECLARE isF boolean default false;
        call proc_get_user_id(chp_user_name, id, isF);
        IF(isF = true) THEN 
            update user set password = new_pwd where user.user_id = id and password = old_pwd;
            select count(*) from user where user_name = chp_user_name and password = new_pwd into ct;
            IF(ct = 1) THEN set is_changed = true;
            END IF;
        END IF;
    END ||
DELIMITER ;
*/


call proc_change_pwd('liusy7', '1234', 'dsaf', @idFD);

--用户查询已经购买
--------------------------------------
select * from purchasing;

-- 查询车的时刻表
drop view if exists view_trip_timetable;
create view view_trip_timetable(trip_no, station_name, arrive_time, leave_time, distance, price) as
    select trip_no, station_name, arrive_time, leave_time, distance, distance*0.35 as price, timediff(leave_time, arrive_time) as stop_time
    from timetable, station 
    where trip_no = 'G381' and timetable.station_no = station.station_no
    order by arrive_time  asc;



-- 小函数：返回站名
DELIMITER ||
drop function if exists return_station_name ||
create function return_station_name (
    tmp_station_no INT 
)
    returns varchar(20)
    READS SQL DATA
    BEGIN
        DECLARE isResult varchar(20) default "Not Exist";
        select station.station_name from station where station.station_no =  tmp_station_no into isResult;
        RETURN isResult;
    END ||
DELIMITER ;



drop view if exists view_double_station_timetable_beijing;
create view view_double_station_timetable_beijing
    (trip_no, station_no, arrive_time, leave_time, distance) as
    select trip_no, station_no, arrive_time, leave_time, distance from timetable 
    where station_no = 1;

create view exists view_double_station_timetable_shenyang
    (trip_no, station_no, arrive_time, leave_time, distance) as
    select trip_no, station_no, arrive_time, leave_time, distance from timetable 
    where station_no = 3;

select 
return_station_name(t1.station_no) as st, 
t1.leave_time as st_time, 
return_station_name(t2.station_no) as ed, 
t2.arrive_time as ed_time, 
timediff(t2.arrive_time, t1.leave_time) as diff_time , 
(t2.distance - t1.distance) * 0.35 as price
    from 
    view_double_station_timetable_beijing t1, 
    view_double_station_timetable_shenyang t2
    where t1.trip_no = t2.trip_no;


        drop view if exists view_double_station_timetable_3;
        create view view_double_station_timetable_3
               (trip_no, station_no, arrive_time, leave_time, distance) as
                    select trip_no, station_no, arrive_time, leave_time, distance from timetable
                   where station_no = 3;

-----------------------


drop view if exists view_double_station_timetable_beijing;
create view view_double_station_timetable_beijing
    (trip_no, station_no, arrive_time, leave_time, distance) as
    

create view exists view_double_station_timetable_shenyang
    (trip_no, station_no, arrive_time, leave_time, distance) as
    

select 
t1.trip_no as trip_number,
return_station_name(t1.station_no) as st, 
t1.leave_time as st_time, 
return_station_name(t2.station_no) as ed, 
t2.arrive_time as ed_time, 
timediff(t2.arrive_time, t1.leave_time) as diff_time , 
(t2.distance - t1.distance) * 0.35 as price
    from 
    (select trip_no, station_no, arrive_time, leave_time, distance from timetable 
    where station_no = 1) t1, 
    (select trip_no, station_no, arrive_time, leave_time, distance from timetable 
    where station_no = 1) t2
    where t1.trip_no = t2.trip_no and (t2.distance - t1.distance) > 0;


        drop view if exists view_double_station_timetable_3;
        create view view_double_station_timetable_3
               (trip_no, station_no, arrive_time, leave_time, distance) as
                    select trip_no, station_no, arrive_time, leave_time, distance from timetable
                   where station_no = 3 and (t2.distance - t1.distance);




select total_count from ticket 
where trip_no = 'G381' 
and ticket_date = '2016-01-01' 
and departure_station_no = 1 
and terminal_station_no = 3;



DELIMITER ||
drop procedure if exists proc_prushing ||
create procedure proc_prushing(
    in name varchar(20),
    in no   varchar(4), 
    in tdate date,
    in departure int, 
    in terminal int, 
    in tamount int,
    out isP boolean
)
BEGIN
    DECLARE TM int;
    DECLARE DC int;
    select remaining_count from ticket 
    where ticket.trip_no = no
    and ticket.ticket_date = tdate
    and ticket.departure_station_no = departure 
    and ticket.terminal_station_no = terminal
    into TM;
    IF TM < tamount THEN
        select TM;
        set isP = false;
    ELSE
        select ticket_no from ticket 
            where ticket.trip_no = no
            and ticket.ticket_date = tdate
            and ticket.departure_station_no = departure 
            and ticket.terminal_station_no = terminal
            into DC;
        update ticket set remaining_count = remaining_count - tamount 
            where ticket.ticket_no = DC;
        select DC;
        call proc_insert_pus(name, DC, tamount);
        set isP = true;
    END IF;
END ||
DELIMITER ;

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

call proc_insert_pus('13349073', 1, 29);


call proc_prushing('13349076', 'G381', '2016-01-01', 1, 3, 5, @isF);
select @isF;


select remaining_count from ticket 
    where ticket.trip_no = no
    and ticket.ticket_date = tdate
    and ticket.departure_station_no = departure 
    and ticket.terminal_station_no = terminal;


call proc_prushing(?, ?, ?, ?, ?, ?, ?);


select * from user where user_name = "13349076";


select count(*) from purchasing where user_name = "13349076";


DELIMITER ||
drop function if exists return_pursh_amt ||
create function return_pursh_amt (
    usr varchar(20)
)
    returns int
    READS SQL DATA
    BEGIN
        DECLARE isResult int default 0;
        select count(*) from purchasing where user_name = usr into isResult;
        RETURN isResult;
    END ||
DELIMITER ;

select return_pursh_amt('13349076') as rs;


select 
    ticket.ticket_date dt,
    ticket.trip_no no,
    return_station_name(ticket.departure_station_no) as sd,
    return_station_name(ticket.terminal_station_no) as ed,
    purchasing.amount
    from ticket, purchasing
    where ticket.ticket_no = purchasing.ticket_no and purchasing.user_name = '13349076';

