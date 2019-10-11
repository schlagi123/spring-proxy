package de.baswil.spring.proxy;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Factory to analyse and get the effective settings of proxy hosts (with or without authentication)
 * and non proxy hosts.
 *
 * @author Bastian Wilhelm
 */
class ProxySettingsFactory {

    /**
     * Analyse the settings of an environment variable and the corresponding app-/system-properties.
     * The value of the environmentVariable (url) is parsed into host, port, user and password.
     * If app-/system-properties are set, the values of the environment variable are overridden.
     *
     * @param environmentVariable value of the environment variable
     * @param appPropertyHost     value of a host app-/system-property
     * @param appPropertyPort     value of a port app-/system-property
     * @param appPropertyUser     value of a user app-/system-property
     * @param appPropertyPassword value of a password app-/system-property
     * @return The effective proxy settings.
     */
    public ProxySettings createProxySettings(final String environmentVariable,
                                             final String appPropertyHost,
                                             final String appPropertyPort,
                                             final String appPropertyUser,
                                             final String appPropertyPassword) {

        final ProxySettings proxySettings;
        if (environmentVariable != null) {
            proxySettings = readProxySettingsFromUrl(environmentVariable);
        } else {
            proxySettings = new ProxySettings();
        }

        overrideProxySettingsWithJavaProperties(proxySettings,
                appPropertyHost, appPropertyPort, appPropertyUser, appPropertyPassword);

        if (proxySettings.getHost() != null) {
            return proxySettings;
        } else {
            return null;
        }
    }

    private void overrideProxySettingsWithJavaProperties(ProxySettings proxySettings,
                                                         String javaPropertyHost,
                                                         String javaPropertyPort,
                                                         String javaPropertyUser,
                                                         String javaPropertyPassword) {
        if (javaPropertyHost != null) {
            if (javaPropertyHost.trim().isEmpty()) {
                proxySettings.setHost(null);
            } else {
                proxySettings.setHost(javaPropertyHost);
            }
        }

        if (javaPropertyPort != null) {
            if (javaPropertyPort.trim().isEmpty()) {
                proxySettings.setPort(null);
            } else {
                try {
                    int portNumber = Integer.parseInt(javaPropertyPort);
                    if (portNumber <= 0) {
                        ProxyApplicationListener.LOGGER.warn("Find Port Number Less Then Or Equals 0. Set Port Number To null.");
                    } else {
                        proxySettings.setPort(portNumber);
                    }
                } catch (NumberFormatException e) {
                    ProxyApplicationListener.LOGGER.warn("Find Port Number That Is No Integer. Set Port Number To null.");
                }
            }
        }

        if (javaPropertyUser != null) {
            if (javaPropertyUser.trim().isEmpty()) {
                proxySettings.setUser(null);
            } else {
                proxySettings.setUser(javaPropertyUser);
            }
        }

        if (javaPropertyPassword != null) {
            if (javaPropertyPassword.trim().isEmpty()) {
                proxySettings.setPassword(null);
            } else {
                proxySettings.setPassword(javaPropertyPassword);
            }
        }

        if (proxySettings.getUser() == null && proxySettings.getPassword() != null) {
            proxySettings.setPassword(null);
        }
    }

    private ProxySettings readProxySettingsFromUrl(String urlString) {
        final ProxySettings proxySettings = new ProxySettings();

        final URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            ProxyApplicationListener.LOGGER.warn("Proxy Url Has Malformed Format. Ignore Url for proxy Parameters", e);
            return proxySettings;
        }

        if (url.getHost().trim().isEmpty()) {
            ProxyApplicationListener.LOGGER.warn("Proxy Url Has Malformed Format. Host Is Empty. Ignore Url for proxy Parameters");
            return proxySettings;
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

        return proxySettings;
    }
}
