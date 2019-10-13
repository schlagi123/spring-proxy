package de.baswil.spring.proxy.proxy;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains settings for a default proxy (http or https)
 *
 * @author Bastian Wilhelm
 */
@Getter
@Setter
public class ProxySettings {
    private String host;
    private Integer port;
    private String user;
    private String password;
}
