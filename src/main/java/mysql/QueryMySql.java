package mysql;

import org.apache.commons.lang.time.StopWatch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryMySql {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String DB_URL = "jdbc:mysql://localhost/speed";

    //  Database credentials
    static final String USER = "root";

    static final String PASS = "root";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to database...");

        for (int i = 0; i < 3; i++) {
            StopWatch sw = new StopWatch();
            System.out.println("Selecting a single person by ID");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); Statement stmt = conn.createStatement()) {

                sw.start();
                ResultSet resultSet = stmt.executeQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e where e.id = 2");
                sw.stop();
                System.out.println("Time required for a single person by id " + sw.getTime());
                printPersons(resultSet);
                resultSet.close();

                System.out.println();
                System.out.println("Find persons that belong to a specific set of Ids");
                PreparedStatement pstmt = conn.prepareStatement("select e.firstName, e.lastName, "
                        + "e.orgId, e.resume, e.salary from person e where e.id in (?, ?, ?, ?, ?)");
                pstmt.setLong(1, 0);
                pstmt.setLong(2, 1);
                pstmt.setLong(3, 2);
                pstmt.setLong(4, 3);
                pstmt.setLong(5, 4);
                sw.reset();
                sw.start();
                resultSet = pstmt.executeQuery();
                sw.stop();
                System.out.println("Time required for persons that belong to a specific set of Ids " + sw.getTime());
                printPersons(resultSet);
                pstmt.close();
                resultSet.close();

                System.out.println();
                System.out.println("Find persons with name 'John'");
                pstmt = conn.prepareStatement("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e where e.firstName = ?");
                pstmt.setString(1, "John");
                sw.reset();
                sw.start();
                resultSet = pstmt.executeQuery();
                sw.stop();
                System.out.println("Time required for Persons by name " + sw.getTime());
                printPersons(resultSet);
                pstmt.close();
                resultSet.close();

                System.out.println();
                System.out.println("Find persons with name like 'Smith'");
                pstmt = conn.prepareStatement("select e.firstName, e.lastName, "
                        + "e.orgId, e.resume, e.salary from person e where e.firstName like ?");
                pstmt.setString(1, "Smith%");
                sw.reset();
                sw.start();
                resultSet = pstmt.executeQuery();
                sw.stop();
                System.out.println("Time required for Persons by name like " + sw.getTime());
                printPersons(resultSet);
                pstmt.close();
                resultSet.close();

                System.out.println();
                System.out.println("Find persons working for organisation with ID greater than 1000");
                pstmt = conn.prepareStatement("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e where e.orgId > ? limit 100");
                pstmt.setLong(1, 1000L);
                sw.reset();
                sw.start();
                resultSet = pstmt.executeQuery();
                sw.stop();
                System.out.println("Time required Persons working for organization with ID > 1000: " + sw.getTime());
                printPersons(resultSet);
                pstmt.close();
                resultSet.close();

                System.out.println();
                System.out.println("Find persons by resume that starts with ...");
                pstmt = conn.prepareStatement("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e where e.resume like ?");
                pstmt.setString(1, "enyrmuznocelnmxnildkicmsedycy%");
                sw.reset();
                sw.start();
                resultSet = pstmt.executeQuery();
                sw.stop();
                System.out.println("Time required Find persons by resume that starts with  " + sw.getTime());
                printPersons(resultSet);
                pstmt.close();
                resultSet.close();

                System.out.println();
                System.out.println("Find persons with salary less than 100000");
                ResultSet set = null;
                try (Statement stmt2 = conn.createStatement()) {
                    sw.reset();
                    sw.start();
                    set = stmt2.executeQuery("select e.firstName, e.salary from person e where e.salary < 100000 limit 100");
                    sw.stop();
                    System.out.println("Time required for persons with salary < 100000: " + sw.getTime());
                    printPersons(set);
                }
                finally {
                    set.close();
                }

            }
            catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }

        }
    }

    private static void printPersons(ResultSet resultSet) throws Exception {
        int count = 0;
        //		while (resultSet.next()) {
        //			System.out.println("FirstName is " + resultSet.getString(1)
        //					+ " LastName " + resultSet.getString(2)
        //					+ " OrgId " + resultSet.getDouble(3)
        //					+ " Resume " + resultSet.getString(4)
        //					+ " Salary " + resultSet.getDouble(5));
        //			count++;
        //			if (count >= 5) {
        //				break;
        //			}
        //		}
    }
}
