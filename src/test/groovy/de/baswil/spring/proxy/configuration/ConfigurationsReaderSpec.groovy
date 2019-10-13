package de.baswil.spring.proxy.configuration

import de.baswil.spring.proxy.noproxy.NoProxyFormat
import de.baswil.spring.proxy.noproxy.NoProxyFormatter
import org.springframework.core.env.ConfigurableEnvironment
import spock.lang.Specification
import spock.lang.Unroll

class ConfigurationsReaderSpec extends Specification {
    Map<String, Object> systemEnvironment = Mock Map
    ConfigurableEnvironment environment = Mock ConfigurableEnvironment
    ConfigurationsReader configurationsReader = new ConfigurationsReader(environment)

    void setup() {
        environment.getSystemEnvironment() >> systemEnvironment
    }

    def "default values"() {
        when:
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, _ as NoProxyFormat) >> {
            it[2]
        }
        def configurations = configurationsReader.readConfigurations()

        then:
        configurations.appHttpProxyHost == null
        configurations.appHttpProxyPort == null
        configurations.appHttpProxyUser == null
        configurations.appHttpProxyPassword == null

        configurations.appHttpsProxyHost == null
        configurations.appHttpsProxyPort == null
        configurations.appHttpsProxyUser == null
        configurations.appHttpsProxyPassword == null

        configurations.appNonProxyHosts == null
        configurations.appNonProxyHostsFormat == NoProxyFormat.JAVA
        configurations.appNonProxyHostsFormatter == null

        configurations.osHttpProxy == null
        configurations.osHttpsProxy == null
        configurations.osNoProxy == null
    }

    @Unroll
    def "App #propertyKey property set"() {
        when:
        environment.getProperty(propertyKey, String.class) >> propertyValue
        def configurations = configurationsReader.readConfigurations()

        then:
        configurations[configurationsPropertyName] == propertyValue

        where:
        propertyKey                             | configurationsPropertyName | propertyValue
        Constants.APP_HTTP_PROXY_HOST_PROP      | "appHttpProxyHost"         | "host"
        Constants.APP_HTTP_PROXY_PORT_PROP      | "appHttpProxyPort"         | "8080"
        Constants.APP_HTTP_PROXY_USER_PROP      | "appHttpProxyUser"         | "user"
        Constants.APP_HTTP_PROXY_PASSWORD_PROP  | "appHttpProxyPassword"     | "password"
        Constants.APP_HTTPS_PROXY_HOST_PROP     | "appHttpsProxyHost"        | "host"
        Constants.APP_HTTPS_PROXY_PORT_PROP     | "appHttpsProxyPort"        | "8443"
        Constants.APP_HTTPS_PROXY_USER_PROP     | "appHttpsProxyUser"        | "user"
        Constants.APP_HTTPS_PROXY_PASSWORD_PROP | "appHttpsProxyPassword"    | "password"
        Constants.APP_NON_PROXY_HOSTS_PROP      | "appNonProxyHosts"         | "host1|*.host2"
    }

    @Unroll
    def "Os #propertyKey Variable set"() {
        when:
        systemEnvironment.get(propertyKey) >> propertyValue
        def configurations = configurationsReader.readConfigurations()

        then:
        configurations[configurationsPropertyName] == propertyValue

        where:
        propertyKey                                 | configurationsPropertyName | propertyValue
        Constants.OS_HTTP_PROXY_PROP.toLowerCase()  | "osHttpProxy"              | "http://test:testPassword@localhost:8080"
        Constants.OS_HTTP_PROXY_PROP.toUpperCase()  | "osHttpProxy"              | "http://test:testPassword@localhost:8080"
        Constants.OS_HTTPS_PROXY_PROP.toLowerCase() | "osHttpsProxy"             | "https://test:testPassword@localhost:8080"
        Constants.OS_HTTPS_PROXY_PROP.toUpperCase() | "osHttpsProxy"             | "https://test:testPassword@localhost:8080"
        Constants.OS_NO_PROXY_PROP.toLowerCase()    | "osNoProxy"                | "host1,*.host2"
        Constants.OS_NO_PROXY_PROP.toUpperCase()    | "osNoProxy"                | "host1,*.host2"
    }

    @Unroll
    def "App appNonProxyHostsFormat property with value '#value'"() {
        when:
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP.toLowerCase(), NoProxyFormat.class, _ as NoProxyFormat) >> value
        def configurations = configurationsReader.readConfigurations()

        then:
        configurations.appNonProxyHostsFormat == value

        where:
        value               | _
        NoProxyFormat.OS    | _
        NoProxyFormat.JAVA  | _
        NoProxyFormat.OTHER | _
    }

    def "App appNonProxyHostsFormatter property"() {
        when:
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMATTER_PROP.toLowerCase(), String.class) >> NormalFormatter.class.getName()
        def configurations = configurationsReader.readConfigurations()

        then:
        configurations.appNonProxyHostsFormatter == NormalFormatter.class
    }

    static class NormalFormatter implements NoProxyFormatter {
        @Override
        List<String> formatHostName(String hostname) {
            return null
        }

        @Override
        String hostDelimiter() {
            return null
        }
    }

    def "App appNonProxyHostsFormatter property with not existing class"() {
        when:
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMATTER_PROP.toLowerCase(), String.class) >> "lalalalalalalalala"
        configurationsReader.readConfigurations()

        then:
        thrown ConfigurationsException
    }

    def "App appNonProxyHostsFormatter property with class with wrong type"() {
        when:
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMATTER_PROP.toLowerCase(), String.class) >> "java.lang.String"
        configurationsReader.readConfigurations()

        then:
        thrown ConfigurationsException
    }
}