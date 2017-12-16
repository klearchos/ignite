package mysql;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class QueryMySql {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final boolean DEBUG = false;

    /** Secret properties loading. **/
    private static final Properties props = new Properties();

    static {
        try (InputStream in = IgniteConfiguration.class.getClassLoader().getResourceAsStream("secret.properties")) {
            props.load(in);
        } catch (Exception ignored) {
            // No-op.
        }
    }

    private static void selectBySignlePersonId(Connection conn) throws Exception {
        StopWatch sw = new StopWatch();
        System.out.println("Selecting a single person by ID");
        ResultSet resultSet = null;
        try (Statement stmt = conn.createStatement()) {
            sw.start();
            resultSet = stmt.executeQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e where e.id = 2");
            sw.stop();
            System.out.println("Time required for a single person by id " + sw.getTime());
            printPersons(resultSet);
        } finally {
            DbUtils.close(resultSet);
        }
    }

    private static void selectPersonsInSet(Connection conn) throws Exception {
        ResultSet resultSet = null;
        try (PreparedStatement pstmt = conn.prepareStatement("select e.firstName, e.lastName, "
                + "e.orgId, e.resume, e.salary from person e where e.id in (?, ?, ?, ?, ?)")) {
            StopWatch sw = new StopWatch();
            System.out.println();
            System.out.println("Find persons that belong to a specific set of Ids");

            pstmt.setLong(1, 0);
            pstmt.setLong(2, 1);
            pstmt.setLong(3, 2);
            pstmt.setLong(4, 3);
            pstmt.setLong(5, 4);

            sw.start();
            resultSet = pstmt.executeQuery();
            sw.stop();
            System.out.println("Time required for persons that belong to a specific set of Ids " + sw.getTime());
            printPersons(resultSet);
        } finally {
            DbUtils.closeQuietly(resultSet);
        }
    }

    private static void selectPersonByName(Connection conn) throws Exception {
        ResultSet resultSet = null;
        try(PreparedStatement pstmt = conn.prepareStatement("select e.firstName, e.lastName, e.orgId, e.resume, " +
                "e.salary from person e where e.firstName = ?")) {
            StopWatch sw = new StopWatch();
            System.out.println();
            System.out.println("Find persons with name 'John'");
            pstmt.setString(1, "John");
            sw.start();
            resultSet = pstmt.executeQuery();
            sw.stop();
            System.out.println("Time required for Persons by name " + sw.getTime());
            printPersons(resultSet);
        } finally {
            DbUtils.closeQuietly(resultSet);
        }
    }

    private static void selectPersonsWithNameLike(Connection conn) throws Exception {
        ResultSet resultSet = null;
        try(PreparedStatement pstmt = conn.prepareStatement("select e.firstName, e.lastName, "
                + "e.orgId, e.resume, e.salary from person e where e.firstName like ?")) {
            System.out.println();
            System.out.println("Find persons with name like 'Smith'");
            StopWatch sw = new StopWatch();
            pstmt.setString(1, "Smith%");
            sw.start();
            resultSet = pstmt.executeQuery();
            sw.stop();
            System.out.println("Time required for Persons by name like " + sw.getTime());
            printPersons(resultSet);
        } finally {
            DbUtils.closeQuietly(resultSet);
        }
    }

    private static void selectPersonsWithIdGreaterThan(Connection conn) throws Exception {
        ResultSet resultSet = null;
        try (PreparedStatement pstmt = conn.prepareStatement("select e.firstName, e.lastName, e.orgId, e.resume, " +
                "e.salary from person e where e.orgId > ? limit 100")) {
            StopWatch sw = new StopWatch();
            System.out.println();
            System.out.println("Find persons working for organisation with ID greater than 1000");
            pstmt.setLong(1, 1000L);
            sw.start();
            resultSet = pstmt.executeQuery();
            sw.stop();
            System.out.println("Time required Persons working for organization with ID > 1000: " + sw.getTime());
            printPersons(resultSet);
        } finally {
            DbUtils.closeQuietly(resultSet);
        }
    }

    private static void selectPersonsByResumeLike(Connection conn) throws Exception {
        ResultSet resultSet = null;
        try (PreparedStatement pstmt = conn.prepareStatement("select e.firstName, e.lastName, e.orgId, " +
                "e.resume, e.salary from person e where e.resume like ?")) {
            StopWatch sw = new StopWatch();
            System.out.println();
            System.out.println("Find persons by resume that starts with ...");
            pstmt.setString(1, "enyrmuznocelnmxnildkicmsedycy%");
            sw.start();
            resultSet = pstmt.executeQuery();
            sw.stop();
            System.out.println("Time required Find persons by resume that starts with  " + sw.getTime());
            printPersons(resultSet);
        } finally {
            DbUtils.closeQuietly(resultSet);
        }
    }

    private static void selectPersonsBySalaryLessThan(Connection conn) throws Exception {

        System.out.println();
        System.out.println("Find persons with salary less than 100000");
        StopWatch sw = new StopWatch();
        ResultSet set = null;
        try (Statement stmt = conn.createStatement()) {
            sw.reset();
            sw.start();
            set = stmt.executeQuery("select e.firstName, e.salary from person e where e.salary < 100000 limit 100");
            sw.stop();
            System.out.println("Time required for persons with salary < 100000: " + sw.getTime());
            printPersons(set);
        } finally {
            DbUtils.closeQuietly(set);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Connecting to database...");
        Class.forName(JDBC_DRIVER);
        try ( Connection conn = DriverManager.getConnection(props.getProperty("dsMySQL_Speed.jdbc.url"),
                props.getProperty("dsMySQL_Speed.jdbc.username"),
                props.getProperty("dsMySQL_Speed.jdbc.password"))) {

            for (int i = 0; i < 3; i++) {
                selectBySignlePersonId(conn);
                selectPersonsInSet(conn);
                selectPersonByName(conn);
                selectPersonsWithNameLike(conn);
                selectPersonsWithIdGreaterThan(conn);
                selectPersonsByResumeLike(conn);
                selectPersonsBySalaryLessThan(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void printPersons(ResultSet resultSet) throws Exception {
        if (DEBUG) {
            int count = 0;
            while (resultSet.next()) {
                System.out.println("FirstName is " + resultSet.getString(1)
                        + " LastName " + resultSet.getString(2)
                        + " OrgId " + resultSet.getDouble(3)
                        + " Resume " + resultSet.getString(4)
                        + " Salary " + resultSet.getDouble(5));
                count++;
                if (count >= 5) {
                    break;
                }
            }
        }
    }
}
