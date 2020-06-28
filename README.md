# dbClient
Simple command line database client

#### Installation
1. Clone repository
2. Run  _mvn install_

#### Configuration
Prepare database configuration file. It can contain multiple database configurations:
```
[
  {
    "name": "company database",
    "driverClassName": "org.postgresql.Driver",
    "url": "jdbc:postgresql://localhost:5432/company",
    "username": "postgres",
    "password": "admin"
  }
]
```

### Usage
* You can run dbClient from _target_ folder using:

     _java -jar dbClient-jar-with-dependencies.jar -c config.json -n "company database" -s "select * from employee"_


Result:
```
[{"id":1,"employee_name":"John Smith","salary":5000.0},{"id":2,"employee_name":"Mambo Jumbo","salary":9000.0}]
```


* To prettify output add option _-p_

     _java -jar dbClient-jar-with-dependencies.jar -c config.json -n "company database" -s "select * from employee" -p_

```
[ {
  "id" : 1,
  "employee_name" : "John Smith",
  "salary" : 5000.0
}, {
  "id" : 2,
  "employee_name" : "Mambo Jumbo",
  "salary" : 9000.0
} ]
```

#### Options
```
 -c *       Database configurations JSON file path
 -n *       Database configuration name
 -s *       Sql query
 -p         Prettify output
```


## DbClient usage
You can run DbClient programmatically
```
//define configuration file and configuration name
File configFile = new File("config.json");
String configName = "company database";

//create DbClient
DbConfig dbConfig = new DbConfigFinder(configFile).find(configName);
DbClient dbClient = new DbClient(dbConfig);

//execute SQL query
String sql = "select * from employee";
List<Map<String, Object>> employees = dbClient.query(sql).asList();
//where the map key is the column name

//or
String employees = dbClient.query(sql).asString();
```
### Methods
```
int count = dbClient.query(sql).count();
String result = dbClient.query(sql).asString();
List<Map<String, Object>> records = dbClient.query(sql).asList();
List<Employee> employees = dbClient.query(sql).asList(Employee.class);
     
dbClient.closeConnection();
```

### Mapping
You can also map records to List of desirable object.

```
String sql = "select * from employee";
List<Employee> employees = dbClient.query(sql).asList(Employee.class);
```

#### Simple mapping
Prepare Employee class with ***@Data*** annotation and add fields with names corresponding to column names

```
import lombok.Data;

@Data
public class Employee {

    private Long id;
    private String firstname;
    private String lastname;
    private BigDecimal salary;
}
```

#### Custom column mapping
If you want to have a different name for your field add ***@Column("column_name")*** annotation

```
@Data
public class Employee {

    private Long id;
    @Column("firstname")
    private String name;
    @Column("lastname")
    private String surname;
    private BigDecimal salary;
}
```

#### Custom object mapping
You can also implement your own mapping using ***RecordMapper*** class and ***@Mapper(Class<? extends RecordMapper>)*** annotation
Create EmployeeMapper class:

```
import pl.pg.dbclient.mapper.RecordMapper;

public class EmployeeMapper extends RecordMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getLong("id"));
        employee.setName(resultSet.getString("firstname"));
        employee.setSurname(resultSet.getString("lastname"));
        employee.setSalary(resultSet.getBigDecimal("salary"));
        return employee;
    }
}
```
Then add created mapper to Employee class. ***Simple mapping and Column mapping will be ignored.***
```
@Data
@Mapper(EmployeeMapper.class)
public class Employee {
...
```
### Example
```
public class EmployeeTest {

    private static DbClient dbClient;

    @BeforeAll
    static void prepareConnection() {
        String configPath = "company_dbConfig.json";
        String configName = "company database";
        File configFile = new File(configPath);
        DbConfig dbConfig = new DbConfigFinder(configFile).find(configName);
        dbClient = new DbClient(dbConfig);
    }

    @Test
    void shouldMapRecordsToGivenClass() {
        String sql = "select * from employee";

        List<Employee> employees = dbClient.query(sql).asList(Employee.class);

        Employee jack = employees.stream()
                .filter(employee -> employee.getName().equals("Jack")).findFirst().get();
        assertEquals(jack.getSurname(), "Nicholson");
        assertEquals(jack.getSalary(), new BigDecimal("5000.00"));

        Employee rob = employees.stream()
                .filter(employee -> employee.getName().equals("Rob")).findFirst().get();
        assertEquals(rob.getSurname(), "Smith");
        assertEquals(rob.getSalary(), new BigDecimal("8000.00"));
    }

    @AfterAll
    static void closeConnection() {
        dbClient.closeConnection();
    }
}
```
