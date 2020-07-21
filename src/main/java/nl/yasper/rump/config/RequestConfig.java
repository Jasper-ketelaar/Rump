package nl.yasper.rump.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
                .setUseCaches(from.isUseCaches());
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

}
