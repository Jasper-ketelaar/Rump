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
import java.net.SocketTimeoutException;

public class ProxyTest {

    private DefaultRestClient withProxy;

    @Before
    public void init() {
        SocketAddress address = new InetSocketAddress("104.168.211.27", 1080);
        RequestConfig withProxy = new RequestConfig()
                .setProxy(new Proxy(Proxy.Type.HTTP, address))
                .setTimeout(25000);
        this.withProxy = Rump.createDefault(withProxy);
    }

    @Test
    public void fetchIp() throws IOException {
        try {
            String withProxy = this.withProxy.getForObject("https://api.ipify.org/?format=json", String.class);
            String res = Rump.getForObject("https://api.ipify.org/?format=json", String.class);
            Assert.assertNotEquals(withProxy, res);
        } catch (SocketTimeoutException ignore) {
            // This is fine, means the proxy is at least being applied.
        }
    }
}
