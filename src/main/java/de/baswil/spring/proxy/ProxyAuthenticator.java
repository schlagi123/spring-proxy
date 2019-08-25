package de.baswil.spring.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

//https://rolandtapken.de/blog/2012-04/java-process-httpproxyuser-and-httpproxypassword
public class ProxyAuthenticator extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType() == RequestorType.PROXY) {
            String prot = getRequestingProtocol().toLowerCase();
            String host = System.getProperty(prot + ".proxyHost", "");
            String port = System.getProperty(prot + ".proxyPort", "");
            String user = System.getProperty(prot + ".proxyUser", "");
            String password = System.getProperty(prot + ".proxyPassword", "");

            if (getRequestingHost().toLowerCase().equals(host.toLowerCase())) {
                if (Integer.parseInt(port) == getRequestingPort()) {
                    // Seems to be OK.
                    return new PasswordAuthentication(user, password.toCharArray());
                }
            }
        }
        return null;
    }
}
