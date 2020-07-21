package nl.yasper.rump.config;

import nl.yasper.rump.interceptor.RequestInterceptor;
import nl.yasper.rump.interceptor.ResponseInterceptor;
import nl.yasper.rump.request.RequestHeaders;
import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.request.RequestParams;
import nl.yasper.rump.request.RequestTransformer;
import nl.yasper.rump.response.JacksonResponseTransformer;
import nl.yasper.rump.response.ResponseTransformer;

import java.util.LinkedList;
import java.util.List;

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

    public int getTimeout() {
        return this.timeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public RequestHeaders getRequestHeaders() {
        return this.requestHeaders;
    }

    public boolean isUsingCaches() {
        return this.useCaches;
    }

    public RequestParams getParams() {
        return this.params;
    }

    public RequestTransformer getRequestTransformer() {
        return this.requestTransformer;
    }

    public ResponseTransformer getResponseTransformer() {
        return this.responseTransformer;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return this.requestInterceptors;
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return this.responseInterceptors;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public RequestConfig setBaseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    public RequestConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RequestConfig setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RequestConfig setRequestHeaders(RequestHeaders requestHeaders) {
        this.requestHeaders = requestHeaders;
        return this;
    }

    public RequestConfig setUseCaches(boolean useCaches) {
        this.useCaches = useCaches;
        return this;
    }

    public RequestConfig setParams(RequestParams params) {
        this.params = params;
        return this;
    }

    public RequestConfig setRequestTransformer(RequestTransformer requestTransformer) {
        this.requestTransformer = requestTransformer;
        return this;
    }

    public RequestConfig setResponseTransformer(ResponseTransformer responseTransformer) {
        this.responseTransformer = responseTransformer;
        return this;
    }

    public RequestConfig setRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
        this.requestInterceptors = requestInterceptors;
        return this;
    }

    public RequestConfig setResponseInterceptors(List<ResponseInterceptor> responseInterceptors) {
        this.responseInterceptors = responseInterceptors;
        return this;
    }

    public RequestConfig setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }
}
