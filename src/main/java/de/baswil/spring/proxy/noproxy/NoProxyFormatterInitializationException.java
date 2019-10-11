package de.baswil.spring.proxy.noproxy;

public class NoProxyFormatterInitializationException extends RuntimeException {
    public NoProxyFormatterInitializationException() {
    }

    public NoProxyFormatterInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProxyFormatterInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoProxyFormatterInitializationException(String message) {
        super(message);
    }

    public NoProxyFormatterInitializationException(Throwable cause) {
        super(cause);
    }
}
