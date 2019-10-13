package de.baswil.spring.proxy.proxy;

public class ProxySettingsAnalyzer {
    private final AbstractUrlProxySettingsParser urlProxySettingsParser;
    private final AbstractPropertyProxySettingsParser propertyProxySettingsParser;

    public ProxySettingsAnalyzer(AbstractUrlProxySettingsParser urlProxySettingsParser,
                                 AbstractPropertyProxySettingsParser propertyProxySettingsParser) {
        this.urlProxySettingsParser = urlProxySettingsParser;
        this.propertyProxySettingsParser = propertyProxySettingsParser;
    }

    public ProxySettings analyze(){
        final ProxySettings proxySettings = new ProxySettings();

        urlProxySettingsParser.readProxySettingsFromUrl(proxySettings);
        propertyProxySettingsParser.readProxySettingsFromProperties(proxySettings);

        if (proxySettings.getHost() != null) {
            if (proxySettings.getUser() == null && proxySettings.getPassword() != null) {
                proxySettings.setPassword(null);
            }
            return proxySettings;
        } else {
            return null;
        }
    }
}
