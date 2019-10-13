package de.baswil.spring.proxy;

import de.baswil.spring.proxy.configuration.Configurations;
import de.baswil.spring.proxy.configuration.ConfigurationsReader;
import de.baswil.spring.proxy.configuration.Constants;
import de.baswil.spring.proxy.noproxy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * <p>
 * A SpringBoot ApplicationListener to read the environment variables
 * <code>http_proxy</code>, <code>https_proxy</code>, <code>no_proxy</code>
 * and the system/app properties
 * <code>http.proxyHost</code>, <code>http.proxyPort</code>,
 * <code>http.proxyUser</code>, <code>http.proxyPassword</code>,
 * <code>https.proxyHost</code>, <code>https.proxyPort</code>,
 * <code>https.proxyUser</code>, <code>https.proxyPassword</code>,
 * <code>http.nonProxyHosts</code>
 * </p>
 * <p>
 * The system/app properties override the environment variables.
 * </p>
 * <p>
 * At the end the system properties
 * <code>http.proxyHost</code>, <code>http.proxyPort</code>,
 * <code>http.proxyUser</code>, <code>http.proxyPassword</code>,
 * <code>https.proxyHost</code>, <code>https.proxyPort</code>,
 * <code>https.proxyUser</code>, <code>https.proxyPassword</code>,
 * <code>http.nonProxyHosts</code>
 * are set, if values are available in the environment variables or system/app properties.
 * </p>
 *
 * @author Bastian Wilhelm
 */
public class ProxyApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    static final Logger LOGGER = LoggerFactory.getLogger(ProxyApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        LOGGER.debug("PROXY LISTENER START");

        ConfigurationsReader configurationsReader = new ConfigurationsReader(event.getEnvironment());
        Configurations configurations = configurationsReader.readConfigurations();

        checkAndSetHttpProxy(configurations);
        checkAndSetHttpsProxy(configurations);
        checkAndSetNoProxy(configurations);

        LOGGER.debug("PROXY LISTENER END");
    }

    private void checkAndSetHttpProxy(Configurations configurations) {
        final ProxySettingsFactory proxySettingsFactory = new ProxySettingsFactory();

        final ProxySettings proxySettings = proxySettingsFactory.createProxySettings(
                configurations.getOsHttpProxy(),
                configurations.getAppHttpProxyHost(),
                configurations.getAppHttpProxyPort(),
                configurations.getAppHttpProxyUser(),
                configurations.getAppHttpProxyPassword()
        );

        if (proxySettings == null) {
            LOGGER.debug("No http proxy settings found");
        } else {
            LOGGER.info("Setup http proxy");
            setSystemProperty(Constants.JAVA_HTTP_PROXY_HOST_PROP, proxySettings.getHost(), false);
            if (proxySettings.getPort() != null) {
                setSystemProperty(Constants.JAVA_HTTP_PROXY_PORT_PROP, String.valueOf(proxySettings.getPort()), false);
            }
            if (proxySettings.getUser() != null) {
                setSystemProperty(Constants.JAVA_HTTP_PROXY_USER_PROP, proxySettings.getUser(), false);
            }
            if (proxySettings.getPassword() != null) {
                setSystemProperty(Constants.JAVA_HTTP_PROXY_PASSWORD_PROP, proxySettings.getPassword(), true);
            }
        }

    }

    private void checkAndSetHttpsProxy(Configurations configurations) {
        final ProxySettingsFactory proxySettingsFactory = new ProxySettingsFactory();

        final ProxySettings proxySettings = proxySettingsFactory.createProxySettings(
                configurations.getOsHttpsProxy(),
                configurations.getAppHttpsProxyHost(),
                configurations.getAppHttpsProxyPort(),
                configurations.getAppHttpsProxyUser(),
                configurations.getAppHttpsProxyPassword()
        );

        if (proxySettings == null) {
            LOGGER.debug("No https proxy settings found");
        } else {
            LOGGER.info("Setup https proxy");
            setSystemProperty(Constants.JAVA_HTTPS_PROXY_HOST_PROP, proxySettings.getHost(), false);
            if (proxySettings.getPort() != null) {
                setSystemProperty(Constants.JAVA_HTTPS_PROXY_PORT_PROP, String.valueOf(proxySettings.getPort()), false);
            }
            if (proxySettings.getUser() != null) {
                setSystemProperty(Constants.JAVA_HTTPS_PROXY_USER_PROP, proxySettings.getUser(), false);
            }
            if (proxySettings.getPassword() != null) {
                setSystemProperty(Constants.JAVA_HTTPS_PROXY_PASSWORD_PROP, proxySettings.getPassword(), true);
            }
        }

    }

    private void checkAndSetNoProxy(Configurations configurations) {
        NoProxyFormatterFactory formatterFactory = new NoProxyFormatterFactory(configurations);
        NoProxyHostsConverter converter = new NoProxyHostsConverter(formatterFactory);
        String value = converter.convert(
                configurations.getOsNoProxy(),
                configurations.getAppNonProxyHosts()
        );

        if (value != null) {
            LOGGER.info("Setup no proxy");
            setSystemProperty(Constants.JAVA_NON_PROXY_HOSTS_PROP, value, false);
        }
    }

    private void setSystemProperty(String systemPropertyName, String systemPropertyValue, boolean password) {
        System.setProperty(systemPropertyName, systemPropertyValue);
        if (password) {
            LOGGER.trace("Set system property: {} = ******", systemPropertyName);
        } else {
            LOGGER.trace("Set system property: {} = {}", systemPropertyName, systemPropertyValue);
        }
    }

    private String getOsEnvironmentVariable(Map<String, Object> systemEnvironment, String name) {
        Object lowerCaseValue = systemEnvironment.get(name.toLowerCase());
        Object upperCaseValue = systemEnvironment.get(name.toUpperCase());

        if (lowerCaseValue != null) {
            return lowerCaseValue.toString();
        } else if (upperCaseValue != null) {
            return upperCaseValue.toString();
        } else {
            return null;
        }
    }
}
