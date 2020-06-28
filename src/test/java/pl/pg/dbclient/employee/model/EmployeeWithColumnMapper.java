package pl.pg.dbclient.employee.model;

import lombok.Data;
import pl.pg.dbclient.annotation.Column;

import java.math.BigDecimal;

@Data
public class EmployeeWithColumnMapper {

    private Long id;
    @Column("firstname")
    private String name;
    @Column("lastname")
    private String surname;
    private BigDecimal salary;

}
