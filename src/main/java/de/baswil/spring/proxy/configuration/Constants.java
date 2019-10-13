package de.baswil.spring.proxy.configuration;

/**
 * Contains all constants for the library.
 *
 * @author Bastian Wilhelm
 */
public class Constants {
    // OS Properties
    /**
     * Name of the environment variable for a http proxy.
     */
    public static final String OS_HTTP_PROXY_PROP = "http_proxy";

    /**
     * Name of the environment variable for a https proxy.
     */
    public static final String OS_HTTPS_PROXY_PROP = "https_proxy";

    /**
     * Name of the environment variable for the no proxy hosts for http and https proxies.
     */
    public static final String OS_NO_PROXY_PROP = "no_proxy";

    // Application Properties
    /**
     * Application variable for the http proxy host.
     */
    public static final String APP_HTTP_PROXY_HOST_PROP = "http.proxyHost";

    /**
     * Application variable for the http proxy port.
     */
    public static final String APP_HTTP_PROXY_PORT_PROP = "http.proxyPort";

    /**
     * Application variable for the http proxy user.
     */
    public static final String APP_HTTP_PROXY_USER_PROP = "http.proxyUser";

    /**
     * Application variable for the http proxy password.
     */
    public static final String APP_HTTP_PROXY_PASSWORD_PROP = "http.proxyPassword";

    /**
     * Application variable for the https proxy host.
     */
    public static final String APP_HTTPS_PROXY_HOST_PROP = "https.proxyHost";

    /**
     * Application variable for the https proxy port.
     */
    public static final String APP_HTTPS_PROXY_PORT_PROP = "https.proxyPort";

    /**
     * Application variable for the https proxy user.
     */
    public static final String APP_HTTPS_PROXY_USER_PROP = "https.proxyUser";

    /**
     * Application variable for the https proxy password.
     */
    public static final String APP_HTTPS_PROXY_PASSWORD_PROP = "https.proxyPassword";


    /**
     * Application variable for the no proxy hosts for http and https proxies.
     */
    public static final String APP_NON_PROXY_HOSTS_PROP = "http.nonProxyHosts";

    // Formatting Settings
    /**
     * Application variable for the format of {@link #APP_NON_PROXY_HOSTS_PROP}.
     */
    public static final String APP_NON_PROXY_HOSTS_FORMAT_PROP = "proxy-format.app-http-no-proxy.format";

    /**
     * Application variable for the formatter of {@link #APP_NON_PROXY_HOSTS_PROP} if {@link #APP_NON_PROXY_HOSTS_FORMAT_PROP} is set.
     */
    public static final String APP_NON_PROXY_HOSTS_FORMATTER_PROP = "proxy-format.app-http-no-proxy.formatter";

    // Java Properties
    /**
     * System property key for http proxy host.
     */
    public static final String JAVA_HTTP_PROXY_HOST_PROP = "http.proxyHost";

    /**
     * System property key for http proxy port.
     */
    public static final String JAVA_HTTP_PROXY_PORT_PROP = "http.proxyPort";

    /**
     * System property key for http proxy user.
     */
    public static final String JAVA_HTTP_PROXY_USER_PROP = "http.proxyUser";

    /**
     * System property key for http proxy password.
     */
    public static final String JAVA_HTTP_PROXY_PASSWORD_PROP = "http.proxyPassword";


    /**
     * System property key for https proxy host.
     */
    public static final String JAVA_HTTPS_PROXY_HOST_PROP = "https.proxyHost";

    /**
     * System property key for https proxy port.
     */
    public static final String JAVA_HTTPS_PROXY_PORT_PROP = "https.proxyPort";

    /**
     * System property key for https proxy user.
     */
    public static final String JAVA_HTTPS_PROXY_USER_PROP = "https.proxyUser";

    /**
     * System property key for https proxy password.
     */
    public static final String JAVA_HTTPS_PROXY_PASSWORD_PROP = "https.proxyPassword";


    /**
     * System property key for non proxy hosts of http and https proxies.
     */
    public static final String JAVA_NON_PROXY_HOSTS_PROP = "http.nonProxyHosts";
}
