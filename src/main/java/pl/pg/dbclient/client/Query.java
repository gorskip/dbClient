package pl.pg.dbclient.client;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.pg.dbclient.exception.CannotInitializeObjectExpcetion;
import pl.pg.dbclient.mapper.ColumnMapper;
import pl.pg.dbclient.mapper.Mapper;
import pl.pg.dbclient.mapper.RecordMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Query {

    private final JdbcTemplate jdbcTemplate;
    private final String sql;

    public Query(JdbcTemplate jdbcTemplate, String sql) {
        this.jdbcTemplate = jdbcTemplate;
        this.sql = sql;
    }

    public <T> List<T> asList(Class<T> clazz) {
        if(clazz.isAnnotationPresent(pl.pg.dbclient.annotation.Mapper.class)) {
            Class<? extends RecordMapper> rowMapperClass = clazz.getAnnotation(pl.pg.dbclient.annotation.Mapper.class).value();
            return jdbcTemplate.query(sql, initializeRowMapper(rowMapperClass));
        } else {
            return asList().stream()
                    .map(result -> new ColumnMapper<T>().map(result, clazz))
                    .collect(Collectors.toList());
        }
    }

    public List<Map<String, Object>> asList() {
        return jdbcTemplate.queryForList(sql);
    }

    public String asString() {
        return Mapper.toString(jdbcTemplate.queryForList(sql));
    }

    public int count() {
        return asList().size();
    }

    private RecordMapper initializeRowMapper(Class<? extends RecordMapper> rowMapperClass){
        try {
            return rowMapperClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CannotInitializeObjectExpcetion(e);
        }
    }
}
