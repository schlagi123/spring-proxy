package de.baswil.spring.proxy.noproxy;

import java.util.List;
import java.util.StringJoiner;

public class NoProxyHostsConverter {
    private final NoProxyFormatterFactory noProxyFormatterFactory;

    public NoProxyHostsConverter(NoProxyFormatterFactory noProxyFormatterFactory) {
        this.noProxyFormatterFactory = noProxyFormatterFactory;
    }

    public String convert(String osPropertyValue, String javaPropertyValue) {
        if(javaPropertyValue != null){
            if(javaPropertyValue.trim().isEmpty()){
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

    private String convertProperty(String value, NoProxyFormatter formatter){
        StringJoiner stringJoiner = new StringJoiner("|");
        String delimiter = formatter.hostDelimiter();
        if(delimiter == null){
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
