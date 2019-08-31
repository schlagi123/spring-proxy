package de.baswil.spring.proxy;

import org.springframework.stereotype.Component;

@Component
public class ProxyInformation {

    public boolean isHttpProxySet(){
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_HOST) != null;
    }

    public String getHttpHost() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_HOST);
    }

    public String getHttpPort() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_PORT);
    }

    public String getHttpUser() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_USER);
    }

    public String getHttpPassword() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_PROXY_PASSWORD);
    }


    public boolean isHttpsProxySet(){
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_HOST) != null;
    }

    public String getHttpsHost() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_HOST);
    }

    public String getHttpsPort() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_PORT);
    }

    public String getHttpsUser() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_USER);
    }

    public String getHttpsPassword() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTPS_PROXY_PASSWORD);
    }


    public boolean isNonProxyHostsSet() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_NO_PROXY_HOSTS) != null;
    }

    public String getNonProxyHosts() {
        return System.getProperty(ProxyApplicationListener.JAVA_PROP_HTTP_NO_PROXY_HOSTS);
    }
}
