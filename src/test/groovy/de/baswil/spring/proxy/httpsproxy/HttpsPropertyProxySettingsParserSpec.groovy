package de.baswil.spring.proxy.httpsproxy

import de.baswil.spring.proxy.configuration.Configurations
import de.baswil.spring.proxy.httpproxy.HttpPropertyProxySettingsParser
import spock.lang.Specification


class HttpsPropertyProxySettingsParserSpec extends Specification {
    Configurations configurations = Mock Configurations;
    HttpsPropertyProxySettingsParser parser = new HttpsPropertyProxySettingsParser(configurations)

    def "test null values"() {
        setup:
        1 * configurations.getAppHttpsProxyHost() >> null
        1 * configurations.getAppHttpsProxyPort() >> null
        1 * configurations.getAppHttpsProxyUser() >> null
        1 * configurations.getAppHttpsProxyPassword() >> null
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
        1 * configurations.getAppHttpsProxyHost() >> "localhost"
        1 * configurations.getAppHttpsProxyPort() >> "8080"
        1 * configurations.getAppHttpsProxyUser() >> "user"
        1 * configurations.getAppHttpsProxyPassword() >> "password"
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