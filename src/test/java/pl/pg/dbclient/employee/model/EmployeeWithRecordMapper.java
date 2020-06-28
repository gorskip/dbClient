package pl.pg.dbclient.employee.model;

import lombok.Data;
import pl.pg.dbclient.annotation.Mapper;
import pl.pg.dbclient.employee.mapper.EmployeeMapper;

import java.math.BigDecimal;

@Data
@Mapper(EmployeeMapper.class)
public class EmployeeWithRecordMapper {

    private Long id;
    private String name;
    private String surname;
    private BigDecimal salary;
}
