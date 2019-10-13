package de.baswil.spring.proxy.configuration;

/**
 * Exception thrown is the {@link ConfigurationsReader} can read/convert a configuration
 *
 * @author Bastian Wilhelm
 */
public class ConfigurationsException extends RuntimeException {
    public ConfigurationsException(String message) {
        super(message);
    }

    public ConfigurationsException(String message, Throwable cause) {
        super(message, cause);
    }
}
