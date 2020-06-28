package pl.pg.dbclient.mapper;

import pl.pg.dbclient.annotation.Column;
import pl.pg.dbclient.exception.CannotInitializeObjectExpcetion;
import pl.pg.dbclient.exception.CannotWriteFieldValueException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ColumnMapper<T> {

    public T map(Map<String, Object> map, Class<T> clazz) {
        
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        boolean hasSpecifiedColumnName = fields.stream().anyMatch(field -> field.isAnnotationPresent(Column.class));
        if(hasSpecifiedColumnName) {
            return convertWithSpecifiedColumnName(map, fields, clazz);
        } else {
           return Mapper.convert(map, clazz);
        }
    }

    private T convertWithSpecifiedColumnName(Map<String, Object> map, List<Field> fields, Class<T> clazz) {
        T object = initializeObject(clazz);
        fields.stream()
                .forEach(field -> {
                    String columnName;
                    if(field.isAnnotationPresent(Column.class)) {
                        columnName = field.getAnnotation(Column.class).value();
                    } else {
                        columnName = field.getName();
                    }
                    setFieldValue(object, field, map.get(columnName));
                });
        return object;
    }

    private void setFieldValue(T object, Field field, Object recordValue) {
        field.setAccessible(true);
        Class<?> type = field.getType();
        try {
            field.set(object, Mapper.convertValue(recordValue, type));
        } catch (IllegalAccessException e) {
            throw new CannotWriteFieldValueException(
                    "Class: ".concat(object.getClass().getName()).concat(" field: ").concat(field.getName()),
                    e);
        }
    }

    private T initializeObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CannotInitializeObjectExpcetion(e);
        }
    }
}
