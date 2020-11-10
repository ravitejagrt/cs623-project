

/*
 * @author Ravi Teja Gajarla 
 */

import java.sql.*;
import java.util.Scanner;

public class Transaction {
//    private static final String TABLE_DROP_PRODUCT = "DROP TABLE IF EXISTS PRODUCT";
//    private static final String TABLE_DROP_DEPOT= "DROP TABLE IF EXISTS DEPOT";
//    private static final String TABLE_DROP_STOCK= "DROP TABLE IF EXISTS STOCK";

	private static final String TABLE_CREATE_PRODUCT = "CREATE TABLE IF NOT EXISTS PRODUCT (PROD varchar(10) NOT NULL, PNAME varchar(25) NOT NULL, PRICE decimal,"
			+ "CONSTRAINT pk_product PRIMARY KEY (PROD), CONSTRAINT ck_product_price CHECK (PRICE > 0))";
    private static final String TABLE_CREATE_DEPOT = "CREATE TABLE IF NOT EXISTS DEPOT (DEP varchar(10) NOT NULL, ADDR varchar(25) NOT NULL, VOLUME integer, "
    		+ "CONSTRAINT pk_depot PRIMARY KEY (DEP), CONSTRAINT ck_depot_volume CHECK (VOLUME > 0))";
    private static final String TABLE_CREATE_STOCK= "CREATE TABLE IF NOT EXISTS STOCK (PROD varchar(10) NOT NULL, DEP varchar(10) NOT NULL, QUANTITY integer,"
    		+ " CONSTRAINT fk_product FOREIGN KEY(PROD) REFERENCES PRODUCT(PROD), CONSTRAINT fk_depot FOREIGN KEY(DEP) REFERENCES DEPOT(DEP))";
    
    private static final String ALTER_TABLE_PRODUCT_ADD_PKEY = "ALTER TABLE PRODUCT ADD CONSTRAINT IF NOT EXISTS pk_product PRIMARY KEY (PROD)";
    private static final String ALTER_TABLE_PRODUCT_ADD_PRICE_CHECK = "ALTER TABLE PRODUCT ADD CONSTRAINT IF NOT EXISTS ck_product_price CHECK (PRICE > 0)";
    private static final String ALTER_TABLE_DEPOT_ADD_PKEY = "ALTER TABLE DEPOT ADD CONSTRAINT pk_depot PRIMARY KEY (DEP)";
    private static final String ALTER_TABLE_DEPOT_ADD_VOLUME_CHECK = "ALTER TABLE DEPOT ADD CONSTRAINT ck_depot_volume CHECK (VOLUME> 0)";
    private static final String ALTER_TABLE_STOCK_ADD_FKEY_PROD = "ALTER TABLE Stock ADD CONSTRAINT fk_product FOREIGN KEY(PROD) REFERENCES Product(PROD)";
	private static final String ALTER_TABLE_STOCK_ADD_FKEY_DEP = "ALTER TABLE Stock ADD CONSTRAINT fk_depot FOREIGN KEY(DEP) REFERENCES Depot(DEP)";
	
    private static final String SELECT_PRODUCT_QUERY = "SELECT PROD, PNAME, PRICE FROM PRODUCT";
    private static final String SELECT_DEPOT_QUERY = "SELECT DEP, ADDR, VOLUME FROM DEPOT";
    private static final String SELECT_STOCK_QUERY = "SELECT PROD, DEP, QUANTITY FROM STOCK";
    
    private static final String INSERT_PRODUCT_DATA = "INSERT INTO Product (PROD, PNAME, PRICE) VALUES ('p1', 'tape', 2.5), ('p2', 'tv', 250), ('p3', 'vcr', 80)";
    private static final String INSERT_DEPOT_DATA = "INSERT INTO Depot (DEP, ADDR, VOLUME) VALUES ('d1', 'New York', 9000), ('d2', 'Syracuse', 6000), ('d4', 'New York', 2000)";
	private static final String INSERT_STOCK_DATA = "INSERT INTO Stock (prod, dep, quantity) VALUES ('p1', 'd1', 1000), ('p1', 'd2', -100), ('p1', 'd4', 1200), ('p3', 'd1', 3000), ('p3', 'd4', 2000), ('p2', 'd4', 1500), ('p2', 'd1', -400), ('p2', 'd2', 2000)";
	
	private static final String INSERT_PRODUCT = "INSERT INTO PRODUCT (PROD, PNAME, PRICE) VALUES (?,?,?)";	
	private static final String INSERT_DEPOT = "INSERT INTO DEPOT (DEP, ADDR, VOLUME)  VALUES (?,?,?)";
	private static final String INSERT_STOCK = "INSERT INTO STOCK (PROD, DEP, QUANTITY)  VALUES (?,?,?)";
	
	private static final String DELETE_PRODUCT = "DELETE FROM PRODUCT WHERE PROD=?";
	private static final String DELETE_DEPOT = "DELETE FROM STOCK WHERE DEP=?";
	private static final String DELETE_STOCK = "DELETE FROM STOCK WHERE PROD=? AND DEP=?";
	
	// Database URL and credentials
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/cs623";
	static final String USER = "postgres";
	static final String PASS = "root";
	static Scanner scan = null;
	
    public static void main(String[] args) throws SQLException {    	
    	Connection conn = null;
    	Statement statement = null;
    	conn = DriverManager.getConnection(DB_URL, USER, PASS);
    	statement = conn.createStatement();    	
        try  {            
			// Run list of insert commands
			defineTablesAndData(statement);
			System.out.println("Perform transaction with the data Product:(p100, cd, 5) and Stock:(p100, d2, 5)");
			System.out.println("\nData before Transaction.");
			showData(statement);
			Product product = new Product("p100", "cd", 5);
			Stock stock = new Stock("p100", "d2",50);
			doTransaction(conn, product, stock);
			System.out.println("\nData after Transaction.");
            showData(statement);
            
            System.out.println("Would you like to perform Group5 transaction?\n"
            		+ "You should be able to enter product and stock details as part of the transaction.\n(y/n):");
            scan = new Scanner(System.in);
            String isTransaction = scan.next();
            
        	switch(isTransaction.toLowerCase()) {
        		case "y":
        			//  take input from user
        			// handled failure like revert if failed in middle of transaction
        			// as group 5 we need to add product and stock only in our transaction
        			
        			// set transaction isolation to serializable
        			
        			// transaction starts here
        			// set auto commit false
        			// get product details from user
        			//insert product
        			//addProduct(conn, product);
        			// get stock details from user
        			// insert stock
        			//addStock(conn, stock);
        			// commit()
        			// set auto commit to true
        			// in case of error rollback in catch block
        			userTransaction(conn, statement);
    			default:
    				System.out.println("exit");
    				break;
        	}
            
        } catch (Exception e) {
            e.printStackTrace();
			 
        } finally {
        	try {
        		scan.close();
				if (conn != null)
					conn.close();
				if (statement != null)
					statement.close();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}
        }
    }
    
    public static void userTransaction(Connection conn, Statement statement) {
		doTransaction(conn);
		System.out.println("\nData after transaction:");
		showData(statement);
		// ask user for another transaction
		System.out.println("\nWould you like to perform another Group5 transaction?\n(y/n):");
        String isAnotherTransaction = scan.next();
		if(isAnotherTransaction.toLowerCase().equals("y")) {
			userTransaction(conn, statement);
		}
    }

    public static void doTransaction(Connection conn) {
    	try {			 
    		System.out.println("---Transaction started---");
			// start transaction with autoCommit false
    		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false); 
			System.out.println("Transaction Started.");
			Product product = getProductFromUser(scan);
			Stock stock = getStockFromUser(scan);
			addProduct(conn, product);
			addStock(conn, stock);
			// end transaction block, commit changes 
			conn.commit();
			// good practice to set it back to default true 
			conn.setAutoCommit(true);
			System.out.println("---Transaction Successful and completed---");
			
    	} catch (Exception e) {
        	System.out.println("Transaction Failed. Below is the database error");
			// TODO: handle exception
            e.printStackTrace();
            try { 
            	conn.rollback(); System.out.
			println("Transaction failed. Any changes to the database during the transaction failure are reverted or roll back.");
            } catch (SQLException e1) { // TODO Auto-generated catch block
			  e1.printStackTrace(); 
			}
		}	 
    }
    
    public static void doTransaction(Connection conn, Product product, Stock stock) {  
    	try {			 
    		System.out.println("---Transaction started---");
			// start transaction with autoCommit false
    		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false); 
			addProduct(conn, product);
			addStock(conn, stock);
			// end transaction block, commit changes 
			conn.commit();
			// good practice to set it back to default true 
			conn.setAutoCommit(true);
			System.out.println("---Transaction Successful and completed---");
			
    	} catch (Exception e) {
        	System.out.println("Transaction Failed. Below is the database error");
            e.printStackTrace();
            try { 
            	conn.rollback(); System.out.
			println("Transaction failed. Any changes to the database during the transaction failure are reverted or roll back.");
            } catch (SQLException e1) { 
			  e1.printStackTrace(); 
			}
		}	 
    }
    
    public static void addProduct(Connection conn, Product product) throws SQLException {
    	PreparedStatement productInsert = null;
		productInsert = conn.prepareStatement(INSERT_PRODUCT);
		
		productInsert.setString(1, product.getProd()); 
		productInsert.setString(2, product.getpName());
		productInsert.setFloat(3, product.getPrice());
					
		System.out.println("Insert Product with data - prod:" + product.getProd() + ", pName:" + product.getpName() + ", price:" + product.getPrice());
		productInsert.execute();
		System.out.println("Product Entered: Record inserted in Product");
		productInsert.close();
    }
        
    public static void addStock(Connection conn, Stock stock) throws SQLException {
    	PreparedStatement stockInsert = null;
		stockInsert = conn.prepareStatement(INSERT_STOCK);

		stockInsert.setString(1, stock.getProd()); 
		stockInsert.setString(2, stock.getDep());
		stockInsert.setInt(3, stock.getQuantity());

		System.out.println("Insert Stock with data - prod:" + stock.getProd() + ", depot:" + stock.getDep() + ", quantity:" + stock.getQuantity());
		stockInsert.execute();
		System.out.println("Stock Entered: Record inserted in Stock"); 
		stockInsert.close();
    }
    
    public static void addDepot(Connection conn, Depot depot) throws SQLException {
    	PreparedStatement depotInsert = null;
    	depotInsert = conn.prepareStatement(INSERT_DEPOT);

    	depotInsert.setString(1, depot.getDep()); 
    	depotInsert.setString(2, depot.getAddr());
    	depotInsert.setInt(3, depot.getVolume());

		System.out.println("Insert Depot with data - Dep Id:" + depot.getDep() + ", Address:" + depot.getAddr() + ", Volume:" + depot.getVolume());
		depotInsert.execute();
		System.out.println("Depot Entered: Record inserted in Depot");     	
		depotInsert.close();
    }
    
    public static Product getProductFromUser(Scanner scan) {
    	Product product = new Product();
    	System.out.println("Enter details of product:");
    	System.out.println("Product Id:");
    	product.setProd(scan.next());
    	System.out.println("Product Name:");
    	product.setpName(scan.next());
    	System.out.println("Product Price:");
    	product.setPrice(scan.nextFloat());
		return product;
    }
    
    public static Depot getDepotFromUser(Scanner scan) {
    	Depot depot= new Depot();
    	System.out.println("Enter details of Depot:");
    	System.out.println("Depot Id:");
    	depot.setDep(scan.next());
    	System.out.println("Depot Address:");
    	depot.setAddr(scan.next());
    	System.out.println("Depot Volume:");
    	depot.setVolume(scan.nextInt());
		return depot;
    }
    
    public static Stock getStockFromUser(Scanner scan) {
    	Stock stock = new Stock();
    	System.out.println("Enter details of Stock:");
    	System.out.println("Product Id:");
    	stock.setProd(scan.next());
    	System.out.println("Depot Id:");
    	stock.setDep(scan.next());
    	System.out.println("Quantity:");
    	stock.setQuantity(scan.nextInt());
    	return stock;
    }

    public static void defineTablesAndData(Statement statement) {
    	try {
//    		statement.execute(TABLE_DROP_STOCK);
//			statement.execute(TABLE_DROP_PRODUCT);
//			statement.execute(TABLE_DROP_DEPOT);
//			System.out.println("Dropped existing tables STOCK, PRODUCT, DEPOT");
			
			boolean productTableCreated = statement.execute(TABLE_CREATE_PRODUCT);
			System.out.println("PRODUCT TABLE CREATED WITH CONSTRAINTS");			
			statement.execute(TABLE_CREATE_DEPOT);
			System.out.println("DEPOT TABLE CREATED WITH CONSTRAINTS");			
			statement.execute(TABLE_CREATE_STOCK);
			System.out.println("STOCK TABLE CREATED WITH CONSTRAINTS");
			
//			statement.execute(ALTER_TABLE_PRODUCT_ADD_PKEY);
//			statement.execute(ALTER_TABLE_PRODUCT_ADD_PRICE_CHECK);
//			statement.execute(ALTER_TABLE_DEPOT_ADD_PKEY);
//			statement.execute(ALTER_TABLE_DEPOT_ADD_VOLUME_CHECK);
//			statement.execute(ALTER_TABLE_STOCK_ADD_FKEY_PROD);
//			statement.execute(ALTER_TABLE_STOCK_ADD_FKEY_DEP);
			
    		statement.execute(INSERT_PRODUCT_DATA);
			System.out.println("PRODUCT DATA INSERTED INTO TABLE");
    		statement.execute(INSERT_DEPOT_DATA);
			System.out.println("DEPOT DATA INSERTED INTO TABLE");
    		statement.execute(INSERT_STOCK_DATA);
			System.out.println("STOCK DATA INSERTED INTO TABLE");
    	} catch (Exception e) {
            e.printStackTrace();
    	}
    }
    
    public static void showData(Statement statement) {
    	showProductsData(statement);
    	showDepotsData(statement);
    	showStocksData(statement);
    	System.out.println();
    }
    
    public static void showProductsData(Statement statement) {
    	try {
			ResultSet rs = statement.executeQuery(SELECT_PRODUCT_QUERY);
			System.out.println("\nProducts Data\nprod\tpname\tprice\n" + "-----------------------------");
			while(rs.next()) {
				String prod = rs.getString(1);
				String pname = rs.getString(2);
				float price = rs.getFloat(3);
				System.out.println(prod + "\t" + pname + "\t" + price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static void showDepotsData(Statement statement) {
    	try {
			ResultSet rs = statement.executeQuery(SELECT_DEPOT_QUERY);
			System.out.println("\nDepot Data:\ndep\taddr\tvolume\n" + "-----------------------------");
			while(rs.next()) {
				String dep = rs.getString(1);
				String addr= rs.getString(2);
				int volume = rs.getInt(3);
				System.out.println(dep + "\t" + addr + "\t" + volume);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static void showStocksData(Statement statement) {
    	try {
			ResultSet rs = statement.executeQuery(SELECT_STOCK_QUERY);
			System.out.println("\nStock Data\nprod\tdep\tquantity\n" + "-----------------------------");
			while(rs.next()) {
				String prod = rs.getString(1);
				String dep = rs.getString(2);
				int quantity = rs.getInt(3);
				System.out.println(prod + "\t" + dep + "\t" + quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}