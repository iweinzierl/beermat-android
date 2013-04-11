package de.inselhome.beermat.exception;

public class BillPersistenceException extends Exception {

    public BillPersistenceException(String s) {
        super(s);
    }

    public BillPersistenceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BillPersistenceException(Throwable throwable) {
        super(throwable);
    }
}
