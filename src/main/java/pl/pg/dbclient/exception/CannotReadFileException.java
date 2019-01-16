package pl.pg.dbclient.exception;

import java.io.IOException;

public class CannotReadFileException extends RuntimeException {

    public CannotReadFileException(IOException e) {
        super(e);
    }
}
