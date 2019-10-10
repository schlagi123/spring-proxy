package de.baswil.spring.proxy.noproxy;

public class OSNoProxyFormatter implements NoProxyFormatter {
    @Override
    public String formatHostName(String hostname) {
        if (hostname.startsWith(".")) {
            hostname = "*" + hostname;
        }
        return hostname;
    }

    @Override
    public String hostDelimiter() {
        return ",";
    }
}
