package pl.pg.dbclient;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.pg.dbclient.annotation.RecordMapper;
import pl.pg.dbclient.config.DbConfig;
import pl.pg.dbclient.exception.CannotCloseConnectionException;
import pl.pg.dbclient.mapper.ColumnMapper;
import pl.pg.dbclient.mapper.Mapper;
import pl.pg.dbclient.mapper.RowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbClient {

    private final DbConfig dbConfig;
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> resultList = new ArrayList<>();
    private String sql;

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
       this.sql = sql;
       return this;
    }

    private List<Map<String, Object>> queryForList() {
        return jdbcTemplate.queryForList(sql);
    }

    private <T> List<T> queryForList(RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    public String asString() {
        return Mapper.toString(jdbcTemplate.queryForList(sql));
    }

    public List<Map<String, Object>> asList() {
        return queryForList();
    }

    public <T> List<T> asList(Class<T> clazz) {
        if(clazz.isAnnotationPresent(RecordMapper.class)) {
            clazz.getAnnotation(RecordMapper.class).value()
        }
        return queryForList().stream()
                .map(result -> new ColumnMapper<T>().map(result, clazz))
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
