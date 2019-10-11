package de.baswil.spring.proxy.noproxy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OSIncludingAllSubDomainsNoProxyFormatter implements NoProxyFormatter {
    @Override
    public List<String> formatHostName(String hostname) {
        if (hostname.startsWith(".")) {
            return Collections.singletonList("*" + hostname);
        } else if(!hostname.startsWith("*.")) {
            return Arrays.asList(hostname, "*." + hostname);
        } else {
            return Collections.singletonList(hostname);
        }
    }

    @Override
    public String hostDelimiter() {
        return ",";
    }
}
