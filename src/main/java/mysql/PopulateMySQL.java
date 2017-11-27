package mysql;

import org.fluttercode.datafactory.impl.DataFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PopulateMySQL {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/speed";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.prepareStatement("insert into person (id, firstName, lastName, orgId, resume, salary) values (?, ?, ?, ?, ?, ?)");

            DataFactory df = new DataFactory();

            for (long i = 2_500_000; i < 5_000_000; i++) {
                stmt.setLong(1, i);
                stmt.setString(2, df.getFirstName());
                stmt.setString(3, df.getLastName());
                stmt.setLong(4, df.getNumberBetween(1000, 5000));
                stmt.setString(5, df.getRandomText(100));
                stmt.setLong(6, df.getNumberBetween(1000, 100000));
                stmt.executeUpdate();
                if (i > 0 && i % 10_000 == 0)
                    System.out.println("Done: " + i);

            }

            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main


}
