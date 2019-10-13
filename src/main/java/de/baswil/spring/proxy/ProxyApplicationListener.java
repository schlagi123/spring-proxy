package de.baswil.spring.proxy;

import de.baswil.spring.proxy.configuration.Configurations;
import de.baswil.spring.proxy.configuration.ConfigurationsReader;
import de.baswil.spring.proxy.httpproxy.HttpPropertyProxySettingsParser;
import de.baswil.spring.proxy.httpproxy.HttpUrlProxySettingsParser;
import de.baswil.spring.proxy.httpsproxy.HttpsPropertyProxySettingsParser;
import de.baswil.spring.proxy.httpsproxy.HttpsUrlProxySettingsParser;
import de.baswil.spring.proxy.noproxy.*;
import de.baswil.spring.proxy.proxy.ProxySettings;
import de.baswil.spring.proxy.proxy.ProxySettingsAnalyzer;
import de.baswil.spring.proxy.sys.SystemPropertyWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * A SpringBoot ApplicationListener to read the environment variables
 * <code>http_proxy</code>, <code>https_proxy</code>, <code>no_proxy</code>
 * and the system/app properties
 * <code>http.proxyHost</code>, <code>http.proxyPort</code>,
 * <code>http.proxyUser</code>, <code>http.proxyPassword</code>,
 * <code>https.proxyHost</code>, <code>https.proxyPort</code>,
 * <code>https.proxyUser</code>, <code>https.proxyPassword</code>,
 * <code>http.nonProxyHosts</code>
 * </p>
 * <p>
 * The system/app properties override the environment variables.
 * </p>
 * <p>
 * At the end the system properties
 * <code>http.proxyHost</code>, <code>http.proxyPort</code>,
 * <code>http.proxyUser</code>, <code>http.proxyPassword</code>,
 * <code>https.proxyHost</code>, <code>https.proxyPort</code>,
 * <code>https.proxyUser</code>, <code>https.proxyPassword</code>,
 * <code>http.nonProxyHosts</code>
 * are set, if values are available in the environment variables or system/app properties.
 * </p>
 *
 * @author Bastian Wilhelm
 */
public class ProxyApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        LOGGER.debug("PROXY LISTENER START");

        Configurations configurations = createConfigurations(event);
        SystemPropertyWriter systemPropertyWriter = createSystemPropertyWriter();

        checkAndSetHttpProxy(configurations, systemPropertyWriter);
        checkAndSetHttpsProxy(configurations, systemPropertyWriter);
        checkAndSetNoProxy(configurations, systemPropertyWriter);

        LOGGER.debug("PROXY LISTENER END");
    }

    private void checkAndSetHttpProxy(Configurations configurations, SystemPropertyWriter systemPropertyWriter) {
        ProxySettingsAnalyzer analyzer = createHttpProxyAnalyzer(configurations);
        ProxySettings proxySettings = analyzer.analyze();
        systemPropertyWriter.writeHttpProxySettings(proxySettings);
    }

    private void checkAndSetHttpsProxy(Configurations configurations, SystemPropertyWriter systemPropertyWriter) {
        ProxySettingsAnalyzer analyzer = createHttpsProxyAnalyzer(configurations);
        ProxySettings proxySettings = analyzer.analyze();
        systemPropertyWriter.writeHttpsProxySettings(proxySettings);
    }

    private void checkAndSetNoProxy(Configurations configurations, SystemPropertyWriter systemPropertyWriter) {
        NoProxyAnalyzer analyzer = createNoProxyAnalyzer(configurations);
        String noProxySettings = analyzer.analyze();
        systemPropertyWriter.writeNoProxySettings(noProxySettings);
    }

    protected ProxySettingsAnalyzer createHttpProxyAnalyzer(Configurations configurations) {
        HttpPropertyProxySettingsParser propertyParser = new HttpPropertyProxySettingsParser(configurations);
        HttpUrlProxySettingsParser urlParser = new HttpUrlProxySettingsParser(configurations);
        return new ProxySettingsAnalyzer(urlParser, propertyParser);
    }

    protected ProxySettingsAnalyzer createHttpsProxyAnalyzer(Configurations configurations) {
        HttpsPropertyProxySettingsParser propertyParser = new HttpsPropertyProxySettingsParser(configurations);
        HttpsUrlProxySettingsParser urlParser = new HttpsUrlProxySettingsParser(configurations);
        return new ProxySettingsAnalyzer(urlParser, propertyParser);
    }

    protected NoProxyAnalyzer createNoProxyAnalyzer(Configurations configurations) {
        NoProxyFormatterFactory formatterFactory = new NoProxyFormatterFactory(configurations);
        return new NoProxyAnalyzer(configurations, formatterFactory);
    }

    protected Configurations createConfigurations(ApplicationEnvironmentPreparedEvent event) {
        ConfigurationsReader configurationsReader = new ConfigurationsReader(event.getEnvironment());
        return configurationsReader.readConfigurations();
    }

    protected SystemPropertyWriter createSystemPropertyWriter() {
        return new SystemPropertyWriter();
    }
}
