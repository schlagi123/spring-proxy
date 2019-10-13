package de.baswil.spring.proxy.noproxy

import de.baswil.spring.proxy.configuration.Configurations
import spock.lang.Specification

class NoProxyAnalyzerSpec extends Specification {
    NoProxyFormatterFactory factory = Mock NoProxyFormatterFactory
    Configurations configurations = Mock Configurations
    NoProxyAnalyzer analyzer = new NoProxyAnalyzer(configurations, factory)

    def "without properties"() {
        setup:
        configurations.getOsNoProxy() >> null
        configurations.getAppNonProxyHosts() >> null

        when:
        def result = analyzer.analyze()

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

        and:
        configurations.getOsNoProxy() >> "host1,.host2,*.host3"
        configurations.getAppNonProxyHosts() >> null

        when:
        def result = analyzer.analyze()

        then:
        result == "host1|*.host2|*.host3"
    }

    def "with default javaProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> null
        formatter.formatHostName(_ as String) >> {[it[0]]}
        factory.createNoProxyFormatterFromProperties() >> formatter

        and:
        configurations.getOsNoProxy() >> null
        configurations.getAppNonProxyHosts() >> "host1|.host2|*.host3"

        when:
        def result = analyzer.analyze()

        then:
        result == "host1|.host2|*.host3"
    }

    def "with javaProperty and osProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> null
        formatter.formatHostName(_ as String) >> {[it[0]]}
        factory.createNoProxyFormatterFromProperties() >> formatter

        and:
        configurations.getOsNoProxy() >> "host1"
        configurations.getAppNonProxyHosts() >> "host2"

        when:
        def result = analyzer.analyze()

        then:
        result == "host2"
    }

    def "with reset osProperty"() {
        setup:
        NoProxyFormatter formatter = Mock NoProxyFormatter
        formatter.hostDelimiter() >> null
        formatter.formatHostName(_ as String) >> {[it[0]]}
        factory.createNoProxyFormatterFromProperties() >> formatter

        and:
        configurations.getOsNoProxy() >> "host1"
        configurations.getAppNonProxyHosts() >> ""

        when:
        def result = analyzer.analyze()

        then:
        result == null
    }
}
