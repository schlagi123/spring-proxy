package de.baswil.spring.proxy.noproxy;

/**
 * Format for application property of no proxy hosts.
 *
 * @author Bastian Wilhelm
 */
public enum NoProxyFormat {
    /**
     * Format of default operation system environment variable.
     */
    OS,

    /**
     * Format of default java system property.
     */
    JAVA,

    /**
     * Other format defined in class that implements the {@link NoProxyFormatter} interface.
     */
    OTHER
}
