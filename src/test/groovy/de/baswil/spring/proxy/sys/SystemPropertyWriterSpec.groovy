package de.baswil.spring.proxy.sys

import de.baswil.spring.proxy.configuration.Constants
import de.baswil.spring.proxy.proxy.ProxySettings
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

@RestoreSystemProperties
class SystemPropertyWriterSpec extends Specification {
    SystemPropertyWriter writer = new SystemPropertyWriter()

    def "test write http proxy"() {
        setup:
        def settings = new ProxySettings(
                host: "localhost",
                port: 8080,
                user: "user",
                password: "password"
        )

        when:
        writer.writeHttpProxySettings(settings)

        then:
        System.getProperty(Constants.JAVA_HTTP_PROXY_HOST_PROP) == "localhost"
        System.getProperty(Constants.JAVA_HTTP_PROXY_PORT_PROP) == "8080"
        System.getProperty(Constants.JAVA_HTTP_PROXY_USER_PROP) == "user"
        System.getProperty(Constants.JAVA_HTTP_PROXY_PASSWORD_PROP) == "password"
    }

    def "test write http proxy null"() {
        when:
        writer.writeHttpProxySettings(null)

        then:
        System.getProperty(Constants.JAVA_HTTP_PROXY_HOST_PROP) == null
        System.getProperty(Constants.JAVA_HTTP_PROXY_PORT_PROP) == null
        System.getProperty(Constants.JAVA_HTTP_PROXY_USER_PROP) == null
        System.getProperty(Constants.JAVA_HTTP_PROXY_PASSWORD_PROP) == null
    }

    def "test write https proxy"() {
        setup:
        def settings = new ProxySettings(
                host: "localhost",
                port: 8080,
                user: "user",
                password: "password"
        )

        when:
        writer.writeHttpsProxySettings(settings)

        then:
        System.getProperty(Constants.JAVA_HTTPS_PROXY_HOST_PROP) == "localhost"
        System.getProperty(Constants.JAVA_HTTPS_PROXY_PORT_PROP) == "8080"
        System.getProperty(Constants.JAVA_HTTPS_PROXY_USER_PROP) == "user"
        System.getProperty(Constants.JAVA_HTTPS_PROXY_PASSWORD_PROP) == "password"
    }

    def "test write https proxy null"() {
        when:
        writer.writeHttpsProxySettings(null)

        then:
        System.getProperty(Constants.JAVA_HTTPS_PROXY_HOST_PROP) == null
        System.getProperty(Constants.JAVA_HTTPS_PROXY_PORT_PROP) == null
        System.getProperty(Constants.JAVA_HTTPS_PROXY_USER_PROP) == null
        System.getProperty(Constants.JAVA_HTTPS_PROXY_PASSWORD_PROP) == null
    }

    def "test write no proxy"() {
        when:
        writer.writeNoProxySettings("localhost")

        then:
        System.getProperty(Constants.JAVA_NON_PROXY_HOSTS_PROP) == "localhost"
    }

    def "test write no proxy null"() {
        when:
        writer.writeNoProxySettings(null)

        then:
        System.getProperty(Constants.JAVA_NON_PROXY_HOSTS_PROP) == null
    }
}