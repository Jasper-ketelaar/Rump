package nl.yasper.rump;

import nl.yasper.rump.client.AsyncRestClient;
import nl.yasper.rump.client.DefaultRestClient;
import nl.yasper.rump.client.RestClient;
import nl.yasper.rump.config.RequestConfig;
import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.response.HttpResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rump {

    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newFixedThreadPool(5);
    private static final DefaultRestClient DEFAULT_CLIENT = new DefaultRestClient(new RequestConfig());
    private static final AsyncRestClient ASYNC_CLIENT = new AsyncRestClient(DEFAULT_CLIENT, DEFAULT_EXECUTOR);

    public static <T> T requestForObject(String path, RequestMethod method, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.requestForObject(path, method, null, responseType, configs);
    }

    public static <T> T getForObject(String path, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.getForObject(path, responseType, configs);
    }

    public static <T> HttpResponse<T> get(String path, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.get(path, responseType, configs);
    }

    public static <T> HttpResponse<T> post(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.post(path, requestBody, responseType, configs);
    }

    public static <T> HttpResponse<T> put(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.put(path, requestBody, responseType, configs);
    }

    public static <T> HttpResponse<T> request(String path, RequestMethod method, Object requestBody,
                                              Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.request(path, method, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> getAsync(String path, Class<T> responseType, RequestConfig... configs) {
        return requestAsync(path, RequestMethod.GET, null, responseType, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> requestAsync(String path, RequestMethod method, Object requestBody,
                                                                      Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.request(path, method, requestBody, responseType);
    }

    public static RestClient create(RequestConfig config, boolean async) {
        DefaultRestClient backing = new DefaultRestClient(config);
        if (async) {
            return new AsyncRestClient(backing, DEFAULT_EXECUTOR);
        }

        return backing;
    }

    public static DefaultRestClient createDefault(RequestConfig config) {
        return new DefaultRestClient(config);
    }

    public static AsyncRestClient createAsync(RequestConfig config, ExecutorService executor) {
        return new AsyncRestClient(new DefaultRestClient(config), executor);
    }
}
