package de.baswil.spring.proxy.httpproxy;

import de.baswil.spring.proxy.configuration.Configurations;
import de.baswil.spring.proxy.proxy.AbstractPropertyProxySettingsParser;

/**
 * Implementation for http proxy.
 *
 * @author Bastian Wilhelm
 */
public class HttpPropertyProxySettingsParser extends AbstractPropertyProxySettingsParser {
    private final Configurations configurations;

    public HttpPropertyProxySettingsParser(Configurations configurations) {
        this.configurations = configurations;
    }

    @Override
    public String getHost() {
        return configurations.getAppHttpProxyHost();
    }

    @Override
    public String getPort() {
        return configurations.getAppHttpProxyPort();
    }

    @Override
    public String getUser() {
        return configurations.getAppHttpProxyUser();
    }

    @Override
    public String getPassword() {
        return configurations.getAppHttpProxyPassword();
    }
}
