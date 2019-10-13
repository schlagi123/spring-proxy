package de.baswil.spring.proxy.httpproxy

import de.baswil.spring.proxy.configuration.Configurations
import spock.lang.Specification


class HttpUrlProxySettingsParserSpec extends Specification {
    Configurations configurations = Mock Configurations;
    HttpUrlProxySettingsParser parser = new HttpUrlProxySettingsParser(configurations)

    def "test null values"() {
        setup:
        1 * configurations.getOsHttpProxy() >> null
        0 * configurations./get.*/()

        when:
        def url = parser.getUrl()

        then:
        url == null
    }

    def "test not null value"() {
        setup:
        1 * configurations.getOsHttpProxy() >> "http://user:password@localhost:8080"
        0 * configurations./get.*/()

        when:
        def url = parser.getUrl()

        then:
        url == "http://user:password@localhost:8080"
    }
}