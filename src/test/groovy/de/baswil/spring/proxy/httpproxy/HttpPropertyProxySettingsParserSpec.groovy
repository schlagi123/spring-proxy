package de.baswil.spring.proxy.httpproxy

import de.baswil.spring.proxy.configuration.Configurations
import spock.lang.Specification


class HttpPropertyProxySettingsParserSpec extends Specification {
    Configurations configurations = Mock Configurations;
    HttpPropertyProxySettingsParser parser = new HttpPropertyProxySettingsParser(configurations)

    def "test null values"() {
        setup:
        1 * configurations.getAppHttpProxyHost() >> null
        1 * configurations.getAppHttpProxyPort() >> null
        1 * configurations.getAppHttpProxyUser() >> null
        1 * configurations.getAppHttpProxyPassword() >> null
        0 * configurations./get.*/()

        when:
        def host = parser.getHost()
        def port = parser.getPort()
        def user = parser.getUser()
        def password = parser.getPassword()

        then:
        host == null
        port == null
        user == null
        password == null
    }

    def "test not null value"() {
        setup:
        1 * configurations.getAppHttpProxyHost() >> "localhost"
        1 * configurations.getAppHttpProxyPort() >> "8080"
        1 * configurations.getAppHttpProxyUser() >> "user"
        1 * configurations.getAppHttpProxyPassword() >> "password"
        0 * configurations./get.*/()

        when:
        def host = parser.getHost()
        def port = parser.getPort()
        def user = parser.getUser()
        def password = parser.getPassword()

        then:
        host == "localhost"
        port == "8080"
        user == "user"
        password == "password"
    }
}