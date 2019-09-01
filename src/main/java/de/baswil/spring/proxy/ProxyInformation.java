package de.baswil.spring.proxy;

import org.springframework.stereotype.Component;

/**
 * Spring Component to get proxy information form the system properties.
 *
 * @author Bastian Wilhelm
 */
@Component
public class ProxyInformation {

    /**
     * @return true if a http proxy is set, otherwise false
     */
    public boolean isHttpProxySet() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_HOST) != null;
    }

    /**
     * @return the host of the http proxy
     */
    public String getHttpHost() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_HOST);
    }

    /**
     * @return the port of the http proxy
     */
    public String getHttpPort() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_PORT);
    }

    /**
     * @return the user of the http proxy
     */
    public String getHttpUser() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_USER);
    }

    /**
     * @return the password of the http proxy
     */
    public String getHttpPassword() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_PASSWORD);
    }


    /**
     * @return true if a https proxy is set, otherwise false
     */
    public boolean isHttpsProxySet() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_HOST) != null;
    }

    /**
     * @return the host of the https proxy
     */
    public String getHttpsHost() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_HOST);
    }

    /**
     * @return the port of the https proxy
     */
    public String getHttpsPort() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_PORT);
    }

    /**
     * @return the user of the https proxy
     */
    public String getHttpsUser() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_USER);
    }

    /**
     * @return the password of the https proxy
     */
    public String getHttpsPassword() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_PASSWORD);
    }


    /**
     * @return true if non proxy hosts are set, otherwise false
     */
    public boolean isNonProxyHostsSet() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_NO_PROXY_HOSTS) != null;
    }

    /**
     * @return the non proxy hosts.
     */
    public String getNonProxyHosts() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_NO_PROXY_HOSTS);
    }
}
