package de.baswil.spring.proxy.noproxy;

import java.util.List;

public interface NoProxyFormatter {
    List<String> formatHostName(String hostname);
    String hostDelimiter();
}
