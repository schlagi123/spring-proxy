package de.baswil.spring.proxy.configuration;

import de.baswil.spring.proxy.noproxy.NoProxyFormat;
import de.baswil.spring.proxy.noproxy.NoProxyFormatter;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

public class ConfigurationsReader {
    private final ConfigurableEnvironment environment;

    public ConfigurationsReader(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    public Configurations readConfigurations() {
        Configurations configurations = new Configurations();

        setOsHttpProxy(configurations);
        setOsHttpsProxy(configurations);
        setOsNoProxy(configurations);

        setAppHttpProxyHost(configurations);
        setAppHttpProxyPort(configurations);
        setAppHttpProxyUser(configurations);
        setAppHttpProxyPassword(configurations);

        setAppHttpsProxyHost(configurations);
        setAppHttpsProxyPort(configurations);
        setAppHttpsProxyUser(configurations);
        setAppHttpsProxyPassword(configurations);

        setAppNonProxyHosts(configurations);

        setAppNonProxyHostsFormat(configurations);
        setAppNonProxyHostsFormatter(configurations);

        return configurations;
    }

    private void setAppNonProxyHosts(Configurations configurations) {
        String appHttpNonProxyHosts = environment.getProperty(Constants.APP_NON_PROXY_HOSTS_PROP, String.class);
        configurations.setAppNonProxyHosts(appHttpNonProxyHosts);
    }

    private void setAppNonProxyHostsFormat(Configurations configurations) {
        NoProxyFormat appHttpNonProxyHostsFormat = environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA);
        configurations.setAppNonProxyHostsFormat(appHttpNonProxyHostsFormat);
    }

    private void setAppNonProxyHostsFormatter(Configurations configurations) {
        String appHttpNonProxyHostsFormatterName = environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMATTER_PROP, String.class);
        if (appHttpNonProxyHostsFormatterName != null) {
            try {
                Class<?> aClass = Class.forName(appHttpNonProxyHostsFormatterName);
                if(!NoProxyFormatter.class.isAssignableFrom(aClass)) {
                    throw new ConfigurationsException("Class " + aClass.getName() + " not implement " + NoProxyFormatter.class.getName());
                }
                final Class<? extends NoProxyFormatter> formatClass = aClass.asSubclass(NoProxyFormatter.class);
                configurations.setAppNonProxyHostsFormatter(formatClass);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationsException("Class " + appHttpNonProxyHostsFormatterName + " not found", e);
            }
        }
    }

    private void setAppHttpsProxyHost(Configurations configurations) {
        String appHttpsProxyHost = environment.getProperty(Constants.APP_HTTPS_PROXY_HOST_PROP, String.class);
        configurations.setAppHttpsProxyHost(appHttpsProxyHost);
    }

    private void setAppHttpsProxyPort(Configurations configurations) {
        String appHttpsProxyPort = environment.getProperty(Constants.APP_HTTPS_PROXY_PORT_PROP, String.class);
        configurations.setAppHttpsProxyPort(appHttpsProxyPort);
    }

    private void setAppHttpsProxyUser(Configurations configurations) {
        String appHttpsProxyUser = environment.getProperty(Constants.APP_HTTPS_PROXY_USER_PROP, String.class);
        configurations.setAppHttpsProxyUser(appHttpsProxyUser);
    }

    private void setAppHttpsProxyPassword(Configurations configurations) {
        String appHttpsProxyPassword = environment.getProperty(Constants.APP_HTTPS_PROXY_PASSWORD_PROP, String.class);
        configurations.setAppHttpsProxyPassword(appHttpsProxyPassword);
    }

    private void setAppHttpProxyHost(Configurations configurations) {
        String appHttpProxyHost = environment.getProperty(Constants.APP_HTTP_PROXY_HOST_PROP, String.class);
        configurations.setAppHttpProxyHost(appHttpProxyHost);
    }

    private void setAppHttpProxyPort(Configurations configurations) {
        String appHttpProxyPort = environment.getProperty(Constants.APP_HTTP_PROXY_PORT_PROP, String.class);
        configurations.setAppHttpProxyPort(appHttpProxyPort);
    }

    private void setAppHttpProxyUser(Configurations configurations) {
        String appHttpProxyUser = environment.getProperty(Constants.APP_HTTP_PROXY_USER_PROP, String.class);
        configurations.setAppHttpProxyUser(appHttpProxyUser);
    }

    private void setAppHttpProxyPassword(Configurations configurations) {
        String appHttpProxyPassword = environment.getProperty(Constants.APP_HTTP_PROXY_PASSWORD_PROP, String.class);
        configurations.setAppHttpProxyPassword(appHttpProxyPassword);
    }

    private void setOsHttpProxy(Configurations configurations) {
        String osHttpProxy = getOsVariableIgnoringCase(Constants.OS_HTTP_PROXY_PROP);
        configurations.setOsHttpProxy(osHttpProxy);
    }

    private void setOsHttpsProxy(Configurations configurations) {
        String osHttpsProxy = getOsVariableIgnoringCase(Constants.OS_HTTPS_PROXY_PROP);
        configurations.setOsHttpsProxy(osHttpsProxy);
    }

    private void setOsNoProxy(Configurations configurations) {
        String osNoProxy = getOsVariableIgnoringCase(Constants.OS_NO_PROXY_PROP);
        configurations.setOsNoProxy(osNoProxy);
    }

    private String getOsVariableIgnoringCase(String variableName) {
        Map<String, Object> systemEnvironment = environment.getSystemEnvironment();

        Object upperCaseValue = systemEnvironment.get(variableName.toLowerCase());
        Object lowerCaseValue = systemEnvironment.get(variableName.toUpperCase());

        if(upperCaseValue != null){
            return upperCaseValue.toString();
        } else if (lowerCaseValue != null) {
            return lowerCaseValue.toString();
        } else {
            return null;
        }
    }
}
