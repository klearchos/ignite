package mysql;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.fluttercode.datafactory.impl.DataFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class PopulateMySQL {

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /** Secret properties loading. **/
    private static final Properties props = new Properties();

    static {
        try (InputStream in = IgniteConfiguration.class.getClassLoader().getResourceAsStream("secret.properties")) {
            props.load(in);
        } catch (Exception ignored) {
            // No-op.
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        try ( Connection conn = DriverManager.getConnection(props.getProperty("dsMySQL_Speed.jdbc.url"),
                props.getProperty("dsMySQL_Speed.jdbc.username"),
                props.getProperty("dsMySQL_Speed.jdbc.password"));
              PreparedStatement pstmt = conn.prepareStatement("insert into person (id, firstName, lastName, " +
                      "orgId, resume, salary) values (?, ?, ?, ?, ?, ?)")) {

            DataFactory df = new DataFactory();

            for (long i = 0; i < 2_500_000; i++) {
                pstmt.setLong(1, i);
                pstmt.setString(2, df.getFirstName());
                pstmt.setString(3, df.getLastName());
                pstmt.setLong(4, df.getNumberBetween(1000, 5000));
                pstmt.setString(5, df.getRandomText(100));
                pstmt.setLong(6, df.getNumberBetween(1000, 100000));
                pstmt.addBatch();

                if (i > 0 && i % 10_000 == 0) {
                    pstmt.executeBatch();
                    System.out.println("Done: " + i);
                }
            }
        } catch(Exception se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        System.out.println("Goodbye!");
    }//end main
}
