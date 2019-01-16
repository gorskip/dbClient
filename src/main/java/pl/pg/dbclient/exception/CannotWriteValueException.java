package pl.pg.dbclient.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CannotWriteValueException extends RuntimeException {

    public CannotWriteValueException(Object object, JsonProcessingException e) {
        super(object.toString(), e);
    }
}
