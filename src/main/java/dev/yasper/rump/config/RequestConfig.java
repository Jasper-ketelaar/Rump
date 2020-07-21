package dev.yasper.rump.config;

import dev.yasper.rump.interceptor.RequestInterceptor;
import dev.yasper.rump.interceptor.ResponseInterceptor;
import dev.yasper.rump.request.RequestHeaders;
import dev.yasper.rump.request.RequestMethod;
import dev.yasper.rump.request.RequestParams;
import dev.yasper.rump.request.RequestTransformer;
import dev.yasper.rump.response.ResponseTransformer;

import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RequestConfig {

    private String baseURL = null;
    private Integer timeout = null;
    private Integer readTimeout = null;
    private Boolean useCaches = null;
    private Proxy proxy = null;
    private Authenticator authenticator = null;
    private RequestHeaders requestHeaders = null;
    private RequestParams params = null;
    private RequestTransformer requestTransformer = null;
    private ResponseTransformer responseTransformer = null;
    private List<RequestInterceptor> requestInterceptors = null;
    private List<ResponseInterceptor> responseInterceptors = null;
    private RequestMethod method = null;
    private Predicate<Integer> ignoreStatusCode = null;
    private Consumer<HttpURLConnection> connectionConsumer = null;

    /**
     * Method to copy properties from a config instance into another config instance. Checks if the values in
     * from are not null to prevent the config from overwriting all the other values in to.
     *
     * @param to   The RequestConfig to copy the properties into
     * @param from The RequestConfig to copy the properties from
     */
    public static void copyProperties(RequestConfig to, RequestConfig from) {
        for (Field field : from.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(from);
                if (value != null) {
                    field.set(to, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "RequestConfig{" + "baseURL='" + baseURL + '\'' +
                ", timeout=" + timeout +
                ", readTimeout=" + readTimeout +
                ", useCaches=" + useCaches +
                ", proxy=" + proxy +
                ", authenticator=" + authenticator +
                ", requestHeaders=" + requestHeaders +
                ", params=" + params +
                ", requestTransformer=" + requestTransformer +
                ", responseTransformer=" + responseTransformer +
                ", requestInterceptors=" + requestInterceptors +
                ", responseInterceptors=" + responseInterceptors +
                ", method=" + method +
                ", ignoreStatusCode=" + ignoreStatusCode +
                ", connectionConsumer=" + connectionConsumer +
                '}';
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public RequestConfig setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public Predicate<Integer> getIgnoreStatusCode() {
        return ignoreStatusCode;
    }

    public RequestConfig setIgnoreStatusCode(Predicate<Integer> ignoreStatusCode) {
        this.ignoreStatusCode = ignoreStatusCode;
        return this;
    }

    public Consumer<HttpURLConnection> getConnectionConsumer() {
        return connectionConsumer;
    }

    public RequestConfig setConnectionConsumer(Consumer<HttpURLConnection> connectionConsumer) {
        this.connectionConsumer = connectionConsumer;
        return this;
    }

    public RequestConfig merge(RequestConfig... merging) {
        RequestConfig result = new RequestConfig();
        RequestConfig.copyProperties(result, this);
        for (RequestConfig merge : merging) {
            RequestConfig.copyProperties(result, merge);
        }

        return result;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public RequestConfig setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public RequestConfig addRequestInterceptor(RequestInterceptor interceptor) {
        this.requestInterceptors.add(interceptor);
        return this;
    }

    public RequestConfig addResponseInterceptor(ResponseInterceptor interceptor) {
        this.responseInterceptors.add(interceptor);
        return this;
    }

    public String getBaseURL() {
        return this.baseURL;
    }

    public RequestConfig setBaseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public RequestConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public RequestConfig setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RequestHeaders getRequestHeaders() {
        return this.requestHeaders;
    }

    public RequestConfig setRequestHeaders(RequestHeaders requestHeaders) {
        this.requestHeaders = requestHeaders;
        return this;
    }

    public Boolean isUsingCaches() {
        return this.useCaches;
    }

    public RequestParams getParams() {
        return this.params;
    }

    public RequestConfig setParams(RequestParams params) {
        this.params = params;
        return this;
    }

    public RequestTransformer getRequestTransformer() {
        return this.requestTransformer;
    }

    public RequestConfig setRequestTransformer(RequestTransformer requestTransformer) {
        this.requestTransformer = requestTransformer;
        return this;
    }

    public ResponseTransformer getResponseTransformer() {
        return this.responseTransformer;
    }

    public RequestConfig setResponseTransformer(ResponseTransformer responseTransformer) {
        this.responseTransformer = responseTransformer;
        return this;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return this.requestInterceptors;
    }

    public RequestConfig setRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
        this.requestInterceptors = requestInterceptors;
        return this;
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return this.responseInterceptors;
    }

    public RequestConfig setResponseInterceptors(List<ResponseInterceptor> responseInterceptors) {
        this.responseInterceptors = responseInterceptors;
        return this;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public RequestConfig setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }

    public RequestConfig setUseCaches(boolean useCaches) {
        this.useCaches = useCaches;
        return this;
    }
}
