package nl.yasper.rump.interceptor;

import nl.yasper.rump.config.RequestConfig;

import java.net.HttpURLConnection;

public interface RequestInterceptor {

    boolean beforeRequest(String mergedURL, HttpURLConnection connection, RequestConfig config);
}
