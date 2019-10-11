package de.baswil.spring.proxy.noproxy;

import java.util.Collections;
import java.util.List;

public class OSNoProxyFormatter implements NoProxyFormatter {
    @Override
    public List<String> formatHostName(String hostname) {
        if (hostname.startsWith(".")) {
            hostname = "*" + hostname;
        }
        return Collections.singletonList(hostname);
    }

    @Override
    public String hostDelimiter() {
        return ",";
    }
}
