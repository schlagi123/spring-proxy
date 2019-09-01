package de.baswil.spring.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

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
     * @param environmentVariable
     * @param appPropertyHost
     * @param appPropertyPort
     * @param appPropertyUser
     * @param appPropertyPassword
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

    /**
     * Analyse the environment variable and the app property.
     * If an app property is set (not null) this value is the return value.
     * If no app property, but the environment variable is set,
     * the environment variable will be converted in a format for the <code>http.nonProxyHosts</code>.
     * If nothing is set null will be returned.
     *
     * @param environmentVariable value of the environment variable
     * @param appProperty value of the app property
     * @return return the effective value for the <code>http.nonProxyHosts</code>
     */
    public String createNonProxyHosts(String environmentVariable, String appProperty) {
        if (appProperty != null) {
            if (appProperty.trim().isEmpty()) {
                return null;
            } else {
                return appProperty;
            }
        } else if (environmentVariable != null) {
            String[] osPropertySplit = environmentVariable.split(",");
            StringJoiner stringJoiner = new StringJoiner("|");

            for (String osPropertyHost : osPropertySplit) {
                osPropertyHost = osPropertyHost.trim();
                if (osPropertyHost.startsWith(".")) {
                    osPropertyHost = "*" + osPropertyHost;
                }
                stringJoiner.add(osPropertyHost);
            }

            return stringJoiner.toString();
        } else {
            return null;
        }
    }
}
