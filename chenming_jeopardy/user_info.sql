DROP DATABASE if exists Jeopardy;
CREATE DATABASE Jeopardy;
USE Jeopardy;
CREATE TABLE UserInfo(
	username varchar(100) not null,
	password varchar(100) not null
);