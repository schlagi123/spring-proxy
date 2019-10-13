package de.baswil.spring.proxy.noproxy;

import java.util.List;

/**
 * Formatter for the non proxy hosts application property.
 *
 * @author Bastian Wilhelm
 */
public interface NoProxyFormatter {

    /**
     * Formats a hostname for the java system properties.
     * A List will be returned if you the formatting follows in multiple result hosts.
     *
     * @param hostname host name.
     * @return list of resulting host names.
     */
    List<String> formatHostName(String hostname);

    /**
     * Delimiter for the application property value, to split it in multiple host names.
     * If null is set there is no splitting and the whole value is passed to {@link #formatHostName(String)}
     *
     * @return host name delimiter.
     */
    String hostDelimiter();
}
