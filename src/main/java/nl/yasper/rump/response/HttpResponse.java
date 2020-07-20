package nl.yasper.rump.response;

import java.util.List;
import java.util.Map;

public class HttpResponse<T> {

    private final T body;
    private final Map<String, List<String>> headerFields;
    private final int responseCode;

    public HttpResponse(T body, Map<String, List<String>> headerFields, int responseCode) {
        this.body = body;
        this.headerFields = headerFields;
        this.responseCode = responseCode;
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
