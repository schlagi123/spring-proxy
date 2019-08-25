package de.baswil.spring.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.Authenticator;
import java.util.Map;

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

        ProxySettings httpProxySettings = checkAndSetHttpProxy(springEnvironment, systemEnvironment);
        ProxySettings httpsProxySettings = checkAndSetHttpsProxy(springEnvironment, systemEnvironment);

        if(httpProxySettings != null && httpsProxySettings != null){
            Authenticator.setDefault(new ProxyAuthenticator());

            checkAndSetNoProxy(springEnvironment, systemEnvironment);
        }

        LOGGER.debug("PROXY LISTENER END");
    }

    private ProxySettings checkAndSetHttpProxy(ConfigurableEnvironment environment, Map<String, Object> systemEnvironment) {
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

        return proxySettings;
    }

    private ProxySettings checkAndSetHttpsProxy(ConfigurableEnvironment environment, Map<String, Object> systemEnvironment) {
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

        return proxySettings;
    }

    private void checkAndSetNoProxy(ConfigurableEnvironment environment, Map<String, Object> systemEnvironment) {
        ProxySettingsFactory proxySettingsFactory = new ProxySettingsFactory();

        String osProperty = getOsEnvironmentVariable(systemEnvironment, OS_PROP_NO_PROXY);
        String javaProperty = environment.getProperty(JAVA_PROP_HTTP_NO_PROXY_HOSTS);

        final String value = proxySettingsFactory.createNoProxyPropertyValue(osProperty, javaProperty);

        if (value != null) {
            LOGGER.info("Setup no proxy");
            setSystemProperty(JAVA_PROP_HTTP_NO_PROXY_HOSTS, value, false);
        }
    }

    private void setSystemProperty(String systemPropertyName, String systemPropertyValue, boolean password) {
        System.setProperty(systemPropertyName, systemPropertyValue);
        if(password){
            LOGGER.trace("Set system property: " + JAVA_PROP_HTTP_PROXY_HOST + " = ******");
        } else {
            LOGGER.trace("Set system property: " + JAVA_PROP_HTTP_PROXY_HOST + " = " + systemPropertyValue);
        }
    }

    private String getOsEnvironmentVariable(Map<String, Object> systemEnvironment, String name){
        Object lowerCaseValue = systemEnvironment.get(name.toLowerCase());
        Object upperCaseValue = systemEnvironment.get(name.toUpperCase());

        if(lowerCaseValue != null){
            return lowerCaseValue.toString();
        } else if(upperCaseValue != null) {
            return upperCaseValue.toString();
        } else {
            return null;
        }
    }
}
