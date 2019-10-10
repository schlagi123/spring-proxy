package de.baswil.spring.proxy.noproxy;

public class NoChangeProxyFormatter implements NoProxyFormatter {
    @Override
    public String formatHostName(String hostname) {
        return hostname;
    }

    @Override
    public String hostDelimiter() {
        return null;
    }
}
