package de.baswil.spring.proxy.httpsproxy;

import de.baswil.spring.proxy.configuration.Configurations;
import de.baswil.spring.proxy.proxy.AbstractPropertyProxySettingsParser;

public class HttpsPropertyProxySettingsParser extends AbstractPropertyProxySettingsParser {
    private final Configurations configurations;

    public HttpsPropertyProxySettingsParser(Configurations configurations) {
        this.configurations = configurations;
    }

    @Override
    public String getHost() {
        return configurations.getAppHttpsProxyHost();
    }

    @Override
    public String getPort() {
        return configurations.getAppHttpsProxyPort();
    }

    @Override
    public String getUser() {
        return configurations.getAppHttpsProxyUser();
    }

    @Override
    public String getPassword() {
        return configurations.getAppHttpsProxyPassword();
    }
}
