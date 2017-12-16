package startup;

import config.ClientConfigurationFactory;
import demo.model.Person;
import org.apache.commons.lang.time.StopWatch;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;


/** This file was generated by Ignite Web Console (‎11‎/‎14‎/‎2017‎ ‎16‎:‎50) **/
public class ClientNodeCodeStartup {


    private static Ignite ignite = null;

    /**
     * Start up node with specified configuration.
     * 
     * @param args Command line arguments, none required.
     * @throws Exception If failed.
     **/
    public static void main(String[] args) throws Exception {
        ignite = Ignition.start(ClientConfigurationFactory.createConfiguration());
        for (int i = 0 ; i< 10; i++) {
            findPersons();
            queryRepository();
            sqlQueryEmployeesWithSalHigherManager();
        }
        ignite.close();
    }


    /**
     * Gets a list of Persons using standard read operations.
     */
    private static void findPersons() {
        IgniteCache<Long, Person> cache = ignite.cache("PersonCache");
        SqlFieldsQuery qry = new SqlFieldsQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e "
                + "where e.id = ?");
        qry.setArgs(2L);
        StopWatch sw = new StopWatch();
        sw.start();
        Iterable<?> personCols = cache.query(qry).getAll();
        sw.stop();

        System.out.println("Selecting a single person by ID");
        System.out.println("Time required for a single person by id " + sw.getTime());

        System.out.println();
        sw.reset();

        qry = new SqlFieldsQuery(
                "select p.firstName, p.lastName, p.orgId, p.resume, p.salary from Person p join table(id bigint = ?) i "
                        + "on p.id = i.id")
                .setArgs(new Object[] { new Long[] {1L, 2L, 3L, 4L, 5L}});
        sw.start();
        personCols = cache.query(qry).getAll();
        sw.stop();

        System.out.println("Find persons that belong to a specific set of Ids");
        System.out.println("Time required for persons that belong to a specific set of Ids " + sw.getTime());
    }

    /**
     * Execute advanced queries over the repository.
     */
    private static void queryRepository() {
        IgniteCache<Long, Person> cache = ignite.cache("PersonCache");
        System.out.println("\nFind persons with name 'John'");

        StopWatch sw = new StopWatch();
        SqlFieldsQuery qry = new SqlFieldsQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e "
                + "where e.firstName = ?");
        qry.setArgs("John");
        sw.start();
        Iterable<?> personCols = cache.query(qry).getAll();
        sw.stop();
        System.out.println("Time required for Persons by name " + sw.getTime());

        sw.reset();
        qry = new SqlFieldsQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e "
                + "where e.firstName like ?");
        qry.setArgs("Smith%");
        sw.start();
        personCols = cache.query(qry).getAll();
        sw.stop();
        System.out.println("Time required for Persons by name like " + sw.getTime());

        System.out.println();
        System.out.println("Persons working for organization with ID > 1000");

        sw.reset();
        qry = new SqlFieldsQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e "
                + "where e.orgId > ? limit 100");
        qry.setArgs(1000L);
        sw.start();
        personCols = cache.query(qry).getAll();
        sw.stop();
        System.out.println("Time required Persons working for organization with ID > 1000: " + sw.getTime());
        System.out.println("Find persons by resume that starts with ...");

        qry = new SqlFieldsQuery("select e.firstName, e.lastName, e.orgId, e.resume, e.salary from person e "
                + "where e.resume like ?");
        qry.setArgs("enyrmuznocelnmxnildkicmsedycy%");

        sw.reset();
        sw.start();
        personCols = cache.query(qry).getAll();
        sw.stop();
        System.out.println("Time required Find persons by resume that starts with " + sw.getTime());
    }

    private static void sqlQueryEmployeesWithSalHigherManager() {
        IgniteCache<Long, Person> cache = ignite.cache("PersonCache");

        System.out.println();
        System.out.println("Find persons with salary less than 100000");
        SqlFieldsQuery qry = new SqlFieldsQuery("select e.firstName, e.salary from Person e where e.salary < 100000 limit 100");

        StopWatch sw = new StopWatch();
        sw.start();
        Iterable<?> col = cache.query(qry).getAll();
        sw.stop();
        System.out.println("Time required for persons with salary < 100000: " + sw.getTime());
    }
}