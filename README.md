# Spring Proxy

This small library gives you the possibility to handle proxy settings 
and convert these to the default java proxy system properties.
This is a benefit, because a lot of libraries use the default java proxy system properties 
and this makes it easier to use proxies for libraries in the Spring universe.
An example is the Feign Client from the Spring Cloud library that need no more settings for proxies, 
if this library is used.  

## Get Library

The library is available over the jcenter maven repository. 
You mast only setup the jcenter repository and use the dependency:

### maven:
```xml
...
<repositories>
    <repository>
        <id>jcenter</id>
        <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>de.baswil.spring</groupId>
        <artifactId>spring-proxy</artifactId>
        <version>1.2</version>
    </dependency>
</dependencies>
...
```
### gradle:

```groovy
...
repositories {
    jcenter()
}

dependencies {
    compile 'de.baswil.spring:spring-proxy:1.2'
}
...
``` 

## Use the library

The library implements an ApplicationListener for Spring applications which handles system property, spring properties and environment variables.
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

### custom format for `http.nonProxyHosts` application property

Sometimes the property `http.nonProxyHosts` contains a custom format. 
In such situations there are two properties for customize the parsing of the no proxy property:
 - `proxy-format.app-http-no-proxy.format`
 - `proxy-format.app-http-no-proxy.formatter`
 
`proxy-format.app-http-no-proxy.format` defines the format. The values `OS`, `JAVA` and `OTHER` are possible.
The default is `JAVA` and do not change the value of the no proxy hosts. 
`OS` stands for the format of a normal environment variable (comma as delimiter).
With `OTHER` you can define a custom formatter with the `proxy-format.app-http-no-proxy.formatter` property.
This library contains some implementations:
 - `de.baswil.spring.proxy.noproxy.NoChangeProxyFormatter`  
 Same as setting `proxy-format.app-http-no-proxy.format` to `JAVA`  
 - `de.baswil.spring.proxy.noproxy.OSNoProxyFormatter`  
 Same as setting `proxy-format.app-http-no-proxy.format` to `OS`  
 - `de.baswil.spring.proxy.noproxy.OSIncludingAllSubDomainsNoProxyFormatter`  
 Extends the formatter `de.baswil.spring.proxy.noproxy.OSNoProxyFormatter` and include all sub domains of domains

All other formats are possible if you implement the `de.baswil.spring.proxy.noproxy.NoProxyFormatter` interface.

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

The ProxyInformation bean has methods for all proxy settings and forwards this method call to the system properties.

## Why an ApplicationListener?

An application Listener is used, because of the starting process of SpringBoot. 
When a SpringBoot application start, SpringBoot configure a lot of things and beans at the start or later at runtime.
At this process SpringBoot do not guarantee an order of the configurations.
This is the reason why an ApplicationListener is needed.
The ProxyApplicationListener is run, between the ApplicationContext of the SpringBoot application was build and 
the application was started. So any (Auto)Configuration has the chance to get the proxy settings.