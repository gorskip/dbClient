package pl.pg.dbclient.employee.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Employee {

    private Long id;
    private String firstname;
    private String lastname;
    private BigDecimal salary;
}
