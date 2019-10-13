package de.baswil.spring.proxy.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Analyze the Environment variable for proxies (http or https)
 *
 * @author Bastian Wilhelm
 */
public abstract class AbstractUrlProxySettingsParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUrlProxySettingsParser.class);

    /**
     * Get the value (url) of the environment variable.
     *
     * @return url
     */
    public abstract String getUrl();

    /**
     * Analyze the url of the environment variable and save the result in the {@link ProxySettings} object.
     *
     * @param proxySettings The settings object for the result of the analyze.
     */
    public void readProxySettingsFromUrl(ProxySettings proxySettings) {
        String urlString = getUrl();
        if(urlString == null){
            return;
        }

        final URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            LOGGER.warn("Wrong proxy url format. Ignore url for proxy settings.", e);
            return;
        }

        if (url.getHost().trim().isEmpty()) {
            LOGGER.warn("Proxy url has no hostname. Ignore url for proxy settings.");
            return;
        }

        proxySettings.setHost(url.getHost());

        if (url.getPort() != -1) {
            proxySettings.setPort(url.getPort());
        }

        if (url.getUserInfo() != null) {
            String[] userInfoSplit = url.getUserInfo().split(":", 2);
            if (userInfoSplit.length == 1) {
                proxySettings.setUser(userInfoSplit[0]);
            } else {
                proxySettings.setUser(userInfoSplit[0]);
                proxySettings.setPassword(userInfoSplit[1]);
            }
        }
    }
}
