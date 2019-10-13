package de.baswil.spring.proxy.noproxy;

/**
 * Throws if the initialization of NoProxyFormatters fails.
 *
 * @author Bastian Wilhelm
 */
public class NoProxyFormatterInitializationException extends RuntimeException {
    public NoProxyFormatterInitializationException(String message) {
        super(message);
    }

    public NoProxyFormatterInitializationException(Throwable cause) {
        super(cause);
    }
}
