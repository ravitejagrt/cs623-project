# cs623-project

### Add postgresql jdbc driver into the project once the project is cloned into the system folder. And add it to the buildpath.

Download the jdbc driver from the link here: [https://jdbc.postgresql.org/download.html](https://jdbc.postgresql.org/download.html)

### Links to the Demo video

Youtube link to view: [https://youtu.be/SMNOsRoFBWc](https://youtu.be/SMNOsRoFBWc)

### Below is the link for ER Diagram.

[https://drive.google.com/file/d/1JMGz05tpCs_kEYf09XUVv_Wl5TkQG87c/view?usp=sharing](https://drive.google.com/file/d/1JMGz05tpCs_kEYf09XUVv_Wl5TkQG87c/view?usp=sharing)


![Picture](https://github.com/ravitejagrt/cs623-project/blob/main/image%20(1).png)

### Database Schema: 'cs623'
### Tables: Product(#prod, pname, price), Depot(#dep, addr, volume), Stock(#prod, #dep, quantity)

### Project Structure:
* Transaction Class and Object classes Product, Depot and Stock
* In the main method, set the transaction isolation level to Serializable
* Methods:
  * defineTables - Creates Product, Depot, Stock tables in the database if not exists
  * getProductFromUser - To get product data from user
  * getStockDataFromUser - To get stock data from user
  * addProduct - Adds product to Product table
  * addStock - Adds stock to Stock table
  * doTransaction - This method actually does the transaction. Sets autocommit to false before starting transations, commits at the end after transaction is successful and sets  the auto commit to true at the end. In case of failure, the control goes to catch block in try catch and executes rollback.
* DB Variables:
  * DB_URL, USER for username, PASS for password.
  
Set the DB variables according to your own configuration.

### Heads-up
If the product, depot and stock tables has no data in your database or if the logs shows empty or no data in the tables, Insert depot data manually into the database before performing the transaction. With out depot, your transaction fails because to insert stock data depot is mandatory.
