package dev.yasper.rump.response;

import dev.yasper.rump.Headers;
import dev.yasper.rump.config.RequestConfig;

import java.util.List;
import java.util.Map;

public class HttpResponse<T> {

    private final Headers responseHeaders;
    private final int responseCode;
    private final String responseMessage;
    private final RequestConfig requestConfig;
    private final String url;
    private T body;

    public HttpResponse(T body, Headers responseHeaders, int responseCode, String responseMessage, RequestConfig requestConfig,
                        String url) {
        this.body = body;
        this.responseHeaders = responseHeaders;
        this.responseCode = responseCode;
        this.requestConfig = requestConfig;
        this.url = url;
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getUrl() {
        return url;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public T getBody() {
        return this.body;
    }

    public void setBody(T to) {
        this.body = to;
    }

    public Headers getResponseHeaders() {
        return this.responseHeaders;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
