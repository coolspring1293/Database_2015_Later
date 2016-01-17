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