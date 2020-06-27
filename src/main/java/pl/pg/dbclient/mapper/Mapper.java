package pl.pg.dbclient.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pl.pg.dbclient.exception.CannotReadFileException;
import pl.pg.dbclient.exception.CannotWriteValueException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Mapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void prettify() {
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String toString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new CannotWriteValueException(object, e);
        }
    }

    public static <T> T readValue(File file, Class<T> clazz) {
        try {
            return MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new CannotReadFileException(e);
        }
    }

    public static <T> T convert(Map<String, Object> map, Class<T> clazz) {
        return MAPPER.convertValue(map, clazz);
    }

    public static <T> List<T> readToList(File file, Class<T> clazz) {
        try {
            return MAPPER.readValue(file, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new CannotReadFileException(e);
        }
    }
}
