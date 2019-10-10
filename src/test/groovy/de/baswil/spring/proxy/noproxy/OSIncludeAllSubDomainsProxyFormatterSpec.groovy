package de.baswil.spring.proxy.noproxy

import spock.lang.Specification
import spock.lang.Unroll

class OSIncludeAllSubDomainsProxyFormatterSpec extends Specification {
    OSIncludingAllSubDomainsNoProxyFormatter formatter = new OSIncludingAllSubDomainsNoProxyFormatter()

    @Unroll
    def "format {host}" () {
        when:
        def formattedNoProxy = formatter.formatHostName(host)

        then:
        formattedNoProxy == expectedHost

        where:
        host | expectedHost
        "localhost" | "*.localhost"
        ".localhost" | "*.localhost"
        "*.localhost" | "*.localhost"
    }

    def "delimiter" () {
        expect:
        formatter.hostDelimiter() == ","
    }
}