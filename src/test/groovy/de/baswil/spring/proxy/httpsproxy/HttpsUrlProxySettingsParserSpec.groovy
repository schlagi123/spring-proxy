package de.baswil.spring.proxy.httpsproxy

import de.baswil.spring.proxy.configuration.Configurations
import de.baswil.spring.proxy.httpproxy.HttpUrlProxySettingsParser
import spock.lang.Specification


class HttpsUrlProxySettingsParserSpec extends Specification {
    Configurations configurations = Mock Configurations;
    HttpsUrlProxySettingsParser parser = new HttpsUrlProxySettingsParser(configurations)

    def "test null values"() {
        setup:
        1 * configurations.getOsHttpsProxy() >> null
        0 * configurations./get.*/()

        when:
        def url = parser.getUrl()

        then:
        url == null
    }

    def "test not null value"() {
        setup:
        1 * configurations.getOsHttpsProxy() >> "http://user:password@localhost:8080"
        0 * configurations./get.*/()

        when:
        def url = parser.getUrl()

        then:
        url == "http://user:password@localhost:8080"
    }
}