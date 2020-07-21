package dev.yasper.rump.response;

import dev.yasper.rump.config.RequestConfig;

import java.util.List;
import java.util.Map;

public class HttpResponse<T> {

    private final Map<String, List<String>> headerFields;
    private final int responseCode;
    private final String responseMessage;
    private final RequestConfig requestConfig;
    private final String url;
    private T body;

    public HttpResponse(T body, Map<String, List<String>> headerFields, int responseCode, String responseMessage, RequestConfig requestConfig,
                        String url) {
        this.body = body;
        this.headerFields = headerFields;
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

    public Map<String, List<String>> getHeaderFields() {
        return this.headerFields;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
