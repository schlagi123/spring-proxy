package de.baswil.spring.proxy

import spock.lang.Specification
import spock.lang.Unroll

class ProxySettingsFactoryCreateProxySpec extends Specification {
    private ProxySettingsFactory proxySettingsFactory

    void setup() {
        proxySettingsFactory = new ProxySettingsFactory()
    }

    def "create proxy without variables"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(
                null,
                null,
                null,
                null,
                null)

        then:
        currentResult == null
    }

    @Unroll
    def "create proxy only from OS environment variables (env. variable: #osProperty)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(
                osProperty,
                null,
                null,
                null,
                null
        )

        then:
        currentResult == expectedResult

        where:
        osProperty                                  || expectedResult
        "http://localhost"                          || new ProxySettings(host: "localhost", port: null, user: null, password: null)
        "http://localhost:8080"                     || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        "http://user@localhost:8080"                || new ProxySettings(host: "localhost", port: 8080, user: "user", password: null)
        "http://user:password@localhost:8080"       || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password")
        "http://user:password:colon@localhost:8080" || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password:colon")
    }

    @Unroll
    def "create proxy only from OS environment variables with wrong urls (env. variable: #osProperty)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(
                osProperty,
                null,
                null,
                null,
                null
        )

        then:
        currentResult == null

        where:
        osProperty << ["localhost", "http:/localhost"]
    }

    @Unroll
    def "create proxy only from java Properties (host: #javaHost, port: #javaPort, user: #javaUser, password: #javaPassword)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(null, javaHost, javaPort, javaUser, javaPassword)

        then:
        currentResult == expectedResult

        where:
        javaHost    || javaPort || javaUser || javaPassword || expectedResult
        "localhost" || null     || null     || null         || new ProxySettings(host: "localhost", port: null, user: null, password: null)
        "localhost" || "8080"   || null     || null         || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        "localhost" || "8080"   || "user"   || "password"   || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password")
    }

    @Unroll
    def "create proxy only from java Properties without hostname (prot: #javaPort, user: #javaUser, password: #javaPassword)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(
                null,
                null,
                javaPort,
                javaUser,
                javaPassword
        )

        then:
        currentResult == null

        where:
        javaPort || javaUser || javaPassword
        "8080"   || null     || null
        null     || "user"   || null
        null     || null     || "password"
        "8080"   || "user"   || null
        "8080"   || null     || "password"
        null     || "user"   || "password"
        "8080"   || "user"   || "password"
    }

    def "create proxy only from java Properties with password and without username"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings("http://test:test@localhost:8080", "localhost", "8080", "", "password")

        then:
        with(currentResult, {
            host == "localhost"
            port == 8080
            user == null
            password == null
        })
    }

    @Unroll
    def "create proxy only from java Properties with wrong port number (port: #javaPort)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(null, "localhost", javaPort, "user", "password")

        then:
        with(currentResult, {
            host == "localhost"
            port == null
            user == "user"
            password == "password"
        })

        where:
        javaPort << ["-1", "0", "bla"]
    }

    @Unroll
    def "override proxy properties from OS (env. variable: #osProperty, host: #javaHost, port: #javaPort, user: #javaUser, password: #javaPassword)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(osProperty, javaHost, javaPort, javaUser, javaPassword)

        then:
        currentResult == expectedResult

        where:
        osProperty                            || javaHost    || javaPort || javaUser || javaPassword || expectedResult
        "http://user:password@localhost:8080" || "tsohlacol" || null     || null     || null         || new ProxySettings(host: "tsohlacol", port: 8080, user: "user", password: "password")
        "http://user:password@localhost:8080" || null        || "9090"   || null     || null         || new ProxySettings(host: "localhost", port: 9090, user: "user", password: "password")
        "http://user:password@localhost:8080" || null        || null     || "resu"   || null         || new ProxySettings(host: "localhost", port: 8080, user: "resu", password: "password")
        "http://user:password@localhost:8080" || null        || null     || null     || "drowssap"   || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "drowssap")
    }

    @Unroll
    def "unset proxy properties from OS with empty string (env. variable: #osProperty, host: #javaHost, port: #javaPort, user: #javaUser, password: #javaPassword)"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(osProperty, javaHost, javaPort, javaUser, javaPassword)

        then:
        currentResult == expectedResult

        where:
        osProperty                            || javaHost || javaPort || javaUser || javaPassword || expectedResult
        "http://user:password@localhost:8080" || null     || null     || null     || ""           || new ProxySettings(host: "localhost", port: 8080, user: "user", password: null)
        "http://user:password@localhost:8080" || null     || null     || ""       || null         || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        "http://user:password@localhost:8080" || null     || ""       || null     || null         || new ProxySettings(host: "localhost", port: null, user: "user", password: "password")
        "http://user:password@localhost:8080" || ""       || null     || null     || null         || null
    }
}
