package de.baswil.spring.proxy.noproxy;

import java.util.Collections;
import java.util.List;

/**
 * Formatter for not change the no proxy hosts.
 *
 * @author Bastian Wilhelm
 * @see de.baswil.spring.proxy.noproxy.NoProxyFormat#JAVA
 */
public class NoChangeProxyFormatter implements NoProxyFormatter {
    @Override
    public List<String> formatHostName(String hostname) {
        return Collections.singletonList(hostname);
    }

    @Override
    public String hostDelimiter() {
        return null;
    }
}
