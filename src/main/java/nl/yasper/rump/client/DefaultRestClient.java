package nl.yasper.rump.client;

import nl.yasper.rump.config.RequestConfig;
import nl.yasper.rump.interceptor.RequestInterceptor;
import nl.yasper.rump.interceptor.ResponseInterceptor;
import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.response.HttpResponse;
import nl.yasper.rump.response.ResponseBody;
import nl.yasper.rump.response.ResponseTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DefaultRestClient implements RestClient {

    private static final List<Class<?>> PRIMITIVE_CLASSES = Arrays.asList(
            String.class, Double.class, Integer.class
    );
    private final RequestConfig config;

    public DefaultRestClient(RequestConfig config) {
        this.config = config;
    }

    public RequestConfig getConfig() {
        return config;
    }

    public <T> T getForObject(String path, Class<T> responseType,
                              RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.GET, null, responseType, merging);
    }

    public <T> T postForObject(String path, Object requestBody, Class<T> responseType,
                               RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    public <T> T putForObject(String path, Object requestBody, Class<T> responseType,
                              RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    public <T> T requestForObject(String path, RequestMethod method, Object requestBody, Class<T> responseType,
                                  RequestConfig... merging) throws IOException {
        return request(path, method, requestBody, responseType, merging).getBody();
    }

    public <T> HttpResponse<T> post(String path, Object requestBody, Class<T> responseType,
                                    RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.POST, requestBody, responseType, merging);
    }

    public <T> HttpResponse<T> put(String path, Object requestBody, Class<T> responseType,
                                   RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    public <T> HttpResponse<T> get(String path, Class<T> responseType,
                                   RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.GET, null, responseType, merging);
    }

    public <T> HttpResponse<T> request(String path, RequestMethod method, Object requestBody, Class<T> responseType,
                                       RequestConfig... merging) throws IOException {
        RequestConfig config = this.config.merge(merging);
        String urlMerged = config.getBaseURL() + path + config.getParams().toURLPart();
        URL url = new URL(urlMerged);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyConfig(connection, config);
        if (!beforeRequest(config, urlMerged, connection)) {
            connection.disconnect();
            return null;
        }

        connection.setRequestMethod(method.toString());
        if (requestBody != null) {
            try (ObjectOutputStream writer = new ObjectOutputStream(connection.getOutputStream())) {
                writer.writeObject(requestBody);
            }
        }


        T body = transform(connection.getInputStream(), responseType, config);
        HttpResponse<T> res = new HttpResponse<>(
                body, connection.getHeaderFields(),
                connection.getResponseCode(), config, urlMerged
        );
        if (!beforeResponse(res)) {
            return null;
        }

        return res;
    }

    private boolean beforeResponse(HttpResponse<?> res) {
        for (ResponseInterceptor interceptor : config.getResponseInterceptors()) {
            if (!interceptor.beforeResponse(res)) {
                return false;
            }
        }

        return true;
    }

    private boolean beforeRequest(RequestConfig config, String mergedURL, HttpURLConnection connection) {
        for (RequestInterceptor interceptor : config.getRequestInterceptors()) {
            if (!interceptor.beforeRequest(mergedURL, connection, config)) {
                return false;
            }
        }

        return true;
    }

    private void applyConfig(HttpURLConnection connection, RequestConfig config) {
        connection.setConnectTimeout(config.getTimeout());
        connection.setReadTimeout(config.getReadTimeout());
        for (String key : config.getRequestHeaders().headerKeys()) {
            connection.setRequestProperty(key, config.getRequestHeaders().getHeader(key));
        }

        connection.setUseCaches(config.isUsingCaches());
    }

    private <T> T transform(InputStream input, Class<T> responseType, RequestConfig config) throws IOException {
        if (responseType == ResponseBody.class || PRIMITIVE_CLASSES.contains(responseType)) {
            ResponseBody body = new ResponseBody(input);
            if (responseType == ResponseBody.class) {
                return responseType.cast(body);
            } else {
                return body.getAs(responseType);
            }
        }

        ResponseTransformer transformer = config.getResponseTransformer();
        return transformer.transform(input, responseType);
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}
