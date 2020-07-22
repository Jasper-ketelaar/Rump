package dev.yasper.rump.client;

import dev.yasper.rump.Headers;
import dev.yasper.rump.Rump;
import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.exception.HttpStatusCodeException;
import dev.yasper.rump.interceptor.RequestInterceptor;
import dev.yasper.rump.interceptor.ResponseInterceptor;
import dev.yasper.rump.request.RequestMethod;
import dev.yasper.rump.response.HttpResponse;
import dev.yasper.rump.response.ResponseBody;
import dev.yasper.rump.response.ResponseTransformer;

import javax.lang.model.type.NullType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DefaultRestClient implements RestClient {

    private static final List<Class<?>> PRIMITIVE_CLASSES = Arrays.asList(
            String.class, Double.class, Integer.class
    );
    private static final int LAST_SUCCESSFUL_RESPONSE = 299;
    private final RequestConfig config;

    protected DefaultRestClient(RequestConfig config) {
        this.config = config;
    }

    public static DefaultRestClient create(RequestConfig config) {
        return new DefaultRestClient(Rump.DEFAULT_CONFIG.merge(config));
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

    public <T> T deleteForObject(String path, Class<T> responseType, RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.DELETE, null, responseType, merging);
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

    public <T> HttpResponse<T> delete(String path, Class<T> repsonseType, RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.DELETE, null, repsonseType, merging);
    }

    public HttpResponse<Void> head(String path, RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.HEAD, null, Void.class, merging);
    }

    public <T> HttpResponse<T> request(String path, RequestMethod method, Object requestBody,
                                       Class<T> responseType, RequestConfig... merging) throws IOException {
        return request(path, requestBody, responseType, this.config.merge(method.toConfig()).merge(merging));
    }

    public <T> HttpResponse<T> request(String path, Object requestBody, Class<T> responseType,
                                       RequestConfig... merging) throws IOException {
        RequestConfig config = this.config.merge(merging);
        return request(path, requestBody, responseType, config);
    }


    private <T> HttpResponse<T> request(String path, Object requestBody, Class<T> responseType,
                                        RequestConfig config) throws IOException {
        String urlMerged = config.getBaseURL() + path + config.getParams().toURLPart();
        URL url = new URL(urlMerged);

        HttpURLConnection connection = openWithProxyIfPresent(url, config.getProxy());
        applyConfig(connection, config);
        if (!beforeRequest(config, urlMerged, connection)) {
            connection.disconnect();
            return null;
        }

        if (requestBody != null && config.isOutputting()) {
            writeToConnection(connection, requestBody, config);
        }

        Headers responseHeaders = new Headers(connection.getHeaderFields());
        if (connection.getResponseCode() > LAST_SUCCESSFUL_RESPONSE
                && !config.getIgnoreStatusCode().test(connection.getResponseCode())) {
            ResponseBody body = new ResponseBody(connection.getErrorStream());
            HttpResponse<String> errorResponse = new HttpResponse<>(
                    body.getAsString(), responseHeaders,
                    connection.getResponseCode(), connection.getResponseMessage(),
                    config, urlMerged
            );

            config.getExceptionHandler().onHttpException(new HttpStatusCodeException(errorResponse));
            return null;
        }

        T body = transform(connection.getInputStream(), responseType, config);
        HttpResponse<T> res = new HttpResponse<>(
                body, responseHeaders,
                connection.getResponseCode(), connection.getResponseMessage(),
                config, urlMerged
        );
        if (!beforeResponse(res)) {
            return null;
        }

        return res;
    }

    private void writeToConnection(HttpURLConnection connection, Object requestBody, RequestConfig config) throws IOException {
        String mapped = config.getRequestTransformer().transform(requestBody, config.getRequestHeaders())
                .toString();
        try (Writer out = new OutputStreamWriter(connection.getOutputStream())) {
            BufferedWriter writer = new BufferedWriter(out);
            writer.write(mapped);
        }
    }

    private HttpURLConnection openWithProxyIfPresent(URL url, Proxy proxy) throws IOException {
        if (proxy == null) {
            return (HttpURLConnection) url.openConnection();
        } else {
            return (HttpURLConnection) url.openConnection(proxy);
        }
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

    private void applyConfig(HttpURLConnection connection, RequestConfig config) throws ProtocolException {
        connection.setConnectTimeout(config.getTimeout());
        connection.setReadTimeout(config.getReadTimeout());
        connection.setRequestMethod(config.getMethod().toString());
        if (config.isOutputting()) {
            connection.setDoOutput(true);
        }

        if (config.getAuthenticator() != null) {
            connection.setAuthenticator(config.getAuthenticator());
        }
        for (String key : config.getRequestHeaders().headerKeys()) {
            connection.setRequestProperty(key, config.getRequestHeaders().getSafeValue(key));
        }

        connection.setUseCaches(config.isUsingCaches());
        config.getConnectionConsumer().accept(connection);
    }

    private <T> T transform(InputStream input, Class<T> responseType, RequestConfig config) throws IOException {
        if (config.getMethod() == RequestMethod.HEAD) {
            return null;
        }

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
