package de.baswil.spring.proxy.noproxy;

public interface NoProxyFormatter {
    String formatHostName(String hostname);
    String hostDelimiter();
}
