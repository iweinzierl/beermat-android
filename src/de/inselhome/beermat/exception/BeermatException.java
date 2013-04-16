package de.inselhome.beermat.exception;

public class BeermatException extends Exception {

    public BeermatException(String s) {
        super(s);
    }

    public BeermatException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BeermatException(Throwable throwable) {
        super(throwable);
    }
}
