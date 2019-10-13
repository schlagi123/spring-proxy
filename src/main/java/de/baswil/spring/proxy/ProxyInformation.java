package de.baswil.spring.proxy;

import de.baswil.spring.proxy.configuration.Constants;
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
        return System.getProperty(Constants.JAVA_HTTP_PROXY_HOST_PROP) != null;
    }

    /**
     * @return the host of the http proxy
     */
    public String getHttpHost() {
        return System.getProperty(Constants.JAVA_HTTP_PROXY_HOST_PROP);
    }

    /**
     * @return the port of the http proxy
     */
    public String getHttpPort() {
        return System.getProperty(Constants.JAVA_HTTP_PROXY_PORT_PROP);
    }

    /**
     * @return the user of the http proxy
     */
    public String getHttpUser() {
        return System.getProperty(Constants.JAVA_HTTP_PROXY_USER_PROP);
    }

    /**
     * @return the password of the http proxy
     */
    public String getHttpPassword() {
        return System.getProperty(Constants.JAVA_HTTP_PROXY_PASSWORD_PROP);
    }


    /**
     * @return true if a https proxy is set, otherwise false
     */
    public boolean isHttpsProxySet() {
        return System.getProperty(Constants.JAVA_HTTPS_PROXY_HOST_PROP) != null;
    }

    /**
     * @return the host of the https proxy
     */
    public String getHttpsHost() {
        return System.getProperty(Constants.JAVA_HTTPS_PROXY_HOST_PROP);
    }

    /**
     * @return the port of the https proxy
     */
    public String getHttpsPort() {
        return System.getProperty(Constants.JAVA_HTTPS_PROXY_PORT_PROP);
    }

    /**
     * @return the user of the https proxy
     */
    public String getHttpsUser() {
        return System.getProperty(Constants.JAVA_HTTPS_PROXY_USER_PROP);
    }

    /**
     * @return the password of the https proxy
     */
    public String getHttpsPassword() {
        return System.getProperty(Constants.JAVA_HTTPS_PROXY_PASSWORD_PROP);
    }


    /**
     * @return true if non proxy hosts are set, otherwise false
     */
    public boolean isNonProxyHostsSet() {
        return System.getProperty(Constants.JAVA_NON_PROXY_HOSTS_PROP) != null;
    }

    /**
     * @return the non proxy hosts.
     */
    public String getNonProxyHosts() {
        return System.getProperty(Constants.JAVA_NON_PROXY_HOSTS_PROP);
    }
}
