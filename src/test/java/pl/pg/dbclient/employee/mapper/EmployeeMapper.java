package pl.pg.dbclient.employee.mapper;

import pl.pg.dbclient.employee.model.EmployeeWithRecordMapper;
import pl.pg.dbclient.mapper.RecordMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper extends RecordMapper<EmployeeWithRecordMapper> {

    @Override
    public EmployeeWithRecordMapper mapRow(ResultSet resultSet, int i) throws SQLException {
        EmployeeWithRecordMapper employee = new EmployeeWithRecordMapper();
        employee.setId(resultSet.getLong("id"));
        employee.setName(resultSet.getString("firstname"));
        employee.setSurname(resultSet.getString("lastname"));
        employee.setSalary(resultSet.getBigDecimal("salary"));
        return employee;
    }
}
