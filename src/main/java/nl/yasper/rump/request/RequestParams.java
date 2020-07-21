package nl.yasper.rump.request;

import nl.yasper.rump.config.RequestConfig;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestParams {

    private final Map<String, String> requestParams = new HashMap<>();
    private boolean encoded = true;
    private Charset charset = StandardCharsets.UTF_8;

    public String toURLPart() {
        if (requestParams.size() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder("?");
        int index = 0;
        Set<String> keySet = requestParams.keySet();
        for (String key : keySet) {
            if (index > 0) {
                result.append("&");
            }
            result.append(key);
            result.append("=");

            String value = requestParams.get(key);
            result.append(isEncoded() ? URLEncoder.encode(value, getCharset()) : requestParams.get(key));
            index++;
        }

        return result.toString();
    }

    public RequestParams add(String key, String value) {
        this.requestParams.put(key, value);
        return this;
    }

    public RequestParams add(String key, Object value) {
        this.requestParams.put(key, value.toString());
        return this;
    }


    public boolean isEncoded() {
        return this.encoded;
    }

    public RequestParams setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public RequestParams setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public RequestConfig toConfig() {
        return new RequestConfig()
                .setParams(this);
    }
}
