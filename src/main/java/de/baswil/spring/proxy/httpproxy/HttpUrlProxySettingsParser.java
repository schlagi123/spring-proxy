package de.baswil.spring.proxy.httpproxy;

import de.baswil.spring.proxy.configuration.Configurations;
import de.baswil.spring.proxy.proxy.AbstractUrlProxySettingsParser;

public class HttpUrlProxySettingsParser extends AbstractUrlProxySettingsParser {
    private final Configurations configurations;

    public HttpUrlProxySettingsParser(Configurations configurations) {
        this.configurations = configurations;
    }

    @Override
    public String getUrl() {
        return configurations.getOsHttpProxy();
    }
}
