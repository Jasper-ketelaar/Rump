package dev.yasper.rump.client;

import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.exception.HttpStatusCodeException;
import dev.yasper.rump.request.RequestMethod;
import dev.yasper.rump.response.HttpResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

public class AsyncRestClient implements RestClient {

    private final DefaultRestClient backing;
    private final ExecutorService executor;

    public AsyncRestClient(DefaultRestClient backing, ExecutorService executor) {
        this.backing = backing;
        this.executor = executor;
    }

    public <T> CompletableFuture<T> getForObject(String path, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.GET, null, responseType, merging);
    }

    public <T> CompletableFuture<T> postForObject(String path, Object requestBody, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    public <T> CompletableFuture<T> putForObject(String path, Object requestBody, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    public <T> CompletableFuture<T> deleteForObject(String path, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.DELETE, null, responseType, merging);
    }

    public <T> CompletableFuture<T> requestForObject(String path, RequestMethod method, Object requestBody,
                                                     Class<T> responseType, RequestConfig... merging) {
        return request(path, method, requestBody, responseType, merging)
                .thenApply(HttpResponse::getBody);
    }

    public <T> CompletableFuture<HttpResponse<T>> post(String path, Object requestBody, Class<T> responseType,
                                                       RequestConfig... merging) {
        return request(path, RequestMethod.POST, requestBody, responseType, merging);
    }

    public <T> CompletableFuture<HttpResponse<T>> put(String path, Object requestBody, Class<T> responseType,
                                                      RequestConfig... merging) {
        return request(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    public <T> CompletableFuture<HttpResponse<T>> get(String path, Class<T> responseType, RequestConfig... merging) {
        return request(path, RequestMethod.GET, null, responseType, merging);
    }

    public <T> CompletableFuture<HttpResponse<T>> delete(String path, Class<T> responseType, RequestConfig... merging) {
        return request(path, RequestMethod.DELETE, null, responseType, merging);
    }

    public CompletableFuture<HttpResponse<Void>> head(String path, RequestConfig... merging) {
        return request(path, RequestMethod.HEAD, null, Void.class, merging);
    }

    public <T> CompletableFuture<HttpResponse<T>> request(String path, RequestMethod method, Object requestBody,
                                                          Class<T> responseType, RequestConfig... merging) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return backing.request(path, method, requestBody, responseType, merging);
            } catch (IOException | HttpStatusCodeException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
