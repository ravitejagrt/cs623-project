
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class Transaction {

    private static final String TABLE_DROP_PRODUCT = "DROP TABLE IF EXISTS PRODUCT";
    private static final String TABLE_DROP_DEPOT= "DROP TABLE IF EXISTS DEPOT";
    private static final String TABLE_DROP_STOCK= "DROP TABLE IF EXISTS STOCK";

	// CREATE TABLE Product (prod char(10), pname varchar(30), price decimal);
	private static final String TABLE_CREATE_PRODUCT = "CREATE TABLE PRODUCT (PROD varchar(10) NOT NULL, PNAME varchar(25) NOT NULL, PRICE decimal)";
    
    //CREATE TABLE Depot (dep char(10), addr varchar(30), volume integer);
    private static final String TABLE_CREATE_DEPOT = "CREATE TABLE DEPOT (DEP varchar(10) NOT NULL, ADDR varchar(25) NOT NULL, VOLUME integer)";

    //CREATE TABLE Stock (prod char(10), dep char(10), quantity integer);
    private static final String TABLE_CREATE_STOCK= "CREATE TABLE STOCK (PROD varchar(10) NOT NULL, DEP varchar(25) NOT NULL, QUANTITY integer)";
    
    //ALTER TABLE Product ADD CONSTRAINT pk_product PRIMARY KEY (prod);
    private static final String ALTER_TABLE_PRODUCT_ADD_PKEY = "ALTER TABLE Product ADD CONSTRAINT pk_product PRIMARY KEY (PROD)";
    //ALTER TABLE Product ADD CONSTRAINT ck_product_price CHECK (price > 0);
    private static final String ALTER_TABLE_PRODUCT_ADD_PRICE_CHECK = "ALTER TABLE Product ADD CONSTRAINT ck_product_price CHECK (PRICE > 0)";

    //INSERT INTO Product (prod, pname, price) VALUES ('p1', 'tape', 2.5), ('p2', 'tv', 250), ('p3', 'vcr', 80);  
    private static final String INSERT_PRODUCT_DATA = "INSERT INTO Product (PROD, PNAME, PRICE) VALUES ('p1', 'tape', 2.5), ('p2', 'tv', 250), ('p3', 'vcr', 80)";
	private static final String INSERT_PRODUCT = "INSERT INTO PRODUCT (PROD, PNAME, PRICE) VALUES (?,?,?)";
	private static final String DELETE_PRODUCT = "DELETE FROM PRODUCT WHERE PROD=?";

	//ALTER TABLE Depot ADD CONSTRAINT pk_depot PRIMARY KEY (dep);
    private static final String ALTER_TABLE_DEPOT_ADD_PKEY = "ALTER TABLE Depot ADD CONSTRAINT pk_depot PRIMARY KEY (DEP)";
	//ALTER TABLE Depot ADD CONSTRAINT ck_depot_volume CHECK (volume > 0);
    private static final String ALTER_TABLE_DEPOT_ADD_VOLUME_CHECK = "ALTER TABLE Depot ADD CONSTRAINT ck_depot_volume CHECK (VOLUME> 0)";

	//ALTER TABLE Stock ADD CONSTRAINT fk_product FOREIGN KEY(prod_id) REFERENCES Product(prod_id);
    private static final String ALTER_TABLE_STOCK_ADD_FKEY_PROD = "ALTER TABLE Stock ADD CONSTRAINT fk_product FOREIGN KEY(PROD) REFERENCES Product(PROD)";
	//ALTER TABLE Stock ADD CONSTRAINT fk_depot FOREIGN KEY(dep_id) REFERENCES Depot(dep_id);
    private static final String ALTER_TABLE_STOCK_ADD_FKEY_DEP = "ALTER TABLE Stock ADD CONSTRAINT fk_depot FOREIGN KEY(DEP) REFERENCES Depot(DEP)";
	
	//INSERT INTO Depot (dep, addr, volume) VALUES ('d1', 'New York', 9000), ('d2', 'Syracuse', 6000), ('d4', 'New York', 2000);
    private static final String INSERT_DEPOT_DATA = "INSERT INTO Depot (DEP, ADDR, VOLUME) VALUES ('d1', 'New York', 9000), ('d2', 'Syracuse', 6000), ('d4', 'New York', 2000)";
	private static final String INSERT_DEPOT = "INSERT INTO DEPOT (DEP, ADDR, VOLUME)  VALUES (?,?,?)";
	private static final String DELETE_DEPOT = "DELETE FROM STOCK WHERE DEP=?";

	//INSERT INTO Stock (prod, dep, quantity) VALUES ('p1', 'd1', 1000), ('p1', 'd2', -100), ('p1', 'd4', 1200), ('p3', 'd1', 3000), ('p3', 'd4', 2000), ('p2', 'd4', 1500), ('p2', 'd1', -400), ('p2', 'd2', 2000);
	private static final String INSERT_STOCK_DATA = "INSERT INTO Stock (prod, dep, quantity) VALUES ('p1', 'd1', 1000), ('p1', 'd2', -100), ('p1', 'd4', 1200), ('p3', 'd1', 3000), ('p3', 'd4', 2000), ('p2', 'd4', 1500), ('p2', 'd1', -400), ('p2', 'd2', 2000)"; 
	private static final String INSERT_STOCK = "INSERT INTO STOCK (PROD, DEP, QUANTITY)  VALUES (?,?,?)";
	private static final String DELETE_STOCK = "DELETE FROM STOCK WHERE PROD=? AND DEP=?";
	
	// Database URL and credentials
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/cs623";
	static final String USER = "postgres";
	static final String PASS = "root";
	
    public static void main(String[] args) {
    	
    	Connection conn = null;
    	Statement statement = null;
    	
        try  {
        	conn = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = conn.createStatement();
									
			PreparedStatement productInsert = conn.prepareStatement(INSERT_PRODUCT);
			PreparedStatement stockInsert = conn.prepareStatement(INSERT_STOCK);
            
			// Run list of insert commands
			defineTablesAndData(statement);

            //(p100, cd, 5) 
            //(p100, d2, 50)
			/*
			 * productInsert.setString(1, "p100"); productInsert.setString(2, "cd");
			 * productInsert.setFloat(3, 50);
			 * 
			 * stockInsert.setString(1, "p100"); stockInsert.setString(2, "d2");
			 * stockInsert.setInt(3, 50);
			 * 
			 * // start transaction conn.setAutoCommit(false); productInsert.execute();
			 * System.out.println("Product Entered: Record inserted in Product");
			 * stockInsert.execute();
			 * System.out.println("Stock Entered: Record inserted in Stock"); // end
			 * transaction block, commit changes conn.commit();
			 * 
			 * // good practice to set it back to default true conn.setAutoCommit(true);
			 */
        } catch (Exception e) {
        	System.out.println("Failed");
            e.printStackTrace();
			/*
			 * try { conn.rollback(); System.out.
			 * println("Transaction failed. Any changes to the database during the transaction are reverted or rollback."
			 * ); } catch (SQLException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */
        } finally {
        	try {
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

    public static void defineTablesAndData(Statement statement) {
    	try {
			/*
			 * productInsert.setString(1, "p100"); productInsert.setString(2, "cd");
			 * productInsert.setFloat(3, 50); p
			 *roductInsert.execute();
			 */
    		statement.execute(TABLE_DROP_STOCK);
			statement.execute(TABLE_DROP_PRODUCT);
			statement.execute(TABLE_DROP_DEPOT);
			System.out.println("Dropped existing tables STOCK, PRODUCT, DEPOT");
			
			statement.execute(TABLE_CREATE_PRODUCT);
			statement.execute(ALTER_TABLE_PRODUCT_ADD_PKEY);
			statement.execute(ALTER_TABLE_PRODUCT_ADD_PRICE_CHECK);
			System.out.println("PRODUCT TABLE CREATED WITH CONSTRAINTS");
			
			statement.execute(TABLE_CREATE_DEPOT);
			statement.execute(ALTER_TABLE_DEPOT_ADD_PKEY);
			statement.execute(ALTER_TABLE_DEPOT_ADD_VOLUME_CHECK);
			System.out.println("DEPOT TABLE CREATED WITH CONSTRAINTS");
			
			statement.execute(TABLE_CREATE_STOCK);
			statement.execute(ALTER_TABLE_STOCK_ADD_FKEY_PROD);
			statement.execute(ALTER_TABLE_STOCK_ADD_FKEY_DEP);
			System.out.println("STOCK TABLE CREATED WITH CONSTRAINTS");
			
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
}