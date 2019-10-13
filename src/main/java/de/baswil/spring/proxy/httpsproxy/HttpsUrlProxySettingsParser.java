package de.baswil.spring.proxy.httpsproxy;

import de.baswil.spring.proxy.configuration.Configurations;
import de.baswil.spring.proxy.proxy.AbstractUrlProxySettingsParser;

public class HttpsUrlProxySettingsParser extends AbstractUrlProxySettingsParser {
    private final Configurations configurations;

    public HttpsUrlProxySettingsParser(Configurations configurations) {
        this.configurations = configurations;
    }

    @Override
    public String getUrl() {
        return configurations.getOsHttpsProxy();
    }
}
