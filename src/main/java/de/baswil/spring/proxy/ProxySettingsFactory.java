package de.baswil.spring.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

class ProxySettingsFactory {

    public ProxySettings createProxySettings(final String osProperty,
                                             final String javaPropertyHost,
                                             final String javaPropertyPort,
                                             final String javaPropertyUser,
                                             final String javaPropertyPassword) {

        final ProxySettings proxySettings;
        if (osProperty != null) {
            proxySettings = readProxySettingsFromUrl(osProperty);
        } else {
            proxySettings = new ProxySettings();
        }

        overrideProxySettingsWithJavaProperties(proxySettings,
                javaPropertyHost, javaPropertyPort, javaPropertyUser, javaPropertyPassword);

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
            if(javaPropertyHost.trim().isEmpty()){
                proxySettings.setHost(null);
            } else {
                proxySettings.setHost(javaPropertyHost);
            }
        }

        if (javaPropertyPort != null) {
            if(javaPropertyPort.trim().isEmpty()){
                proxySettings.setPort(null);
            } else {
                try {
                    int portNumber = Integer.parseInt(javaPropertyPort);
                    if(portNumber < 0) {
                        ProxyApplicationListener.LOGGER.warn("Find Port Number Less Then 0. Set Port Number To null.");
                        proxySettings.setPort(null);
                    } else {
                        proxySettings.setPort(portNumber);
                    }
                } catch (NumberFormatException e){
                    ProxyApplicationListener.LOGGER.warn("Find Port Number That Is No Integer. Set Port Number To null.");
                    proxySettings.setPort(null);
                }
            }
        }

        if (javaPropertyUser != null) {
            if(javaPropertyUser.trim().isEmpty()){
                proxySettings.setUser(null);
                proxySettings.setPassword(null);
            } else {
                proxySettings.setUser(javaPropertyUser);
            }
        }

        if (javaPropertyPassword != null) {
            if(javaPropertyPassword.trim().isEmpty()){
                proxySettings.setPassword(null);
            } else {
                if(proxySettings.getUser() == null){
                    proxySettings.setPassword(null);
                } else {
                    proxySettings.setPassword(javaPropertyPassword);
                }
            }
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

        if(url.getHost().trim().isEmpty()){
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

    public String createNoProxy(String osProperty, String javaProperty) {
        if (javaProperty != null) {
            if(javaProperty.trim().isEmpty()){
                return null;
            } else {
                return javaProperty;
            }
        } else if (osProperty != null) {
            String[] osPropertySplit = osProperty.split(",");
            StringJoiner stringJoiner = new StringJoiner("|");

            for (String osPropertyHost : osPropertySplit) {
                osPropertyHost = osPropertyHost.trim();
                if(osPropertyHost.startsWith(".")){
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
