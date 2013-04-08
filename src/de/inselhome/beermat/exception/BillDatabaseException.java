package de.inselhome.beermat.exception;

public class BillDatabaseException extends Exception {

    public BillDatabaseException(String s) {
        super(s);
    }

    public BillDatabaseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BillDatabaseException(Throwable throwable) {
        super(throwable);
    }
}
