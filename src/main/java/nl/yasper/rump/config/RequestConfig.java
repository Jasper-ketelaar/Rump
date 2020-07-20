package nl.yasper.rump.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import nl.yasper.rump.request.RequestHeaders;
import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.request.RequestParams;
import nl.yasper.rump.request.RequestTransformer;
import nl.yasper.rump.response.JacksonResponseTransformer;
import nl.yasper.rump.response.ResponseTransformer;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class RequestConfig {

    private String baseURL = "";

    private int timeout;
    private int readTimeout;

    private RequestHeaders requestHeaders = new RequestHeaders();

    private boolean useCaches;

    private RequestParams params = new RequestParams();
    private RequestTransformer requestTransformer = (obj, headers) -> obj;
    private ResponseTransformer responseTransformer = new JacksonResponseTransformer();

    private RequestMethod method = RequestMethod.GET;

    public static RequestConfig copyProperties(RequestConfig to, RequestConfig from) {
        return to.setBaseURL(from.getBaseURL())
                .setRequestHeaders(RequestHeaders.merge(to.getRequestHeaders(), from.getRequestHeaders()))
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
        for (String key : requestHeaders.headerKeys()) {
            connection.setRequestProperty(key, requestHeaders.getHeader(key));
        }

        connection.setUseCaches(useCaches);
    }

    public RequestConfig merge(RequestConfig... merging) {
        RequestConfig result = new RequestConfig();
        result = RequestConfig.copyProperties(result, this);
        for (RequestConfig merge : merging) {
            result = RequestConfig.copyProperties(result, merge);
        }

        return result;
    }

}
