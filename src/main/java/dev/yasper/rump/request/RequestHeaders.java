package dev.yasper.rump.request;

import dev.yasper.rump.config.RequestConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestHeaders {

    private final Map<String, String> headers = new HashMap<>();

    public static RequestHeaders merge(RequestHeaders first, RequestHeaders second) {
        RequestHeaders result = new RequestHeaders();
        for (String key : first.headerKeys()) {
            result.setHeader(key, first.getHeader(key));
        }

        for (String key : second.headerKeys()) {
            result.setHeader(key, second.getHeader(key));
        }

        return result;
    }

    public Set<String> headerKeys() {
        return headers.keySet();
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    public RequestHeaders setContentType(String type) {
        headers.put("Content-Type", type);
        return this;
    }

    public RequestHeaders setContentType(ContentType type) {
        return setContentType(type.getCode());
    }

    public RequestHeaders setUserAgent(String userAgent) {
        headers.put("User-Agent", userAgent);
        return this;
    }

    public RequestConfig toConfig() {
        return new RequestConfig()
                .setRequestHeaders(this);
    }

    public RequestHeaders setAuthentication(String authentication) {
        headers.put("Authentication", authentication);
        return this;
    }

    public RequestHeaders setHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public enum ContentType {
        TEXT("text/plain"),
        JSON("application/json");

        private final String code;

        ContentType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
