/**
 * Rump is a REST client for Java that allows for easy configuration and default values.
 *
 * Copyright (C) 2020 Jasper Ketelaar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
 * See {@link DefaultRestClient} or {@link AsyncRestClient} for better documentation on the methods.
 * This class just encapsulates two instances of those clients for its methods.
 */
public class Rump {

    /**
     * Default config values for Rump
     */
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

    /**
     * Create a {@link RestClient} instance.
     *
     * @param config The config from which to construct the client
     * @param async  Whether or not the returned client should be for async requests
     * @return The constructed {@link RestClient}
     */
    public static RestClient create(RequestConfig config, boolean async) {
        DefaultRestClient backing = DefaultRestClient.create(config);
        if (async) {
            return new AsyncRestClient(backing, DEFAULT_EXECUTOR);
        }

        return backing;
    }

    /**
     * Creates a {@link DefaultRestClient} instance
     *
     * @param config The config from which to construct the client
     * @return The constructed {@link DefaultRestClient}
     */
    public static DefaultRestClient createDefault(RequestConfig config) {
        return DefaultRestClient.create(config);
    }

    /**
     * Creates an {@link AsyncRestClient} instance
     *
     * @param config   The config from which to construct the client
     * @param executor The executor to construct this instance from. See {@link Executors} to construct an executor.
     * @return The constructed {@link AsyncRestClient}
     */
    public static AsyncRestClient createAsync(RequestConfig config, ExecutorService executor) {
        return new AsyncRestClient(DefaultRestClient.create(config), executor);
    }
}
