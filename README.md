# dbClient
Simple command line database client

### Installation
1. Clone repository
2. Run  _mvn install_

### Configuration
Prepare database configuration file:
```
{
  "name": "company database",
  "driverClassName": "org.postgresql.Driver",
  "url": "jdbc:postgresql://localhost:5432/company",
  "username": "postgres",
  "password": "admin"
}
```

### Usage
* You can run dbClient from _target_ folder using:

     _java -jar dbClient-jar-with-dependencies.jar -c config.json -s "select * from employee"_


Result:
```
[{"id":1,"employee_name":"John Smith","salary":5000.0},{"id":2,"employee_name":"Mambo Jumbo","salary":9000.0}]
```


* To prettify output add option _-p_

     _java -jar dbClient-jar-with-dependencies.jar -c config.json -s "select * from employee" -p_

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

### Options
```
 -c *       Database configuration JSON file path
 -s *       Sql query
 -p         Prettify output
```

