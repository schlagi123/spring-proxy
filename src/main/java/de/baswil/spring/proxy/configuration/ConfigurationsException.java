package de.baswil.spring.proxy.configuration;

public class ConfigurationsException extends RuntimeException {
    public ConfigurationsException(String message) {
        super(message);
    }

    public ConfigurationsException(String message, Throwable cause) {
        super(message, cause);
    }
}
