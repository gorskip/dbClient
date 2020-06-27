package pl.pg.dbclient.exception;

import java.sql.SQLException;

public class CannotCloseConnectionException extends RuntimeException {
    public CannotCloseConnectionException(SQLException e) {
    }
}
