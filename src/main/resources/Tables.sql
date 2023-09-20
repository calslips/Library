drop table painting_exhibition_junction if exists;
drop table exhibition if exists;
drop table painting if exists;
drop table author if exists;

--create table painting ( title varchar(255), year_made int, author varchar(255),
--    author_nationality varchar(255), author_year_born int );
--having data that isn't directly related to the table's entity causes issues with performance & consistency.
--what we want to do is to separate all of our data in a way that eliminate redunancy, while still preserving
--relationships between entities (ie, we still want to track what authors made what painting.)
--normalization
create table author (author_id int primary key, name varchar(255), author_nationality varchar(255), author_year_born int);
create table painting (painting_id int primary key, title varchar(255), year_made int, made_by int, foreign key (made_by) references author(author_id));
--a primary key is a unique identifier of a row - it contains a unique value (and can't be null)
--a foreign key is a value that is related to a primary key somewhere else
insert into author (author_id, name, author_nationality, author_year_born) values (111, 'van gogh', 'dutch', 1853);
insert into painting (painting_id, title, year_made, made_by) values (2222, 'starry night', 1880, 111);
insert into painting (painting_id, title, year_made, made_by) values (2223, 'self portrait', 1880, 111);

insert into painting (painting_id, title, year_made, made_by) values (2224, 'sample painting', 1890, 111);
--sql keeps track of all relationships we establish between tables - and it must maintain a logical organization
--which it refers to as referential integrity - a fkey must always either point to a REAL value or be null
--multiple foreign keys can point to the same primary key - a author may have many paintings - one-to-many relationship
--'multiplicity relationships' (cardinality)
--there are also one-to-one relationships and many-to-many relationships (manages with a junction table)
create table exhibition (exhibition_id int primary key, title varchar(255));
insert into exhibition (exhibition_id, title) values (1, 'van gogh at philadelphia');
insert into exhibition (exhibition_id, title) values (2, 'van gogh at new york city');

create table painting_exhibition_junction (exhibition_fkey int, painting_fkey int, foreign key (exhibition_fkey)
    references exhibition(exhibition_id), foreign key (painting_fkey) references painting(painting_id));

insert into painting_exhibition_junction (exhibition_fkey, painting_fkey) values (1, 2222);
insert into painting_exhibition_junction (exhibition_fkey, painting_fkey) values (2, 2222);