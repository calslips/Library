drop table books if exists;
drop table users if exists;

create table users (userId int primary key, username varchar(30));
create table books (bookId int primary key, title varchar(255), author varchar(255), signedOutBy int references users(userId));

--insert into users (userId, username) values (23, 'me');
--insert into books (bookId, title, author, signedOutBy) values (55, 'blah', 'raft', 23);