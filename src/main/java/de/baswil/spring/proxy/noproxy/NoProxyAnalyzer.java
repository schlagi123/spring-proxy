package de.baswil.spring.proxy.noproxy;

import de.baswil.spring.proxy.configuration.Configurations;

import java.util.List;
import java.util.StringJoiner;

/**
 * Analyze environment variable and application property non proxy hosts.
 *
 * @author Bastian Wilhelm
 */
public class NoProxyAnalyzer {
    private final Configurations configurations;
    private final NoProxyFormatterFactory noProxyFormatterFactory;

    public NoProxyAnalyzer(Configurations configurations,
                           NoProxyFormatterFactory noProxyFormatterFactory) {
        this.configurations = configurations;
        this.noProxyFormatterFactory = noProxyFormatterFactory;
    }

    /**
     * Analyze and format non proxy hosts.
     *
     * @return result of analyze.
     */
    public String analyze() {
        String osPropertyValue = configurations.getOsNoProxy();
        String javaPropertyValue = configurations.getAppNonProxyHosts();

        if (javaPropertyValue != null) {
            if (javaPropertyValue.trim().isEmpty()) {
                return null;
            } else {
                NoProxyFormatter formatter = noProxyFormatterFactory.createNoProxyFormatterFromProperties();
                return convertProperty(javaPropertyValue, formatter);
            }
        } else if (osPropertyValue != null) {
            NoProxyFormatter formatter = noProxyFormatterFactory.createNoProxyFormatterForOSProperty();
            return convertProperty(osPropertyValue, formatter);
        } else {
            return null;
        }
    }

    private String convertProperty(String value, NoProxyFormatter formatter) {
        StringJoiner stringJoiner = new StringJoiner("|");
        String delimiter = formatter.hostDelimiter();
        if (delimiter == null) {
            List<String> convertedHosts = formatter.formatHostName(value);
            convertedHosts.forEach(stringJoiner::add);
            return stringJoiner.toString();
        } else {
            String[] hosts = value.split(delimiter);
            for (String host : hosts) {
                List<String> convertedHosts = formatter.formatHostName(host);
                convertedHosts.forEach(stringJoiner::add);
            }
            return stringJoiner.toString();
        }
    }
}
