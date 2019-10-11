package de.baswil.spring.proxy.noproxy

import org.springframework.core.env.Environment
import spock.lang.Specification

class NoProxyFormatterFactorySpec extends Specification {
    Environment environment = Mock Environment
    NoProxyFormatterFactory factory = new NoProxyFormatterFactory(environment)

    def "default without setting format and formatter"() {
        setup:
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> {
            it[2]
        }

        when:
        def formatter = factory.createNoProxyFormatterFromProperties()

        then:
        formatter.class == NoChangeProxyFormatter.class
    }

    def "with JAVA format"() {
        setup:
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.JAVA

        when:
        def formatter = factory.createNoProxyFormatterFromProperties()

        then:
        formatter.class == NoChangeProxyFormatter.class
    }

    def "with OS format"() {
        setup:
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OS

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
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OTHER
        environment.getProperty(NoProxyFormatterFactory.FORMATTER_PROPERTY_NAME, String.class) >> null

        when:
        factory.createNoProxyFormatterFromProperties()

        then:
        thrown NoProxyFormatterInitializationException
    }

    def "with OTHER format and with not existing formatter"() {
        setup:
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OTHER
        environment.getProperty(NoProxyFormatterFactory.FORMATTER_PROPERTY_NAME, String.class) >> "helloworld"

        when:
        factory.createNoProxyFormatterFromProperties()

        then:
        thrown NoProxyFormatterInitializationException
    }

    def "with OTHER format and with formatter without implementing interface"() {
        setup:
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OTHER
        environment.getProperty(NoProxyFormatterFactory.FORMATTER_PROPERTY_NAME, String.class) >> "java.lang.String"

        when:
        factory.createNoProxyFormatterFromProperties()

        then:
        thrown NoProxyFormatterInitializationException
    }

    def "with OTHER format and with formatter without default constructor"() {
        setup:
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OTHER
        environment.getProperty(NoProxyFormatterFactory.FORMATTER_PROPERTY_NAME, String.class) >> WithoutDefaultConstructor.class.getName()

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
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OTHER
        environment.getProperty(NoProxyFormatterFactory.FORMATTER_PROPERTY_NAME, String.class) >> WithExceptionDefaultConstructor.class.getName()

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
        environment.getProperty(NoProxyFormatterFactory.FORMAT_PROPERTY_NAME, NoProxyFormat.class, _ as NoProxyFormat) >> NoProxyFormat.OTHER
        environment.getProperty(NoProxyFormatterFactory.FORMATTER_PROPERTY_NAME, String.class) >> RightFormatter.class.getName()

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