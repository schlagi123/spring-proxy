package de.baswil.spring.proxy.proxy;

/**
 *  Analyzed proxy settings from the environment variables and application properties ({@link de.baswil.spring.proxy.configuration.Configurations})
 *
 * @author Bastian Wilhelm
 */
public class ProxySettingsAnalyzer {
    private final AbstractUrlProxySettingsParser urlProxySettingsParser;
    private final AbstractPropertyProxySettingsParser propertyProxySettingsParser;

    /**
     * Create Object
     *
     * @param urlProxySettingsParser for analyzing the environment variable
     * @param propertyProxySettingsParser for analyzing the application properties
     */
    public ProxySettingsAnalyzer(AbstractUrlProxySettingsParser urlProxySettingsParser,
                                 AbstractPropertyProxySettingsParser propertyProxySettingsParser) {
        this.urlProxySettingsParser = urlProxySettingsParser;
        this.propertyProxySettingsParser = propertyProxySettingsParser;
    }

    /**
     * Analyze all environment variables and application properties for a proxy (http or https).
     *
     * @return Result of the analyze.
     */
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
