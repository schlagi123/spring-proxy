package de.baswil.spring.proxy.noproxy

import de.baswil.spring.proxy.configuration.Configurations
import spock.lang.Specification

class NoProxyFormatterFactorySpec extends Specification {
    Configurations configurations = Mock Configurations
    NoProxyFormatterFactory factory = new NoProxyFormatterFactory(configurations)

    def "default without setting format and formatter"() {
        when:
        def formatter = factory.createNoProxyFormatterFromProperties()

        then:
        formatter.class == NoChangeProxyFormatter.class
    }

    def "with JAVA format"() {
        setup:
        configurations.getAppNonProxyHostsFormat() >> NoProxyFormat.JAVA

        when:
        def formatter = factory.createNoProxyFormatterFromProperties()

        then:
        formatter.class == NoChangeProxyFormatter.class
    }

    def "with OS format"() {
        setup:
        configurations.getAppNonProxyHostsFormat() >> NoProxyFormat.OS

        when:
        def formatter = factory.createNoProxyFormatterFromProperties()

        then:
        formatter.class == OSNoProxyFormatter.class
    }

    def "for OS Property"() {
        when:
        def formatter = factory.createNoProxyFormatterForOSProperty()

        then:
        formatter.class == OSNoProxyFormatter.class
    }

    def "with OTHER format and without formatter"() {
        setup:
        configurations.getAppNonProxyHostsFormat() >> NoProxyFormat.OTHER
        configurations.getAppNonProxyHostsFormatter() >> null

        when:
        factory.createNoProxyFormatterFromProperties()

        then:
        thrown NoProxyFormatterInitializationException
    }

    def "with OTHER format and with formatter without default constructor"() {
        setup:
        configurations.getAppNonProxyHostsFormat() >> NoProxyFormat.OTHER
        configurations.getAppNonProxyHostsFormatter() >> WithoutDefaultConstructor.class.getName()

        when:
        factory.createNoProxyFormatterFromProperties()

        then:
        thrown NoProxyFormatterInitializationException
    }

    static class WithoutDefaultConstructor implements NoProxyFormatter {
        WithoutDefaultConstructor(String param) {
        }

        @Override
        List<String> formatHostName(String hostname) {
            return null
        }

        @Override
        String hostDelimiter() {
            return null
        }
    }

    def "with OTHER format and with formatter with exception in default constructor"() {
        setup:
        configurations.getAppNonProxyHostsFormat() >> NoProxyFormat.OTHER
        configurations.getAppNonProxyHostsFormatter() >> WithExceptionDefaultConstructor.class.getName()

        when:
        factory.createNoProxyFormatterFromProperties()

        then:
        thrown NoProxyFormatterInitializationException
    }

    static class WithExceptionDefaultConstructor implements NoProxyFormatter {
        WithExceptionDefaultConstructor() {
            throw new RuntimeException()
        }

        @Override
        List<String> formatHostName(String hostname) {
            return null
        }

        @Override
        String hostDelimiter() {
            return null
        }
    }

    def "with OTHER format with formatter"() {
        setup:
        configurations.getAppNonProxyHostsFormat() >> NoProxyFormat.OTHER
        configurations.getAppNonProxyHostsFormatter() >> RightFormatter.class.getName()

        when:
        def formatter = factory.createNoProxyFormatterFromProperties()

        then:
        formatter.class == RightFormatter.class
    }

    static class RightFormatter implements NoProxyFormatter {
        @Override
        List<String> formatHostName(String hostname) {
            return null
        }

        @Override
        String hostDelimiter() {
            return null
        }
    }
}