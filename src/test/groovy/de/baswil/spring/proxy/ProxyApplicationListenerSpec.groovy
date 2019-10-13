package de.baswil.spring.proxy

import de.baswil.spring.proxy.configuration.Constants
import de.baswil.spring.proxy.noproxy.NoProxyFormat
import org.junit.Ignore
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.core.env.ConfigurableEnvironment
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties

@Ignore
//ToDo Zum Schluss überarbeiten
class ProxyApplicationListenerSpec extends Specification {
    private ProxyApplicationListener applicationListener
    private ApplicationEnvironmentPreparedEvent applicationEvent
    private ConfigurableEnvironment environment
    private Map<String, Object> systemEnvironment

    void setup() {
        applicationListener = new ProxyApplicationListener()

        applicationEvent = Mock(ApplicationEnvironmentPreparedEvent)

        environment = Mock(ConfigurableEnvironment)
        applicationEvent.getEnvironment() >> environment

        systemEnvironment = new HashMap<>()
        environment.getSystemEnvironment() >> systemEnvironment
    }

    private void setUpSpringEnvironmentProperty(String name, String value) {
        environment.getProperty(name) >> value
    }

    private void setUpOsEnvironmentVariable(String name, String value) {
        if (value != null) {
            systemEnvironment.put(name, value)
        }
    }

    @Unroll
    @RestoreSystemProperties
    def "OS #variable is set (http_proxy)"() {
        setup:
        setUpOsEnvironmentVariable(variable, "http://test:testPassword@localhost:8080")
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA) >> NoProxyFormat.JAVA

        when:
        applicationListener.onApplicationEvent(applicationEvent)

        then:
        System.getProperty("http.proxyHost") == "localhost"
        System.getProperty("http.proxyPort") == "8080"
        System.getProperty("http.proxyUser") == "test"
        System.getProperty("http.proxyPassword") == "testPassword"
        System.getProperty("https.proxyHost") == null
        System.getProperty("https.proxyPort") == null
        System.getProperty("https.proxyUser") == null
        System.getProperty("https.proxyPassword") == null
        System.getProperty("http.nonProxyHosts") == null

        where:
        variable << ["http_proxy", "HTTP_PROXY"]
    }

    @Unroll
    @RestoreSystemProperties
    def "OS #variable is set (https_proxy)"() {
        setup:
        setUpOsEnvironmentVariable(variable, "https://test:testPassword@localhost:8080")
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA) >> NoProxyFormat.JAVA

        when:
        applicationListener.onApplicationEvent(applicationEvent)

        then:
        System.getProperty("http.proxyHost") == null
        System.getProperty("http.proxyPort") == null
        System.getProperty("http.proxyUser") == null
        System.getProperty("http.proxyPassword") == null
        System.getProperty("https.proxyHost") == "localhost"
        System.getProperty("https.proxyPort") == "8080"
        System.getProperty("https.proxyUser") == "test"
        System.getProperty("https.proxyPassword") == "testPassword"
        System.getProperty("http.nonProxyHosts") == null

        where:
        variable << ["https_proxy", "HTTPS_PROXY"]
    }

    @Unroll
    @RestoreSystemProperties
    def "OS #variable is set (no_proxy)"() {
        setup:
        setUpOsEnvironmentVariable(variable, "google.de")
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA) >> NoProxyFormat.JAVA

        when:
        applicationListener.onApplicationEvent(applicationEvent)

        then:
        System.getProperty("http.proxyHost") == null
        System.getProperty("http.proxyPort") == null
        System.getProperty("http.proxyUser") == null
        System.getProperty("http.proxyPassword") == null
        System.getProperty("https.proxyHost") == null
        System.getProperty("https.proxyPort") == null
        System.getProperty("https.proxyUser") == null
        System.getProperty("https.proxyPassword") == null
        System.getProperty("http.nonProxyHosts") == "google.de"

        where:
        variable << ["no_proxy", "NO_PROXY"]
    }

    @RestoreSystemProperties
    def "Spring Properties for http.proxy..."() {
        setup:
        setUpSpringEnvironmentProperty("http.proxyHost", "localhost")
        setUpSpringEnvironmentProperty("http.proxyPort", "8080")
        setUpSpringEnvironmentProperty("http.proxyUser", "user")
        setUpSpringEnvironmentProperty("http.proxyPassword","password")
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA) >> NoProxyFormat.JAVA

        when:
        applicationListener.onApplicationEvent(applicationEvent)

        then:
        System.getProperty("http.proxyHost") == "localhost"
        System.getProperty("http.proxyPort") == "8080"
        System.getProperty("http.proxyUser") == "user"
        System.getProperty("http.proxyPassword") == "password"
        System.getProperty("https.proxyHost") == null
        System.getProperty("https.proxyPort") == null
        System.getProperty("https.proxyUser") == null
        System.getProperty("https.proxyPassword") == null
        System.getProperty("http.nonProxyHosts") == null
    }

    @RestoreSystemProperties
    def "Spring Properties for https.proxy..."() {
        setup:
        setUpSpringEnvironmentProperty("https.proxyHost", "localhost")
        setUpSpringEnvironmentProperty("https.proxyPort", "8080")
        setUpSpringEnvironmentProperty("https.proxyUser", "user")
        setUpSpringEnvironmentProperty("https.proxyPassword","password")
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA) >> NoProxyFormat.JAVA

        when:
        applicationListener.onApplicationEvent(applicationEvent)

        then:
        System.getProperty("http.proxyHost") == null
        System.getProperty("http.proxyPort") == null
        System.getProperty("http.proxyUser") == null
        System.getProperty("http.proxyPassword") == null
        System.getProperty("https.proxyHost") == "localhost"
        System.getProperty("https.proxyPort") == "8080"
        System.getProperty("https.proxyUser") == "user"
        System.getProperty("https.proxyPassword") == "password"
        System.getProperty("http.nonProxyHosts") == null
    }

    @RestoreSystemProperties
    def "Spring Properties for http.nonProxyHosts"() {
        setup:
        setUpSpringEnvironmentProperty("http.nonProxyHosts","localhost|*.google.de")
        environment.getProperty(Constants.APP_NON_PROXY_HOSTS_FORMAT_PROP, NoProxyFormat.class, NoProxyFormat.JAVA) >> NoProxyFormat.JAVA

        when:
        applicationListener.onApplicationEvent(applicationEvent)

        then:
        System.getProperty("http.proxyHost") == null
        System.getProperty("http.proxyPort") == null
        System.getProperty("http.proxyUser") == null
        System.getProperty("http.proxyPassword") == null
        System.getProperty("https.proxyHost") == null
        System.getProperty("https.proxyPort") == null
        System.getProperty("https.proxyUser") == null
        System.getProperty("https.proxyPassword") == null
        System.getProperty("http.nonProxyHosts") == "localhost|*.google.de"
    }
}
