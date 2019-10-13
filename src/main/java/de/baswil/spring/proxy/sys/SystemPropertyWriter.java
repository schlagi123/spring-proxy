package de.baswil.spring.proxy.sys;

import de.baswil.spring.proxy.configuration.Constants;
import de.baswil.spring.proxy.proxy.ProxySettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemPropertyWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemPropertyWriter.class);

    public void writeHttpProxySettings(ProxySettings httpProxySettings) {
        if (httpProxySettings == null) {
            LOGGER.debug("No http proxy settings found");
        } else {
            LOGGER.info("Setup http proxy");
            writeProxySettings(
                    httpProxySettings,
                    Constants.JAVA_HTTP_PROXY_HOST_PROP,
                    Constants.JAVA_HTTP_PROXY_PORT_PROP,
                    Constants.JAVA_HTTP_PROXY_USER_PROP,
                    Constants.JAVA_HTTP_PROXY_PASSWORD_PROP
            );
        }
    }

    public void writeHttpsProxySettings(ProxySettings httpsProxySettings) {
        if (httpsProxySettings == null) {
            LOGGER.debug("No http proxy settings found");
        } else {
            LOGGER.info("Setup http proxy");
            writeProxySettings(
                    httpsProxySettings,
                    Constants.JAVA_HTTPS_PROXY_HOST_PROP,
                    Constants.JAVA_HTTPS_PROXY_PORT_PROP,
                    Constants.JAVA_HTTPS_PROXY_USER_PROP,
                    Constants.JAVA_HTTPS_PROXY_PASSWORD_PROP
            );
        }
    }

    private void writeProxySettings(ProxySettings proxySettings, String hostKey, String portKey, String userKey, String passwordKey) {
        setSystemProperty(hostKey, proxySettings.getHost(), false);
        if (proxySettings.getPort() != null) {
            setSystemProperty(portKey, String.valueOf(proxySettings.getPort()), false);
        }
        if (proxySettings.getUser() != null) {
            setSystemProperty(userKey, proxySettings.getUser(), false);
        }
        if (proxySettings.getPassword() != null) {
            setSystemProperty(passwordKey, proxySettings.getPassword(), true);
        }
    }

    public void writeNoProxySettings(String noProxySettings) {
        if (noProxySettings != null) {
            LOGGER.info("Setup no proxy");
            setSystemProperty(Constants.JAVA_NON_PROXY_HOSTS_PROP, noProxySettings, false);
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
}
