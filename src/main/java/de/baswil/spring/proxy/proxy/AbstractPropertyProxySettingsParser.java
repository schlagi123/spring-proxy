package de.baswil.spring.proxy.proxy;

import de.baswil.spring.proxy.ProxyApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractPropertyProxySettingsParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPropertyProxySettingsParser.class);

    public abstract String getHost();

    public abstract String getPort();

    public abstract String getUser();

    public abstract String getPassword();

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
