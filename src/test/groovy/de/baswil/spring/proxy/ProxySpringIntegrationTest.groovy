package de.baswil.spring.proxy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties


@SpringBootApplication
@EnableWebMvc
class ProxySpringIntegrationTest extends Specification {

    @RestoreSystemProperties
    def "run spring with proxy Settings"() {
        setup:
        def application = new SpringApplication(ProxySpringIntegrationTest.class)
        application.addListeners(new ProxyApplicationListener())
        def context = application.run(
                "--server.port=0",
                "--http.proxyHost=localhosthttp",
                "--http.proxyPort=80",
                "--http.proxyUser=user",
                "--http.proxyPassword=password",
                "--https.proxyHost=localhosthttps",
                "--https.proxyPort=443",
                "--https.proxyUser=users",
                "--https.proxyPassword=passwords",
                "--http.nonProxyHosts=google.de"
        )

        when:
        def proxyInformation = context.getBean(ProxyInformation.class)

        then:
        proxyInformation.isHttpProxySet()
        proxyInformation.getHttpHost() == "localhosthttp"
        proxyInformation.getHttpPort() == "80"
        proxyInformation.getHttpUser() == "user"
        proxyInformation.getHttpPassword() == "password"
        proxyInformation.isHttpsProxySet()
        proxyInformation.getHttpsHost() == "localhosthttps"
        proxyInformation.getHttpsPort() == "443"
        proxyInformation.getHttpsUser() == "users"
        proxyInformation.getHttpsPassword() == "passwords"
        proxyInformation.isNonProxyHostsSet()
        proxyInformation.getNonProxyHosts() == "google.de"

        cleanup:
        context.close()
    }

    @RestoreSystemProperties
    def "run spring without proxy Settings"() {
        setup:
        def application = new SpringApplication(ProxySpringIntegrationTest.class)
        application.addListeners(new ProxyApplicationListener())
        def context = application.run("--server.port=0")

        when:
        def proxyInformation = context.getBean(ProxyInformation.class)

        then:
        !proxyInformation.isHttpProxySet()
        proxyInformation.getHttpHost() == null
        proxyInformation.getHttpPort() == null
        proxyInformation.getHttpUser() == null
        proxyInformation.getHttpPassword() == null

        !proxyInformation.isHttpsProxySet()
        proxyInformation.getHttpsHost() == null
        proxyInformation.getHttpsPort() == null
        proxyInformation.getHttpsUser() == null
        proxyInformation.getHttpsPassword() == null

        !proxyInformation.isNonProxyHostsSet()
        proxyInformation.getNonProxyHosts() == null

        cleanup:
        context.close()
    }
}