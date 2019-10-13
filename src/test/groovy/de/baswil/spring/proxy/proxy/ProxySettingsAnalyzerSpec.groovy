package de.baswil.spring.proxy.proxy

import spock.lang.Specification

class ProxySettingsAnalyzerSpec extends Specification {
    AbstractUrlProxySettingsParser urlParser = Mock AbstractUrlProxySettingsParser
    AbstractPropertyProxySettingsParser propertyParser = Mock AbstractPropertyProxySettingsParser
    ProxySettingsAnalyzer analyzer = new ProxySettingsAnalyzer(urlParser, propertyParser)

    def "test without settings"() {
        setup:
        1 * urlParser.readProxySettingsFromUrl(_ as ProxySettings)
        1 * propertyParser.readProxySettingsFromProperties(_ as ProxySettings)

        when:
        ProxySettings settings = analyzer.analyze();

        then:
        settings == null
    }

    def "test with normal settings"() {
        setup:
        final hostValue = "host"
        final portValue = 80
        final userValue = "user"
        final passwordValue = "password"
        1 * urlParser.readProxySettingsFromUrl(_ as ProxySettings)
        1 * propertyParser.readProxySettingsFromProperties(_ as ProxySettings) >> { it ->
            it[0].host = hostValue
            it[0].port = portValue
            it[0].user = userValue
            it[0].password = passwordValue
        }
        when:
        ProxySettings settings = analyzer.analyze()

        then:
        settings != null
        settings.host == hostValue
        settings.port == portValue
        settings.user == userValue
        settings.password == passwordValue
    }

    def "test with normal settings but without password"() {
        setup:
        final hostValue = "host"
        final portValue = 80
        final passwordValue = "password"
        1 * urlParser.readProxySettingsFromUrl(_ as ProxySettings)
        1 * propertyParser.readProxySettingsFromProperties(_ as ProxySettings) >> { it ->
            it[0].host = hostValue
            it[0].port = portValue
            it[0].password = passwordValue
        }
        when:
        ProxySettings settings = analyzer.analyze()

        then:
        settings != null
        settings.host == hostValue
        settings.port == portValue
        settings.user == null
        settings.password == null
    }
}
