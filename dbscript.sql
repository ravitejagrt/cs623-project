--Dropping all tables to create predefined table with data
DROP TABLE STOCK;
DROP TABLE PRODUCT;
DROP TABLE DEPOT;

--Create table Product if not exists
CREATE TABLE IF NOT EXISTS Product (prod_id char(10), pname varchar(30), price decimal);
--Add constraints if not exists
ALTER TABLE Product ADD CONSTRAINT pk_product PRIMARY KEY (prod_id);
ALTER TABLE Product ADD CONSTRAINT ck_product_price CHECK (price > 0);
--Insert predefined records
INSERT INTO Product (prod_id, pname, price) VALUES ('p1', 'tape', 2.5), ('p2', 'tv', 250), ('p3', 'vcr', 80);

--Create table Product if not exists
CREATE TABLE IF NOT EXISTS Depot (dep_id char(10), addr varchar(30), volume integer);
--Add constraints if not exists
ALTER TABLE Depot ADD CONSTRAINT  pk_depot PRIMARY KEY (dep_id);
ALTER TABLE Depot ADD CONSTRAINT  ck_depot_volume CHECK (volume > 0);
--Insert predefined records
INSERT INTO Depot (dep_id, addr, volume) VALUES ('d1', 'New York', 9000), ('d2', 'Syracuse', 6000), ('d4', 'New York', 2000);

--Create table Product if not exists
CREATE TABLE IF NOT EXISTS Stock (prod_id char(10), dep_id char(10), quantity integer);
--Add constraints if not exists
ALTER TABLE Stock ADD CONSTRAINT  fk_product FOREIGN KEY(prod_id) REFERENCES Product(prod_id);
ALTER TABLE Stock ADD CONSTRAINT  fk_depot FOREIGN KEY(dep_id) REFERENCES Depot(dep_id);
--Insert predefined records
INSERT INTO Stock (prod_id, dep_id, quantity) VALUES ('p1', 'd1', 1000), ('p1', 'd2', -100), ('p1', 'd4', 1200), ('p3', 'd1', 3000), ('p3', 'd4', 2000), ('p2', 'd4', 1500), ('p2', 'd1', -400), ('p2', 'd2', 2000);
