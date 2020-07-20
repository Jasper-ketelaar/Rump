package nl.yasper.rump.client;

import lombok.RequiredArgsConstructor;
import nl.yasper.rump.config.RequestConfig;
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

@RequiredArgsConstructor
public class DefaultRestClient implements RestClient {

    private static final List<Class<?>> PRIMITIVE_CLASSES = Arrays.asList(
        String.class, Double.class, Integer.class
    );

    private final RequestConfig config;

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
        URL url = new URL(config.getBaseURL() + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.toString());
        if (requestBody != null) {
            try (ObjectOutputStream writer = new ObjectOutputStream(connection.getOutputStream())) {
                writer.writeObject(requestBody);
            }
        }

        T body = transform(connection.getInputStream(), responseType, config);
        return new HttpResponse<>(body, connection.getHeaderFields(), connection.getResponseCode());
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
