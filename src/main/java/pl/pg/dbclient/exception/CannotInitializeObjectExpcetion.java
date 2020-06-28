package pl.pg.dbclient.exception;

public class CannotInitializeObjectExpcetion extends RuntimeException {
    public CannotInitializeObjectExpcetion(ReflectiveOperationException e) {
    }
}
