package nl.yasper.rump.response;

import nl.yasper.rump.config.RequestConfig;

import java.util.List;
import java.util.Map;

public class HttpResponse<T> {

    private final T body;
    private final Map<String, List<String>> headerFields;
    private final int responseCode;
    private final RequestConfig requestConfig;

    public HttpResponse(T body, Map<String, List<String>> headerFields, int responseCode, RequestConfig requestConfig) {
        this.body = body;
        this.headerFields = headerFields;
        this.responseCode = responseCode;
        this.requestConfig = requestConfig;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public T getBody() {
        return this.body;
    }

    public Map<String, List<String>> getHeaderFields() {
        return this.headerFields;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
