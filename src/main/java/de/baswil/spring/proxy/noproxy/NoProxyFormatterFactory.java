package de.baswil.spring.proxy.noproxy;

import org.springframework.core.env.Environment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NoProxyFormatterFactory {
    public static final String FORMAT_PROPERTY_NAME = "http.nonProxyHosts.format";
    public static final String FORMATTER_PROPERTY_NAME = "http.nonProxyHosts.formatter";

    private final Environment environment;

    public NoProxyFormatterFactory(Environment environment) {
        this.environment = environment;
    }

    public NoProxyFormatter createNoProxyFormatterForOSProperty() {
        return new OSNoProxyFormatter();
    }

    public NoProxyFormatter createNoProxyFormatterFromProperties() {
        final NoProxyFormat format = environment.getProperty(FORMAT_PROPERTY_NAME, NoProxyFormat.class, NoProxyFormat.JAVA);

        if(format == NoProxyFormat.OS) {
            return createNoProxyFormatterForOSProperty();
        } else if(format == NoProxyFormat.JAVA) {
            return new NoChangeProxyFormatter();
        } else if (format == NoProxyFormat.OTHER){
            final String otherNoProxyFormatter = environment.getProperty(FORMATTER_PROPERTY_NAME, String.class);
            if(otherNoProxyFormatter == null){
                throw new NoProxyFormatterInitializationException();
            }

            try {
                final Class<?> aClass = Class.forName(otherNoProxyFormatter);
                if(!NoProxyFormatter.class.isAssignableFrom(aClass)) {
                    throw new NoProxyFormatterInitializationException();
                }
                final Class<? extends NoProxyFormatter> formatClass = aClass.asSubclass(NoProxyFormatter.class);
                final Constructor<? extends NoProxyFormatter> defaultConstructor = formatClass.getConstructor();
                return defaultConstructor.newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new NoProxyFormatterInitializationException(e);
            }
        } else {
            throw new NoProxyFormatterInitializationException();
        }
    }
}
