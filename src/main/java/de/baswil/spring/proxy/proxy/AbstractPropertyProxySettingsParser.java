package de.baswil.spring.proxy.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Analyze the Application properties for proxies (http or https)
 *
 * @author Bastian Wilhelm
 */
public abstract class AbstractPropertyProxySettingsParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPropertyProxySettingsParser.class);

    /**
     * Get the host value of the application properties.
     *
     * @return host
     */
    public abstract String getHost();

    /**
     * Get the port value of the application properties.
     *
     * @return port
     */
    public abstract String getPort();

    /**
     * Get the user value of the application properties.
     *
     * @return user
     */
    public abstract String getUser();

    /**
     * Get the password value of the application properties.
     *
     * @return password
     */
    public abstract String getPassword();

    /**
     * Analyze the url of the application variables and save the result in the {@link ProxySettings} object.
     *
     * @param proxySettings The settings object for the result of the analyze.
     */
    public void readProxySettingsFromProperties(ProxySettings proxySettings) {
        String host = getHost();
        String port = getPort();
        String user = getUser();
        String password = getPassword();

        if (host != null) {
            if (host.trim().isEmpty()) {
                proxySettings.setHost(null);
            } else {
                proxySettings.setHost(host);
            }
        }

        if (port != null) {
            if (port.trim().isEmpty()) {
                proxySettings.setPort(null);
            } else {
                try {
                    int portNumber = Integer.parseInt(port);
                    if (portNumber <= 0) {
                        LOGGER.warn("Find Port Number Less Then Or Equals 0. Set Port Number To null.");
                    } else {
                        proxySettings.setPort(portNumber);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warn("Find Port Number That Is No Integer. Set Port Number To null.");
                }
            }
        }

        if (user != null) {
            if (user.trim().isEmpty()) {
                proxySettings.setUser(null);
            } else {
                proxySettings.setUser(user);
            }
        }

        if (password != null) {
            if (password.trim().isEmpty()) {
                proxySettings.setPassword(null);
            } else {
                proxySettings.setPassword(password);
            }
        }
    }
}
