package dev.yasper.rump;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyTest {

    @Before
    public void init() {
        SocketAddress address = new InetSocketAddress("45.225.93.66", 999);
        Rump.DEFAULT_CONFIG.setProxy(new Proxy(Proxy.Type.HTTP, address));
    }

    @Test
    public void fetchIp() throws IOException {
        String res = Rump.getForObject("https://api.ipify.org/?format=json", String.class);
        System.out.println(res);
    }
}
