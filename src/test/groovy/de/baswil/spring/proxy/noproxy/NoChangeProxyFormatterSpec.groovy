package de.baswil.spring.proxy.noproxy

import spock.lang.Specification


class NoChangeProxyFormatterSpec extends Specification {
    NoChangeProxyFormatter formatter = new NoChangeProxyFormatter()

    def "format without changes" () {
        setup:
        def noProxy = "localhost|*.google.de"

        when:
        def formattedNoProxy = formatter.formatHostName(noProxy)

        then:
        formatter.hostDelimiter() == null
        formattedNoProxy == noProxy
    }
}