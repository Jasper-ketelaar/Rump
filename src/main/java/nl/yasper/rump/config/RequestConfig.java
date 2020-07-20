package nl.yasper.rump.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.request.RequestTransformer;
import nl.yasper.rump.response.JacksonResponseTransformer;
import nl.yasper.rump.response.ResponseTransformer;

import java.net.HttpURLConnection;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class RequestConfig {

    private String baseURL = "";

    private int timeout;
    private int readTimeout;

    private Map<String, String> requestHeaders;

    private boolean useCaches;

    private RequestTransformer requestTransformer = (obj, headers) -> obj;
    private ResponseTransformer responseTransformer = new JacksonResponseTransformer();

    private RequestMethod method = RequestMethod.GET;

    public static RequestConfig copyProperties(RequestConfig to, RequestConfig from) {
        return to.setBaseURL(from.getBaseURL())
                .setRequestHeaders(from.getRequestHeaders())
                .setMethod(from.getMethod())
                .setReadTimeout(from.getReadTimeout())
                .setTimeout(from.getTimeout())
                .setRequestTransformer(from.getRequestTransformer())
                .setResponseTransformer(from.getResponseTransformer())
                .setUseCaches(from.isUseCaches());
    }

    public void applyConfig(HttpURLConnection connection) {
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(readTimeout);
        for (String key : requestHeaders.keySet()) {
            connection.setRequestProperty(key, requestHeaders.get(key));
        }

        connection.setUseCaches(useCaches);
    }

    public String getContentType() {
        return requestHeaders.get("Content-Type");
    }

    public RequestConfig setContentType(String type) {
        requestHeaders.put("Content-Type", type);
        return this;
    }

    public RequestConfig setContentType(ContentType type) {
        return setContentType(type.getCode());
    }

    public RequestConfig setUserAgent(String userAgent) {
        requestHeaders.put("User-Agent", userAgent);
        return this;
    }

    public RequestConfig merge(RequestConfig... merging) {
        RequestConfig result = new RequestConfig();
        result = RequestConfig.copyProperties(result, this);
        for (RequestConfig merge : merging) {
            result = RequestConfig.copyProperties(result, merge);
        }

        return result;
    }

    public enum ContentType {
        Text("text/plain"),
        JSON("application/json");

        private String code;

        ContentType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
