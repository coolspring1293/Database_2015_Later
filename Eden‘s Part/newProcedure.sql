-- 获得最大的ticket_no
DELIMITER ||
drop procedure if exists get_max_ticket_no ||
create procedure get_max_ticket_no(
    out max_no int)
    BEGIN
    select max(ticket_no) from ticket into max_no;
    END ||
DELIMITER ;

-- 获得最大的station_no
DELIMITER ||
drop procedure if exists get_max_no ||
create procedure get_max_no(
    out max_no int)
    BEGIN
    select max(station_no) from station into max_no;
    END ||
DELIMITER ;