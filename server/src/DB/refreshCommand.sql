drop table if exists Person;
drop table if exists Event;
drop table if exists User;
drop table if exists Auth_Token;

create table Person
(
    PersonID varchar(255) not null primary key,
    Username varchar(255) not null, /* foreign key */
    Firstname varchar(255) not null,
    Lastname varchar(255) not null,
    Gender varchar(255) not null,
    FatherID varchar(255), /* foreign key */
    MotherID varchar(255), /* foreign key */
    SpouseID varchar(255) /* foreign key */
);

create table Event
(
    EventID varchar(255) not null primary key,
    Username varchar(255) not null, /* foreign key */
    PersonID varchar(255) not null, /* foreign key */
    Latitude float(25) not null,
    Longitude float(25) not null,
    Country varchar(255) not null,
    City varchar(255) not null,
    EventType varchar(255) not null,
    Year int not null
);

create table User
(
    Username varchar(255) not null primary key,
    Password varchar(255) not null,
    Email varchar(255) not null,
    Fname varchar(255) not null,
    Lname varchar(255) not null,
    Gender varchar(255) not null,
    PersonID varchar(255) not null /* foreign key */
);

create table Auth_Token
(
    Value integer primary key,
    Username varchar(255) not null /* foreign key */
);