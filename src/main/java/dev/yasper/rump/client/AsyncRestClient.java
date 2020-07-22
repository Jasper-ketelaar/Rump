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

    /**
     * Constructor for AsyncRestClient
     *
     * @param backing  The backing default (sync) rest client
     * @param executor The executor for this rest client
     */
    public AsyncRestClient(DefaultRestClient backing, ExecutorService executor) {
        this.backing = backing;
        this.executor = executor;
    }

    /**
     * Get the backing executor
     *
     * @return the executor
     */
    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Calls {@link AsyncRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)} using the
     * passed parameters and RequestMethod.GET
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing just the response body of a {@link HttpResponse}.
     */
    public <T> CompletableFuture<T> getForObject(String path, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.GET, null, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)} using the
     * passed parameters and RequestMethod.POST
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody  The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing just the response body of a {@link HttpResponse}.
     */
    public <T> CompletableFuture<T> postForObject(String path, Object requestBody, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.POST, requestBody, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)} using the
     * passed parameters and RequestMethod.PUT
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody  The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing just the response body of a {@link HttpResponse}.
     */
    public <T> CompletableFuture<T> putForObject(String path, Object requestBody, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)} using the
     * passed parameters and RequestMethod.DELETE
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing just the response body of a {@link HttpResponse}.
     */
    public <T> CompletableFuture<T> deleteForObject(String path, Class<T> responseType, RequestConfig... merging) {
        return requestForObject(path, RequestMethod.DELETE, null, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} and maps
     * the response to just return the body
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param method       The method to use for this request
     * @param requestBody  The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing just the response body of a {@link HttpResponse}.
     */
    public <T> CompletableFuture<T> requestForObject(String path, RequestMethod method, Object requestBody,
                                                     Class<T> responseType, RequestConfig... merging) {
        return request(path, method, requestBody, responseType, merging)
                .thenApply(HttpResponse::getBody);
    }

    /**
     * Calls {@link AsyncRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} passing the parameters
     * and RequestMethod.POST
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody  The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing a {@link HttpResponse} with the response body.
     */
    public <T> CompletableFuture<HttpResponse<T>> post(String path, Object requestBody, Class<T> responseType,
                                                       RequestConfig... merging) {
        return request(path, RequestMethod.POST, requestBody, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} passing the parameters
     * and RequestMethod.PUT
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody  The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing a {@link HttpResponse} with the response body.
     */
    public <T> CompletableFuture<HttpResponse<T>> put(String path, Object requestBody, Class<T> responseType,
                                                      RequestConfig... merging) {
        return request(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} passing the parameters
     * and RequestMethod.GET
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing a {@link HttpResponse} with the response body.
     */
    public <T> CompletableFuture<HttpResponse<T>> get(String path, Class<T> responseType, RequestConfig... merging) {
        return request(path, RequestMethod.GET, null, responseType, merging);
    }

    /**
     * Calls {@link AsyncRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} passing the parameters
     * and RequestMethod.DELETE
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing a {@link HttpResponse} with the response body.
     */
    public <T> CompletableFuture<HttpResponse<T>> delete(String path, Class<T> responseType, RequestConfig... merging) {
        return request(path, RequestMethod.DELETE, null, responseType, merging);
    }


    /**
     * Calls {@link AsyncRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} passing the parameters
     * and RequestMethod.HEAD
     *
     * @param path    The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param merging The configs to overload the request with
     * @return A {@link CompletableFuture} containing a {@link HttpResponse} with no response body and the headers.
     */
    public CompletableFuture<HttpResponse<Void>> head(String path, RequestConfig... merging) {
        return request(path, RequestMethod.HEAD, null, Void.class, merging);
    }

    /**
     * Returns a completable version of {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)} by
     * executing the request on the passed executor.
     *
     * @param path         The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param method       The method to use for this request
     * @param requestBody  The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging      The configs to overload the request with
     * @param <T>          The type of the response
     * @return A {@link CompletableFuture} containing a {@link HttpResponse} with the response body.
     */
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
