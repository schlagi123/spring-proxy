package de.baswil.spring.proxy.noproxy;

import de.baswil.spring.proxy.configuration.Configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory for creating a {@link NoProxyFormatter} instance.
 *
 * @author Bastian Wilhelm
 */
public class NoProxyFormatterFactory {
    private final Configurations configurations;

    public NoProxyFormatterFactory(Configurations configurations) {
        this.configurations = configurations;
    }

    /**
     * Create default instance for formatting operation system environment variable.
     *
     * @return instance
     */
    public NoProxyFormatter createNoProxyFormatterForOSProperty() {
        return new OSNoProxyFormatter();
    }

    /**
     * Create instance for formatting non proxy hosts based on formatting application properties.
     *
     * @throws NoProxyFormatterInitializationException if initialization of custom {@link NoProxyFormatter} fails.
     * @return instance
     */
    public NoProxyFormatter createNoProxyFormatterFromProperties() {
        final NoProxyFormat format = configurations.getAppNonProxyHostsFormat();

        if (format == NoProxyFormat.OS) {
            return createNoProxyFormatterForOSProperty();
        } else if (format == NoProxyFormat.JAVA || format == null) {
            return new NoChangeProxyFormatter();
        } else {
            // NoProxyFormat.OTHER
            try {
                Class<? extends NoProxyFormatter> formatterClass = configurations.getAppNonProxyHostsFormatter();
                if (formatterClass == null) {
                    throw new NoProxyFormatterInitializationException("If NoProxyFormat.OTHER is used a no proxy Formatter must be used.");
                }
                final Constructor<? extends NoProxyFormatter> defaultConstructor = formatterClass.getConstructor();
                return defaultConstructor.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new NoProxyFormatterInitializationException(e);
            }
        }
    }
}
