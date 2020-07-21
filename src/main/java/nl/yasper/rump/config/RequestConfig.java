package nl.yasper.rump.config;

import nl.yasper.rump.interceptor.RequestInterceptor;
import nl.yasper.rump.interceptor.ResponseInterceptor;
import nl.yasper.rump.request.RequestHeaders;
import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.request.RequestParams;
import nl.yasper.rump.request.RequestTransformer;
import nl.yasper.rump.response.JacksonResponseTransformer;
import nl.yasper.rump.response.ResponseTransformer;

import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RequestConfig {

    private String baseURL = "";

    private int timeout;
    private int readTimeout;

    private RequestHeaders requestHeaders = new RequestHeaders();

    private boolean useCaches;

    private RequestParams params = new RequestParams();
    private RequestTransformer requestTransformer = (obj, headers) -> obj;
    private ResponseTransformer responseTransformer = new JacksonResponseTransformer();

    private List<RequestInterceptor> requestInterceptors = new LinkedList<>();
    private List<ResponseInterceptor> responseInterceptors = new LinkedList<>();

    private RequestMethod method = RequestMethod.GET;
    private Predicate<Integer> ignoreStatusCode = (val) -> true;
    private Consumer<HttpURLConnection> connectionConsumer = (connection -> {});

    public static RequestConfig copyProperties(RequestConfig to, RequestConfig from) {
        return to.setBaseURL(from.getBaseURL())
                .setRequestHeaders(RequestHeaders.merge(to.getRequestHeaders(), from.getRequestHeaders()))
                .setMethod(from.getMethod())
                .setReadTimeout(from.getReadTimeout())
                .setTimeout(from.getTimeout())
                .setRequestTransformer(from.getRequestTransformer())
                .setResponseTransformer(from.getResponseTransformer())
                .setUseCaches(from.isUsingCaches());
    }

    public Predicate<Integer> getIgnoreStatusCode() {
        return ignoreStatusCode;
    }

    public RequestConfig setIgnoreStatusCode(Predicate<Integer> ignoreStatusCode) {
        this.ignoreStatusCode = ignoreStatusCode;
        return this;
    }

    public RequestConfig withConsumer(Consumer<HttpURLConnection> connectionConsumer) {
        this.connectionConsumer = connectionConsumer;
        return this;
    }

    public Consumer<HttpURLConnection> getConnectionConsumer() {
        return connectionConsumer;
    }

    public RequestConfig merge(RequestConfig... merging) {
        RequestConfig result = new RequestConfig();
        result = RequestConfig.copyProperties(result, this);
        for (RequestConfig merge : merging) {
            result = RequestConfig.copyProperties(result, merge);
        }

        return result;
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

    public boolean isUsingCaches() {
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
