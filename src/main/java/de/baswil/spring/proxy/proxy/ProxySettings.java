package de.baswil.spring.proxy.proxy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProxySettings {
    private String host;
    private Integer port;
    private String user;
    private String password;
}
