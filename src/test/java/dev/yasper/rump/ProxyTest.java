package dev.yasper.rump;

import dev.yasper.rump.client.DefaultRestClient;
import dev.yasper.rump.config.RequestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyTest {

    private DefaultRestClient withProxy;

    @Before
    public void init() {
        SocketAddress address = new InetSocketAddress("45.225.93.66", 999);
        RequestConfig withProxy = new RequestConfig()
                .setProxy(new Proxy(Proxy.Type.HTTP, address));
        this.withProxy = Rump.createDefault(withProxy);
    }

    @Test
    public void fetchIp() throws IOException {
        String withProxy = this.withProxy.getForObject("https://api.ipify.org/?format=json", String.class);
        String res = Rump.getForObject("https://api.ipify.org/?format=json", String.class);
        Assert.assertNotEquals(withProxy, res);
    }
}
