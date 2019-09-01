package de.baswil.spring.proxy;

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
 * The system/app properties override the environment variables.</br>
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

    private static final String OS_PROP_HTTP_PROXY = "http_proxy";
    static final String JAVA_PROP_HTTP_PROXY_HOST = "http.proxyHost";
    static final String JAVA_PROP_HTTP_PROXY_PORT = "http.proxyPort";
    static final String JAVA_PROP_HTTP_PROXY_USER = "http.proxyUser";
    static final String JAVA_PROP_HTTP_PROXY_PASSWORD = "http.proxyPassword";

    private static final String OS_PROP_HTTPS_PROXY = "https_proxy";
    static final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";
    static final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";
    static final String JAVA_PROP_HTTPS_PROXY_USER = "https.proxyUser";
    static final String JAVA_PROP_HTTPS_PROXY_PASSWORD = "https.proxyPassword";

    private static final String OS_PROP_NO_PROXY = "no_proxy";
    static final String JAVA_PROP_HTTP_NO_PROXY_HOSTS = "http.nonProxyHosts";


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        LOGGER.debug("PROXY LISTENER START");

        ConfigurableEnvironment springEnvironment = event.getEnvironment();
        Map<String, Object> systemEnvironment = springEnvironment.getSystemEnvironment();

        checkAndSetHttpProxy(springEnvironment, systemEnvironment);
        checkAndSetHttpsProxy(springEnvironment, systemEnvironment);

        checkAndSetNoProxy(springEnvironment, systemEnvironment);

        LOGGER.debug("PROXY LISTENER END");
    }

    private void checkAndSetHttpProxy(ConfigurableEnvironment environment, Map<String, Object> systemEnvironment) {
        final ProxySettingsFactory proxySettingsFactory = new ProxySettingsFactory();

        final String osProperty = getOsEnvironmentVariable(systemEnvironment, OS_PROP_HTTP_PROXY);
        final String javaPropertyHost = environment.getProperty(JAVA_PROP_HTTP_PROXY_HOST);
        final String javaPropertyPort = environment.getProperty(JAVA_PROP_HTTP_PROXY_PORT);
        final String javaPropertyUser = environment.getProperty(JAVA_PROP_HTTP_PROXY_USER);
        final String javaPropertyPassword = environment.getProperty(JAVA_PROP_HTTP_PROXY_PASSWORD);

        final ProxySettings proxySettings = proxySettingsFactory.createProxySettings(osProperty,
                javaPropertyHost, javaPropertyPort, javaPropertyUser, javaPropertyPassword);

        if (proxySettings == null) {
            LOGGER.debug("No http proxy settings found");
        } else {
            LOGGER.info("Setup http proxy");
            setSystemProperty(JAVA_PROP_HTTP_PROXY_HOST, proxySettings.getHost(), false);
            if (proxySettings.getPort() != null) {
                setSystemProperty(JAVA_PROP_HTTP_PROXY_PORT, String.valueOf(proxySettings.getPort()), false);
            }
            if (proxySettings.getUser() != null) {
                setSystemProperty(JAVA_PROP_HTTP_PROXY_USER, proxySettings.getUser(), false);
            }
            if (proxySettings.getPassword() != null) {
                setSystemProperty(JAVA_PROP_HTTP_PROXY_PASSWORD, proxySettings.getPassword(), true);
            }
        }

    }

    private void checkAndSetHttpsProxy(ConfigurableEnvironment environment, Map<String, Object> systemEnvironment) {
        final ProxySettingsFactory proxySettingsFactory = new ProxySettingsFactory();

        final String osProperty = getOsEnvironmentVariable(systemEnvironment, OS_PROP_HTTPS_PROXY);
        final String javaPropertyHost = environment.getProperty(JAVA_PROP_HTTPS_PROXY_HOST);
        final String javaPropertyPort = environment.getProperty(JAVA_PROP_HTTPS_PROXY_PORT);
        final String javaPropertyUser = environment.getProperty(JAVA_PROP_HTTPS_PROXY_USER);
        final String javaPropertyPassword = environment.getProperty(JAVA_PROP_HTTPS_PROXY_PASSWORD);

        final ProxySettings proxySettings = proxySettingsFactory.createProxySettings(osProperty,
                javaPropertyHost, javaPropertyPort, javaPropertyUser, javaPropertyPassword);

        if (proxySettings == null) {
            LOGGER.debug("No https proxy settings found");
        } else {
            LOGGER.info("Setup https proxy");
            setSystemProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxySettings.getHost(), false);
            if (proxySettings.getPort() != null) {
                setSystemProperty(JAVA_PROP_HTTPS_PROXY_PORT, String.valueOf(proxySettings.getPort()), false);
            }
            if (proxySettings.getUser() != null) {
                setSystemProperty(JAVA_PROP_HTTPS_PROXY_USER, proxySettings.getUser(), false);
            }
            if (proxySettings.getPassword() != null) {
                setSystemProperty(JAVA_PROP_HTTPS_PROXY_PASSWORD, proxySettings.getPassword(), true);
            }
        }

    }

    private void checkAndSetNoProxy(ConfigurableEnvironment environment, Map<String, Object> systemEnvironment) {
        ProxySettingsFactory proxySettingsFactory = new ProxySettingsFactory();

        String osProperty = getOsEnvironmentVariable(systemEnvironment, OS_PROP_NO_PROXY);
        String javaProperty = environment.getProperty(JAVA_PROP_HTTP_NO_PROXY_HOSTS);

        final String value = proxySettingsFactory.createNonProxyHosts(osProperty, javaProperty);

        if (value != null) {
            LOGGER.info("Setup no proxy");
            setSystemProperty(JAVA_PROP_HTTP_NO_PROXY_HOSTS, value, false);
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
