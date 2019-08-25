package de.baswil.spring.proxy

import spock.lang.Specification
import spock.lang.Unroll

class ProxySettingsFactorySpec extends Specification {
    private ProxySettingsFactory proxySettingsFactory

    void setup() {
        proxySettingsFactory = new ProxySettingsFactory()
    }

    @Unroll
    def "create proxy with parameters: #osProperty, #javaHost, #javaPort, #javaUser, #javaPassword"() {
        when:
        def currentResult = proxySettingsFactory.createProxySettings(osProperty, javaHost, javaPort, javaUser, javaPassword)

        then:
        currentResult == expectedResult

        where:
        osProperty         || javaHost || javaPort || javaUser || javaPassword || expectedResult
        null               || null     || null     || null     || null         || null
        // Only Environment Variable
        "http://localhost" || null     || null     || null     || null         || new ProxySettings(host: "localhost", port: null, user: null, password: null)
        "http://localhost:8080" || null || null || null || null || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        "http://user@localhost:8080" || null || null || null || null || new ProxySettings(host: "localhost", port: 8080, user: "user", password: null)
        "http://user:password@localhost:8080" || null || null || null || null || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password")
        "http://user:password:colon@localhost:8080" || null || null || null || null || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password:colon")
        // Only Environment Variable, but with wrong url format
        "localhost" || null     || null     || null     || null         || null
        "http:/localhost" || null     || null     || null     || null         || null
        // Only Java Properties
        null || "localhost" || null || null || null || new ProxySettings(host: "localhost", port: null, user: null, password: null)
        null || "localhost" || "8080" || null || null || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        null || "localhost" || "8080" || "user" || "password" || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password")
        // Only Java Properties, with password but without user
        null || "localhost" || "8080" || null || "password" || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        // Only Java Properties, but without host:
        null || null || "8080" || null || null || null
        null || null || null || "user" || null || null
        null || null || null || null || "password" || null
        null || null || "8080" || "user" || null || null
        null || null || "8080" || null || "password" || null
        null || null || null || "user" || "password" || null
        null || null || "8080" || "user" || "password" || null
        // Only Java Properties, with wrong port number:
        null || "localhost" || "-1" || "user" || "password" || new ProxySettings(host: "localhost", port: null, user: "user", password: "password")
        null || "localhost" || "bla" || "user" || "password" || new ProxySettings(host: "localhost", port: null, user: "user", password: "password")
        // Override Environment Properties
        "http://user:password@localhost:8080" || "tsohlacol" || null || null || null || new ProxySettings(host: "tsohlacol", port: 8080, user: "user", password: "password")
        "http://user:password@localhost:8080" || null || "9090" || null || null || new ProxySettings(host: "localhost", port: 9090, user: "user", password: "password")
        "http://user:password@localhost:8080" || null || null || "resu" || null || new ProxySettings(host: "localhost", port: 8080, user: "resu", password: "password")
        "http://user:password@localhost:8080" || null || null || null || "drowssap" || new ProxySettings(host: "localhost", port: 8080, user: "user", password: "drowssap")
        // Reset Environment Properties
        "http://user:password@localhost:8080" || null || null || null || "" || new ProxySettings(host: "localhost", port: 8080, user: "user", password: null)
        "http://user:password@localhost:8080" || null || null || "" || null || new ProxySettings(host: "localhost", port: 8080, user: null, password: null)
        "http://user:password@localhost:8080" || null || "" || null || null || new ProxySettings(host: "localhost", port: null, user: "user", password: "password")
        "http://user:password@localhost:8080" || "" || null || null || null || null
    }
}
