package de.inselhome.beermat.exception;

public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
    }

    public NotImplementedException(String detailMessage) {
        super(detailMessage);
    }

    public NotImplementedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotImplementedException(Throwable throwable) {
        super(throwable);
    }
}
