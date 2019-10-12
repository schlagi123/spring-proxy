package de.baswil.spring.proxy.configuration;

import de.baswil.spring.proxy.noproxy.NoProxyFormat;
import de.baswil.spring.proxy.noproxy.NoProxyFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Configurations {
    private String osHttpProxy;
    private String osHttpsProxy;
    private String osNoProxy;

    private String appHttpProxyHost;
    private String appHttpProxyPort;
    private String appHttpProxyUser;
    private String appHttpProxyPassword;

    private String appHttpsProxyHost;
    private String appHttpsProxyPort;
    private String appHttpsProxyUser;
    private String appHttpsProxyPassword;

    private String appNonProxyHosts;
    private NoProxyFormat appNonProxyHostsFormat;
    private Class<? extends NoProxyFormatter> appNonProxyHostsFormatter;
}
