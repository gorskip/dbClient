package pl.pg.dbclient.client;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.pg.dbclient.config.DbConfig;
import pl.pg.dbclient.exception.CannotCloseConnectionException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbClient {

    private final DbConfig dbConfig;
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> resultList = new ArrayList<>();
    private Query query;

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
       this.query = new Query(jdbcTemplate, sql);
       return this;
    }

    public String asString() {
        return query.asString();
    }

    public List<Map<String, Object>> asList() {
        return query.asList();
    }

    public <T> List<T> asList(Class<T> clazz) {
        return query.asList(clazz);
    }

    public int count() {
        return query.count();
    }

    public void closeConnection() {
        try {
            jdbcTemplate.getDataSource().getConnection().close();
        } catch (SQLException e) {
            throw new CannotCloseConnectionException(e);
        }
    }
}
