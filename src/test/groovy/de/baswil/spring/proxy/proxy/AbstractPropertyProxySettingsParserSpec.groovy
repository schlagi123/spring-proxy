package de.baswil.spring.proxy.proxy

import spock.lang.Specification
import spock.lang.Unroll


class AbstractPropertyProxySettingsParserSpec extends Specification {
    AbstractPropertyProxySettingsParser parser = Spy(AbstractPropertyProxySettingsParser)

    @Unroll
    def "test parsing with #hostP, #portP, #userP, #passwordP"() {
        setup:
        parser.getHost() >> hostP
        parser.getPort() >> portP
        parser.getUser() >> userP
        parser.getPassword() >> passwordP
        def settings = new ProxySettings()

        when:
        parser.readProxySettingsFromProperties(settings)

        then:
        settings.host == host
        settings.port == port
        settings.user == user
        settings.password == password

        where:
        hostP       | portP  | userP  | passwordP  | host        | port | user   | password
        null        | null   | null   | null       | null        | null | null   | null

        "localhost" | null   | null   | null       | "localhost" | null | null   | null
        null        | "8080" | null   | null       | null        | 8080 | null   | null
        null        | null   | "user" | null       | null        | null | "user" | null
        null        | null   | null   | "password" | null        | null | null   | "password"

        "localhost" | "8080" | null   | null       | "localhost" | 8080 | null   | null
        "localhost" | null   | "user" | null       | "localhost" | null | "user" | null
        "localhost" | null   | null   | "password" | "localhost" | null | null   | "password"
        null        | "8080" | "user" | null       | null        | 8080 | "user" | null
        null        | "8080" | null   | "password" | null        | 8080 | null   | "password"
        null        | null   | "user" | "password" | null        | null | "user" | "password"

        "localhost" | "8080" | "user" | null       | "localhost" | 8080 | "user" | null
        "localhost" | "8080" | null   | "password" | "localhost" | 8080 | null   | "password"
        "localhost" | null   | "user" | "password" | "localhost" | null | "user" | "password"
        null        | "8080" | "user" | "password" | null        | 8080 | "user" | "password"

        "localhost" | "8080" | "user" | "password" | "localhost" | 8080 | "user" | "password"
    }

    @Unroll
    def "test parsing special port cases #portP"() {
        setup:
        parser.getHost() >> null
        parser.getPort() >> portP
        parser.getUser() >> null
        parser.getPassword() >> null
        def settings = new ProxySettings()

        when:
        parser.readProxySettingsFromProperties(settings)

        then:
        settings.host == null
        settings.port == port
        settings.user == null
        settings.password == null

        where:
        portP  | port
        "-1"   | null
        "0"    | null
        "text" | null
    }

    @Unroll
    def "reset #hostP, #portP, #userP, #passwordP"() {
        setup:
        parser.getHost() >> hostP
        parser.getPort() >> portP
        parser.getUser() >> userP
        parser.getPassword() >> passwordP
        def settings = new ProxySettings(host: "localhost", port: 8080, user: "user", password: "password")

        when:
        parser.readProxySettingsFromProperties(settings)

        then:
        settings.host == host
        settings.port == port
        settings.user == user
        settings.password == password

        where:
        hostP | portP | userP | passwordP | host        | port | user   | password
        ""    | null  | null  | null      | null        | 8080 | "user" | "password"
        null  | ""    | null  | null      | "localhost" | null | "user" | "password"
        null  | null  | ""    | null      | "localhost" | 8080 | null   | "password"
        null  | null  | null  | ""        | "localhost" | 8080 | "user" | null
    }
}