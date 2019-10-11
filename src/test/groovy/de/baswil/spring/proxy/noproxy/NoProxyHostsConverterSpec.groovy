package de.baswil.spring.proxy.noproxy

import spock.lang.Specification

class NoProxyHostsConverterSpec extends Specification {
    NoProxyFormatterFactory factory = Mock NoProxyFormatterFactory
    NoProxyHostsConverter converter = new NoProxyHostsConverter(factory)

    def "without properties"() {
        when:
        def result = converter.convert(null, null)

        then:
        result == null
    }

    def "with default osProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> ","
        formatter.formatHostName("host1") >> ["host1"]
        formatter.formatHostName(".host2") >> ["*.host2"]
        formatter.formatHostName("*.host3") >> ["*.host3"]
        factory.createNoProxyFormatterForOSProperty() >> formatter

        when:
        def result = converter.convert("host1,.host2,*.host3", null)

        then:
        result == "host1|*.host2|*.host3"
    }

    def "with default javaProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> null
        formatter.formatHostName(_ as String) >> {[it[0]]}
        factory.createNoProxyFormatterFromProperties() >> formatter

        when:
        def result = converter.convert(null, "host1|.host2|*.host3")

        then:
        result == "host1|.host2|*.host3"
    }

    def "with javaProperty and osProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> null
        formatter.formatHostName(_ as String) >> {[it[0]]}
        factory.createNoProxyFormatterFromProperties() >> formatter

        when:
        def result = converter.convert("host1", "host2")

        then:
        result == "host2"
    }

    def "with reset osProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> null
        formatter.formatHostName(_ as String) >> {[it[0]]}
        factory.createNoProxyFormatterFromProperties() >> formatter

        when:
        def result = converter.convert("host1", "")

        then:
        result == null
    }
}
