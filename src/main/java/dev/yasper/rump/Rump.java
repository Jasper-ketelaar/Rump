package dev.yasper.rump;

import dev.yasper.rump.client.AsyncRestClient;
import dev.yasper.rump.client.DefaultRestClient;
import dev.yasper.rump.client.RestClient;
import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.exception.DefaultExceptionHandler;
import dev.yasper.rump.request.JacksonRequestTransformer;
import dev.yasper.rump.request.RequestMethod;
import dev.yasper.rump.request.RequestParams;
import dev.yasper.rump.response.HttpResponse;
import dev.yasper.rump.response.JacksonResponseTransformer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main entry point for Rump.
 */
public class Rump {

    public static final RequestConfig DEFAULT_CONFIG = new RequestConfig()
            .setBaseURL("")
            .setTimeout(7500)
            .setReadTimeout(7500)
            .setUseCaches(false)
            .setRequestHeaders(new Headers())
            .setParams(new RequestParams())
            .setRequestTransformer(new JacksonRequestTransformer())
            .setResponseTransformer(new JacksonResponseTransformer())
            .setRequestInterceptors(new LinkedList<>())
            .setResponseInterceptors(new LinkedList<>())
            .setMethod(RequestMethod.GET)
            .setIgnoreStatusCode((val) -> false)
            .setExceptionHandler(new DefaultExceptionHandler())
            .setConnectionConsumer((connection -> {
            }));
    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newFixedThreadPool(5);
    private static final DefaultRestClient DEFAULT_CLIENT = DefaultRestClient.create(DEFAULT_CONFIG);
    private static final AsyncRestClient ASYNC_CLIENT = new AsyncRestClient(DEFAULT_CLIENT, DEFAULT_EXECUTOR);

    public static <T> T requestForObject(String path, RequestMethod method, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.requestForObject(path, method, null, responseType, configs);
    }

    public static <T> T getForObject(String path, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.getForObject(path, responseType, configs);
    }

    public static <T> T postForObject(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.postForObject(path, requestBody, responseType, configs);
    }

    public static <T> T putForObject(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.putForObject(path, requestBody, responseType, configs);
    }

    public static <T> T deleteForObject(String path, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.deleteForObject(path, responseType, configs);
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

    public static <T> HttpResponse<T> delete(String path, Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.delete(path, responseType, configs);
    }

    public static HttpResponse<Void> head(String path, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.head(path, configs);
    }

    public static <T> HttpResponse<T> request(String path, Object requestBody,
                                              Class<T> responseType, RequestConfig... configs) throws IOException {
        return DEFAULT_CLIENT.request(path, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<T> requestForObjectAsync(String path, RequestMethod method, Object requestBody, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.requestForObject(path, method, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<T> getForObjectAsync(String path, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.getForObject(path, responseType, configs);
    }

    public static <T> CompletableFuture<T> postForObjectAsync(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.postForObject(path, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<T> putForObjectAsync(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.putForObject(path, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<T> deleteForObjectAsync(String path, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.deleteForObject(path, responseType, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> getAsync(String path, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.get(path, responseType, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> postAsync(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.post(path, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> putAsync(String path, Object requestBody, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.put(path, requestBody, responseType, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> deleteAsync(String path, Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.delete(path, responseType, configs);
    }

    public static CompletableFuture<HttpResponse<Void>> headAsync(String path, RequestConfig... configs) {
        return ASYNC_CLIENT.head(path, configs);
    }

    public static <T> CompletableFuture<HttpResponse<T>> requestAsync(String path, RequestMethod method, Object requestBody,
                                                                      Class<T> responseType, RequestConfig... configs) {
        return ASYNC_CLIENT.request(path, method, requestBody, responseType);
    }

    public static RestClient create(RequestConfig config, boolean async) {
        DefaultRestClient backing = DefaultRestClient.create(config);
        if (async) {
            return new AsyncRestClient(backing, DEFAULT_EXECUTOR);
        }

        return backing;
    }

    public static DefaultRestClient createDefault(RequestConfig config) {
        return DefaultRestClient.create(config);
    }

    public static AsyncRestClient createAsync(RequestConfig config, ExecutorService executor) {
        return new AsyncRestClient(DefaultRestClient.create(config), executor);
    }
}
