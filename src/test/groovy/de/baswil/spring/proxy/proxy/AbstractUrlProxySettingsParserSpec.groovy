package de.baswil.spring.proxy.proxy

import spock.lang.Specification
import spock.lang.Unroll


class AbstractUrlProxySettingsParserSpec extends Specification {
    AbstractUrlProxySettingsParser parser = Spy(AbstractUrlProxySettingsParser)

    @Unroll
    def "test parsing with url #url"() {
        setup:
        parser.getUrl() >> url
        def settings = new ProxySettings()

        when:
        parser.readProxySettingsFromUrl(settings)

        then:
        settings.host == host
        settings.port == port
        settings.user == user
        settings.password == password

        where:
        url                                   | host        | port | user   | password
        null                                  | null        | null | null   | null
        "http://user:password@localhost:8080" | "localhost" | 8080 | "user" | "password"
        "http://user@localhost:8080"          | "localhost" | 8080 | "user" | null
        "http://localhost:8080"               | "localhost" | 8080 | null   | null
        "http://localhost"                    | "localhost" | null | null   | null
        "http:/localhost"                     | null        | null | null   | null
        "localhost"                           | null        | null | null   | null
    }
}