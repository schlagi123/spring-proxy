package de.baswil.spring.proxy

import de.baswil.spring.proxy.noproxy.NoChangeProxyFormatter
import spock.lang.Specification
import spock.lang.Unroll

class ProxySettingsFactoryCreateNoProxySpec extends Specification {
    private ProxySettingsFactory proxySettingsFactory

    void setup() {
        proxySettingsFactory = new ProxySettingsFactory()
    }

    def "create no proxy only without variables"() {
        when:
        def currentResult = proxySettingsFactory.createNonProxyHosts(null, null, new NoChangeProxyFormatter())

        then:
        currentResult == null
    }

    @Unroll
    def "create no proxy only from OS environment variables (env. variable: #osProperty)"() {
        when:
        def currentResult = proxySettingsFactory.createNonProxyHosts(osProperty, null, new NoChangeProxyFormatter())

        then:
        currentResult == expectedResult

        where:
        osProperty                || expectedResult
        "google.de"               || "google.de"
        "google.de,google.com"    || "google.de|google.com"
        "*.google.de"             || "*.google.de"
        ".google.de"              || "*.google.de"
        "*.google.de,.google.com" || "*.google.de|*.google.com"
    }

    @Unroll
    def "create no proxy only from Java properties (nonProxyHosts: #javaProperty)"() {
        when:
        def currentResult = proxySettingsFactory.createNonProxyHosts(null, javaProperty, new NoChangeProxyFormatter())

        then:
        currentResult == expectedResult

        where:
        javaProperty           || expectedResult
        "google.de"            || "google.de"
        "google.de|google.com" || "google.de|google.com"
        "*.google.de"          || "*.google.de"
        ".google.de"           || ".google.de"
    }

    @Unroll
    def "overwrite no proxy OS environment variables (env. variable: #osProperty, nonProxyHosts: #javaProperty)"() {
        when:
        def currentResult = proxySettingsFactory.createNonProxyHosts(osProperty, javaProperty, new NoChangeProxyFormatter())

        then:
        currentResult == expectedResult

        where:
        osProperty             || javaProperty           || expectedResult
        "testAAA"              || "testBBB"              || "testBBB"
        "google.de,google.com" || "google.de,google.com" || "google.de,google.com"
    }

    def "unset no proxy only OS environment variables with empty string"() {
        when:
        def currentResult = proxySettingsFactory.createNonProxyHosts("prop", "", new NoChangeProxyFormatter())

        then:
        currentResult == null
    }
}
