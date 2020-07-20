package nl.yasper.rump.client;

import nl.yasper.rump.request.RequestMethod;
import nl.yasper.rump.response.HttpResponse;

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

    public <T> CompletableFuture<T> getForObject(String path, Class<T> responseType) {
        return requestForObject(path, RequestMethod.GET, null, responseType);
    }

    public <T> CompletableFuture<T> postForObject(String path, Object requestBody, Class<T> responseType) {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType);
    }

    public <T> CompletableFuture<T> putForObject(String path, Object requestBody, Class<T> responseType) {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType);
    }

    public <T> CompletableFuture<T> requestForObject(String path, RequestMethod method, Object requestBody, Class<T> responseType) {
        return request(path, method, requestBody, responseType)
                .thenApply(HttpResponse::getBody);
    }

    public <T> CompletableFuture<HttpResponse<T>> post(String path, Object requestBody, Class<T> responseType) {
        return request(path, RequestMethod.POST, requestBody, responseType);
    }

    public <T> CompletableFuture<HttpResponse<T>> put(String path, Object requestBody, Class<T> responseType) {
        return request(path, RequestMethod.PUT, requestBody, responseType);
    }

    public <T> CompletableFuture<HttpResponse<T>> get(String path, Class<T> responseType) {
        return request(path, RequestMethod.GET, null, responseType);
    }

    public <T> CompletableFuture<HttpResponse<T>> request(String path, RequestMethod method, Object requestBody, Class<T> responseType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return backing.request(path, method, requestBody, responseType);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }


    @Override
    public boolean isAsync() {
        return true;
    }
}
