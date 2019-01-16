package pl.pg.dbclient;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.pg.dbclient.config.DbConfig;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DbClient {

    private final DbConfig dbConfig;
    private JdbcTemplate jdbcTemplate;

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

    public List<Map<String, Object>> query(String sql) throws SQLException {
        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql);
        closeConnection();
        return records;
    }

    private void closeConnection() throws SQLException {
        jdbcTemplate.getDataSource().getConnection().close();
    }

}
