package pl.pg.dbclient.employee;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.pg.dbclient.client.DbClient;
import pl.pg.dbclient.config.DbConfig;
import pl.pg.dbclient.config.DbConfigFinder;
import pl.pg.dbclient.employee.model.Employee;
import pl.pg.dbclient.employee.model.EmployeeWithColumnMapper;
import pl.pg.dbclient.employee.model.EmployeeWithRecordMapper;
import pl.pg.dbclient.helper.ResourceHelper;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {

    private static DbClient dbClient;
    private String sql = "select * from employee";

//    @BeforeAll
//    static void prepareConnection() {
//        String configFileName = "company_dbConfig.json";
//        String configName = "company database";
//        File resource = new ResourceHelper().getResource(configFileName);
//        DbConfig dbConfig = new DbConfigFinder(resource).find(configName);
//        dbClient = new DbClient(dbConfig);
//    }

//    @AfterAll
//    static void closeConnection() {
//        dbClient.closeConnection();
//    }

    @Disabled("Run only with local database prepared")
    @Test
    void shouldGetRecordsCount() {
        int count = dbClient.query(sql).count();
        assert 2 == count;
    }

    @Disabled("Run only with local database prepared")
    @Test
    void shouldMapRecordsToList() {
        List<Map<String, Object>> employees = dbClient.query(sql).asList();
        Map<String, Object> jack = employees.get(0);
        assertEquals(jack.get("firstname"), "Jack");
        assertEquals(jack.get("lastname"), "Nicholson");
        assertEquals(jack.get("salary"), new BigDecimal("5000.00"));

        Map<String, Object> rob = employees.get(1);
        assertEquals(rob.get("firstname"), "Rob");
        assertEquals(rob.get("lastname"), "Smith");
        assertEquals(rob.get("salary"), new BigDecimal("8000.00"));
    }

    @Disabled("Run only with local database prepared")
    @Test
    void shouldUseSimpleMappingAndGetListOfObjects() {
        List<Employee> employees = dbClient.query(sql).asList(Employee.class);
        Employee jack = employees.stream()
                .filter(employee -> employee.getFirstname().equals("Jack")).findFirst().get();
        assertEquals(jack.getLastname(), "Nicholson");
        assertEquals(jack.getSalary(), new BigDecimal("5000.00"));

        Employee rob = employees.stream()
                .filter(employee -> employee.getFirstname().equals("Rob")).findFirst().get();
        assertEquals(rob.getLastname(), "Smith");
        assertEquals(rob.getSalary(), new BigDecimal("8000.00"));
    }

    @Disabled("Run only with local database prepared")
    @Test
    void shouldUseColumnMappingAndGetListOfObjects() {
        List<EmployeeWithColumnMapper> employees = dbClient.query(sql).asList(EmployeeWithColumnMapper.class);
        EmployeeWithColumnMapper jack = employees.stream()
                .filter(employee -> employee.getName().equals("Jack")).findFirst().get();
        assertEquals(jack.getSurname(), "Nicholson");
        assertEquals(jack.getSalary(), new BigDecimal("5000.00"));

        EmployeeWithColumnMapper rob = employees.stream()
                .filter(employee -> employee.getName().equals("Rob")).findFirst().get();
        assertEquals(rob.getSurname(), "Smith");
        assertEquals(rob.getSalary(), new BigDecimal("8000.00"));
    }

    @Disabled("Run only with local database prepared")
    @Test
    void shouldUseRecordMappingAndGetListOfObjects() {
        List<EmployeeWithRecordMapper> employees = dbClient.query(sql).asList(EmployeeWithRecordMapper.class);
        EmployeeWithRecordMapper jack = employees.stream()
                .filter(employee -> employee.getName().equals("Jack")).findFirst().get();
        assertEquals(jack.getSurname(), "Nicholson");
        assertEquals(jack.getSalary(), new BigDecimal("5000.00"));

        EmployeeWithRecordMapper rob = employees.stream()
                .filter(employee -> employee.getName().equals("Rob")).findFirst().get();
        assertEquals(rob.getSurname(), "Smith");
        assertEquals(rob.getSalary(), new BigDecimal("8000.00"));
    }
}
