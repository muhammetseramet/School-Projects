
create database bookstore127;

create table Author(
	author_name varchar(60) not null primary key,
	author_address varchar(200),
	author_url varchar(50)

);


create table Publisher(
	publisher_name varchar(60) not null primary key,
	publisher_address varchar(200),
	phone int,
	publisher_url varchar(50)

);

create table Book(
	isbn int not null primary key,
	publish_year date,
	title varchar(50),
	price int,
	author_name varchar(60) foreign key references Author(author_name),
	publisher_name varchar(60) foreign key references Publisher(publisher_name) on update cascade
);
create table Warehouse(
	warehouse_code int primary key,
	phone int,
	warehouse_address varchar(200)

);

create table Book_Warehouse(
	isbn int foreign key references Book(isbn),
	warehouse_code int foreign key references Warehouse(warehouse_code)

);

create table Customer(
	email varchar(70) primary key,
	customer_name varchar(60),
	phone int,
	customer_address varchar(200),
	nameAddress as customer_name+Space(1)+customer_address
);

create table ShoppingBasket(
	shoppingBasket_id int primary key,
	customer_email varchar(70) foreign key references Customer(email)
);

create table Feedback(
	customer_email varchar(70) foreign key references Customer(email),
	book_isbn int foreign key references Book(isbn),
	rating int,
	comment varchar(200)
);

create table Book_ShoppingBasket(
	book_isbn int foreign key references Book(isbn),
	shoppingBasket_id int foreign key references ShoppingBasket(shoppingBasket_id)
);

insert into Customer(email, customer_name, phone, customer_address) values ('aktasmahmut97@gmail.com', 'Mahmut AKTAS', 507636642 , 'Pendik, Istanbul') ;
insert into Customer(email, customer_name, phone, customer_address) values ('ayhanerkin@gmail.com', 'Ayhan ERKIN', 551989123 , 'Nilüfer, Bursa') ;
insert into Customer(email, customer_name, phone, customer_address) values ('aysefatma@gmail.com', 'Ayse YILMAZ', 543212347 , 'Fatih, Istanbul') ;
insert into Customer(email, customer_name, phone, customer_address) values ('ezgimola@gmail.com', 'Ezgi MOLA', 507612673 , 'Gelibolu, Çanakkale') ;
insert into Customer(email, customer_name, phone, customer_address) values ('fundayasar@gmail.com', 'Funda YASAR', 454563123 , 'Bandýrma, Bolu') ;
insert into Customer(email, customer_name, phone, customer_address) values ('hakandemirci@hotmail.com' , 'Hakan Demirci', 532561452 , 'Çankaya, Ankara') ;
insert into Customer(email, customer_name, phone, customer_address) values ('hughjackman@gmail.com', 'Hugh JACKMAN', 536951238 , 'Kadikoy, Istanbul') ;
insert into Customer(email, customer_name, phone, customer_address) values ('johnnydepp@hotmail.com', 'Johnny DEPP', 329854326 , 'Sincan, Ankara') ;
insert into Customer(email, customer_name, phone, customer_address) values ('martinfreeman@gmail.com', 'Martin FREEMAN', 289127650 , 'Maltepe, Istanbul') ;
insert into Customer(email, customer_name, phone, customer_address) values ('morganfreeman@gmail.com', 'Morgan FREEMAN', 657982341 , 'Kadikoy, Istanbul') ;

insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Bloomsbury', 'London, UK', 1256302699, 'bloomsbury.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Bookmasters', 'Ohio, USA', 567215003, 'bookmasters.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Catapult', 'California, USA', 55123498, 'catapult.co'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Epsilon', 'Istanbul, Turkey', 2123456789, 'epsilonyayinevi.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Ithaki', 'Ankara, Turkey', 34562181, 'ithakiyayinlari.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Palme', 'Bursa, Turkey', 245652130, 'palmeyayinevi.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Pearson', 'Manchester, UK', 218932144, 'pearsoneducation.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Pegasus', 'Istanbul, Turkey', 212567234, 'pegasusyayinlari.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('Scholastic Press', 'Pennsylvania, USA', 269445912, 'scholastic.com'); 
insert into Publisher (publisher_name, publisher_address, phone, publisher_url) values ('YKY', 'Istanbul, Turkey', 2125431269, 'ykykultur.com.tr'); 

insert into Author(author_name, author_address, author_url) values ('Anany Levitin', 'Washington, USA', 'ananylevitin.com'); 
insert into Author(author_name, author_address, author_url) values ('George R.R. Martin', 'New Jersey, USA', 'georgerrmartin.com'); 
insert into Author(author_name, author_address, author_url) values ('J.K. Rowling', 'London, UK', 'jkrowling.com'); 
insert into Author(author_name, author_address, author_url) values ('J.R.R. Tolkien', 'Dorset, UK', 'NULL'); 
insert into Author(author_name, author_address, author_url) values ('Kemal Tahir', 'Istanbul, Turkey', 'NULL'); 
insert into Author(author_name, author_address, author_url) values ('Margaret Weis', 'London, UK', 'margaretweis.com'); 
insert into Author(author_name, author_address, author_url) values ('Nilsson Riedel', 'London, UK', 'nilssonriedel.com'); 
insert into Author(author_name, author_address, author_url) values ('Sir Arthur Conan Doyle', 'Edinburgh, UK', 'NULL'); 
insert into Author(author_name, author_address, author_url) values ('Suzanne Collins', 'New York, USA', 'suzannecollinsbooks.com'); 
insert into Author(author_name, author_address, author_url) values ('Victor Hugo', 'Paris, France', 'NULL'); 

 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1100, '1997-07-30', 'Harry Potter And The Philosophers Stone', 10, 'J.K. Rowling', 'YKY'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1101, '1998-07-02', 'Harry Potter And The Chamber Of Secrets', 12, 'J.K. Rowling', 'YKY'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1102, '1999-07-08', 'Harry Potter And The Prisoner Of Azkaban', 12, 'J.K. Rowling', 'YKY'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1103, '1862-01-01', 'Les Miserables', 6, 'Victor Hugo', 'Epsilon'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1104, '1887-01-01', 'A Study In Scarlet', 5, 'Sir Arthur Conan Doyle', 'Bloomsbury'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1105, '1996-08-01', 'Game Of Thrones', 15, 'George R.R. Martin', 'Bookmasters'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1106, '1937-09-21', 'Hobbit', 13, 'J.R.R. Tolkien', 'Catapult'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1107, '1998-01-01', 'Lord Of The Rings Fellowship Of The Ring', 18, 'J.R.R. Tolkien', 'Ithaki'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1108, '1956-01-01', 'Esir Sehrin Insanlarý', 7, 'Kemal Tahir', 'Ithaki'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1109, '2008-09-14', 'The Hunger Games', 20, 'Suzanne Collins', 'Scholastic Press'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1110, '2012-07-17', 'Electric Circuits', 60, 'Nilsson Riedel', 'Pearson'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1111, '2013-04-12', 'Analysis Of Algorithms', 50, 'Anany Levitin', 'Pearson'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1112, '2000-06-12', 'Harry Potter And The Goblet Of Fire', 15, 'J.K. Rowling', 'YKY'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1113, '1997-04-29', 'Clash Of Kings', 17, 'George R.R. Martin', 'Epsilon'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1114, '2009-09-02' ,'Catching Fire', 12, 'Suzanne Collins', 'Scholastic Press'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1115, '2010-07-06', 'Mocking Jay', 17, 'Suzanne Collins', 'Scholastic Press'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1116, '1954-01-01', 'Silmarillion', 20, 'J.R.R. Tolkien', 'Catapult'); 
 insert into Book(isbn, publish_year, title, price, author_name, publisher_name) values (1117, '1888-01-01', 'A Game Of Shadows', 8, 'Sir Arthur Conan Doyle', 'Bloomsbury'); 

 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'hughjackman@gmail.com', 1111, 9, 'Great!'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'hughjackman@gmail.com', 1117, 4, 'Not OK'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'hughjackman@gmail.com', 1114, 8, 'Catchy'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'hughjackman@gmail.com', 1105, 10, 'Best book ever!'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'morganfreeman@gmail.com', 1111, 8, 'Not bad'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'aktasmahmut97@gmail.com', 1111, 10, 'Great'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'aysefatma@gmail.com', 1105, 8, ''); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'aktasmahmut97@gmail.com', 1105, 4, ''); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'johnnydepp@hotmail.com', 1117, 2, 'worst book'); 
 insert into Feedback(customer_email, book_isbn, rating, comment) values ( 'morganfreeman@gmail.com', 1117, 1, ''); 

 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (1, 'aktasmahmut97@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (2, 'aysefatma@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (3, 'aktasmahmut97@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (4, 'hughjackman@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (5, 'johnnydepp@hotmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (6, 'hughjackman@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (7, 'morganfreeman@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (8, 'ezgimola@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (9, 'martinfreeman@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (10, 'johnnydepp@hotmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (11, 'hughjackman@gmail.com'); 
 insert into ShoppingBasket(shoppingBasket_id, customer_email) values (12, 'aysefatma@gmail.com'); 

 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77100, 214634984, 'Kagithane, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77101, 212610762, 'Merter, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77102, 216123679, 'Kadikoy, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77103, 212341230, 'Sariyer, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77104, 212545645, 'Avcilar, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77105, 216224462, 'Beykoz, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77106, 212115568, 'Kagithane, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77107, 216668823, 'Kadikoy, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77123, 216764239, 'Merter, Istanbul'); 
 insert into Warehouse(warehouse_code, phone, warehouse_address) values (77132, 554632454, 'Besiktas, Istanbul'); 

 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1111, 1); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1104, 1); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1105, 1); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1105, 2); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1100, 2); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1103, 2); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1111, 4); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1117, 4); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1114, 4); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1103, 4); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1105, 4); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1100, 7); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1117, 7); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1111, 7); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1111, 11); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1113, 11); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1103, 11); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1101, 5); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1100, 5); 
 insert into Book_ShoppingBasket(book_isbn, shoppingBasket_id) values (1117, 5); 

 insert into Book_Warehouse(isbn, warehouse_code) values (1100, 77100); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1105, 77100); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1109, 77132); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1108, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1101, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1102, 77132); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1104, 77100); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1100, 77132); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1104, 77132); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1109, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1109, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1111, 77107); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1112, 77107); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1104, 77105); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1104, 77101); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1117, 77103); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1117, 77104); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1117, 77106); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1114, 77102); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1114, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1114, 77100); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1112, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1112, 77107); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1112, 77104); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1112, 77106); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1109, 77100); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1109, 77123); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1109, 77103); 
 insert into Book_Warehouse(isbn, warehouse_code) values (1108, 77103); 

 CREATE PROCEDURE deleteCustomer
	@email varchar(60)
AS
BEGIN
SET NOCOUNT ON;

delete 
from Customer
where Customer.email = @email

END

exec deleteCustomer 'fundayasar@gmail.com'

CREATE PROCEDURE insertPublisher 
       @publisher_name varchar(70),
       @publisher_url  varchar(70),
       @phone          int, 
       @publisher_address  varchar(200)         
AS 
begin
insert into Publisher (publisher_name,publisher_url,phone,publisher_address) values( @publisher_name, @publisher_url, @phone, @publisher_address)
end

exec insertPublisher 'IsBankasi', 'isbankasikultur.com', 543213567, 'Kadikoy, Istanbul'


CREATE PROC getTotalSoldBookFromAPublisher
	@publisher_name varchar(70) OUTPUT

AS
select publisher_name, SUM(soldBooks) totalSold  from
 (select Book.title,Publisher.publisher_name, count(Book_ShoppingBasket.book_isbn) soldBooks
from Book_ShoppingBasket, Book, Publisher
where Book.isbn=Book_ShoppingBasket.book_isbn
and Publisher.publisher_name = Book.publisher_name
and Publisher.publisher_name = @publisher_name
and Book.publisher_name=@publisher_name
 group by Publisher.publisher_name, Book.title) soldBookst
 where publisher_name=@publisher_name
 group by publisher_name
 order by totalSold desc


GO

exec getTotalSoldBookFromAPublisher 'YKY' 

CREATE PROC bookNumberInAWarehouse
	@warehouse_code int OUTPUT

AS
select count(Book_Warehouse.isbn) bookNum, Warehouse.warehouse_code
from Book_Warehouse, Warehouse
where Book_Warehouse.warehouse_code = Warehouse.warehouse_code
and Warehouse.warehouse_code=@warehouse_code
group by Warehouse.warehouse_code

GO

exec bookNumberInAWarehouse 77100





CREATE VIEW CHEAP_BOOKS AS 
select B.title, A.author_name, B.price
from Book B, Author A
where B.author_name=A.author_name
and B.price <= (select avg(price) from Book)


CREATE VIEW BEST_SELLER_AUTHORS AS
select author_name, SUM(soldBooks) totalSold  from
 (select Book.title,Author.author_name, count(Book_ShoppingBasket.book_isbn) soldBooks
from Book_ShoppingBasket, Book, Author
where Book.isbn=Book_ShoppingBasket.book_isbn
and Author.author_name = Book.author_name
 group by Author.author_name, Book.title) soldBookst
 group by author_name
 order by totalSold desc

 CREATE VIEW MOST_FILLED_WAREHOUSES AS
 select Warehouse.warehouse_code, totalBook.bookNum
 from Warehouse,
(select count(Book_Warehouse.isbn) bookNum, Warehouse.warehouse_code
from Book_Warehouse, Warehouse
where Book_Warehouse.warehouse_code = Warehouse.warehouse_code
group by Warehouse.warehouse_code) totalBook
where totalBook.bookNum >= 4
and totalBook.warehouse_code = Warehouse.warehouse_code


CREATE VIEW TOP_RATED_BOOKS AS
select Book.title, avg(Feedback.rating) ratings, count(Feedback.book_isbn) peopleRated
from Book, Feedback
where Book.isbn = Feedback.book_isbn
and Feedback.rating >= (select avg(Feedback.rating) from Feedback)
group by title


CREATE TABLE BOOKLOG
(
	isbn int NOT NULL,
	ACTIONPERFORMED char(1),
	ACTIONOCCURREDAT datetime2
);


CREATE TRIGGER insert_BOOKLOG ON Book
AFTER INSERT AS 
BEGIN
    
    INSERT INTO BOOKLOG(isbn,ACTIONPERFORMED,ACTIONOCCURREDAT)
    select i.isbn, 'I', GETDATE()   FROM inserted i;
    
END;

CREATE INDEX idxCustomer_name
ON Customer (customer_name);