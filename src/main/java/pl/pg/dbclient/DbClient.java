package pl.pg.dbclient;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.pg.dbclient.config.DbConfig;
import pl.pg.dbclient.exception.CannotCloseConnectionException;
import pl.pg.dbclient.mapper.Mapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbClient {

    private final DbConfig dbConfig;
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> resultList = new ArrayList<>();

    public DbClient(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
        initJdbcTemplate();
    }

    private void initJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbConfig.getDriverClassName());
        dataSource.setUrl(dbConfig.getUrl());
        dataSource.setUsername(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DbClient query(String sql) {
        this.resultList = jdbcTemplate.queryForList(sql);
        return this;
    }

    public String asString() {
        return Mapper.toString(resultList);
    }

    public List<Map<String, Object>> asList() {
        return resultList;
    }

    public <T> List<T> asList(Class<T> clazz) {
        return resultList.stream()
                .map(result -> Mapper.convert(result, clazz))
                .collect(Collectors.toList());
    }

    public void closeConnection() {
        try {
            jdbcTemplate.getDataSource().getConnection().close();
        } catch (SQLException e) {
            throw new CannotCloseConnectionException(e);
        }
    }

}
