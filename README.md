# Spring Proxy

This small library gives you the possibility to handle proxy settings 
and convert these to the default java proxy system properties.
This is a benefit, because a lot of libraries use the default java proxy system properties (e. g. ApacheHttpClient)
and this makes it easier to use proxies with libraries in the Spring universe.

## Integration

The library implements an ApplicationListener for Spring application which handles all the system property and environment magic.
With Spring boot you must only change your main class from:
```java
package de.baswil.spring.proxy.examples;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleProxyApplication {
  public static void main (String[] args) {
    SpringApplication.run(ExampleProxyApplication.class, args);
  }
}
```

to:
```java
package de.baswil.spring.proxy.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.baswil.spring.proxy.ProxyApplicationListener;

@SpringBootApplication
public class ExampleProxyApplication {
  public static void main (String[] args) {
    SpringApplication app = new SpringApplication(ExampleProxyApplication.class);
    app.addListeners(new ProxyApplicationListener());
    app.run(args);
  }
}
```

## Functionality of the ProxyApplicationListener

The ProxyApplicationListener read in a first step the following environment variables:
 - `http_proxy`
 - `https_proxy` 
 - `no_proxy`

In the next step the listener read the following properties of the spring properties:
 - `http.proxyHost`  
 - `http.proxyPort`  
 - `http.proxyUser`  
 - `http.proxyPassword`  
 - `https.proxyHost`  
 - `https.proxyPort`  
 - `https.proxyUser`  
 - `https.proxyPassword`
 - `http.nonProxyHosts`

The spring application properties have a higher priority, 
so that these properties can override the values of the environment variables.

The Last step is to write the properties in the java system properties if they are set.

## Overriding of Environment Variables

The overriding process in the ApplicationListener is designed to override only the particular set values of a proxy setting.
In the following table are the environment variables and the system variables that can override this variables. 

| Environment Variable | System Properties                                                      |
| ---------------------|------------------------------------------------------------------------|
| http_proxy           | http.proxyHost, http.proxyPort, http.proxyUser, http.proxyPassword     |
| https_proxy          | https.proxyHost, https.proxyPort, https.proxyUser, https.proxyPassword |
| no_proxy             | http.nonProxyHosts                                                     |

If the environment variable `http_proxy` is set to `http://username:password@localhost:8080` 
and in the application the property `http.proxyPort` is set to `9999`, the resulting system properties are:
 - `http.proxyHost = localhost`
 - `http.proxyPort = 9999`
 - `http.proxyUser = username`
 - `http.proxyPassword = password`
 

## Special behavior by `no_proxy` and `http.nonProxyHosts`

The `no_proxy` environment variable is a little bit tricky, 
because it has not the same format as the java `http.nonProxyHosts` system property.
`no_proxy` has a comma (`,`) as delimiter that will be converted to a pipe (`|`) for the `http.nonProxyHosts` property.

Moreover there are some operation systems / tools that do not use a astrix (`*`) `no_proxy` to include all sub domains
(e. g. `.google.de` includes all sub domains of google.de because of the leading dot). 
The `http.nonProxyHosts` property do not support this so the listener added the astrix in front of the dot.

## Unset Environment Variables

In the same way environment variables can be overridden be application properties you can unset the values.
You must only set the application properties without a value (empty string).

## Libraries do not use Default Java Proxy Properties

There are some libraries that do not use the default java system properties for proxy settings.
In this case there two possibilities, to get the information about the set proxy settings:

### Read System Properties:
```java
System.getProperty("http.proxyHost");
```

### Use ProxyInformation Bean:
```java
@Configure
public class UseNoProxySettings {
  @Autowired
  private ProxyInformation proxyInformation;
}
```

The ProxyInformation bean has methods for all proxy settings and forwards this method call this to the system properties.

## Why an ApplicationListener?

An application Listener is used, because of the starting process of SpringBoot. 
When a SpringBoot application start, SpringBoot configure a lot of things and beans at the start or later at runtime.
At this process SpringBoot do not guarantee an order of the configurations.
This is the reason why an ApplicationListener is needed.
The ProxyApplicationListener is run, between the ApplicationContext of the SpringBoot application was build and 
the application was started. So any (Auto)Configuration has the chance to get the proxy settings.