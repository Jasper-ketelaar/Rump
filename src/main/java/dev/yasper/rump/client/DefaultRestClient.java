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

import dev.yasper.rump.Headers;
import dev.yasper.rump.Rump;
import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.exception.HttpStatusCodeException;
import dev.yasper.rump.interceptor.RequestInterceptor;
import dev.yasper.rump.interceptor.ResponseInterceptor;
import dev.yasper.rump.request.RequestMethod;
import dev.yasper.rump.response.HttpResponse;
import dev.yasper.rump.response.PrimitiveBody;
import dev.yasper.rump.response.ResponseTransformer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DefaultRestClient implements RestClient {

    private static final List<Class<?>> PRIMITIVE_CLASSES = Arrays.asList(
            String.class, Double.class, Integer.class, Float.class,
            Short.class, Byte.class, Boolean.class, Character.class
    );
    private static final int LAST_SUCCESSFUL_RESPONSE = 299;
    private final RequestConfig config;

    protected DefaultRestClient(RequestConfig config) {
        this.config = config;
    }

    /**
     * Create a DefaultRestClient, constructor is protected to prevent a user creating a RequestConfig with a null
     * value causing unexpected errors during execution.
     *
     * @param config The config instance for this rest client, this is merged with {@link Rump#DEFAULT_CONFIG}
     * @return The created {@link DefaultRestClient}
     */
    public static DefaultRestClient create(RequestConfig config) {
        return new DefaultRestClient(Rump.DEFAULT_CONFIG.merge(config));
    }

    public RequestConfig getConfig() {
        return config;
    }

    /**
     * Calls {@link DefaultRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.GET
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link T} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> T getForObject(String path, Class<T> responseType,
                              RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.GET, null, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.POST
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link T} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> T postForObject(String path, Object requestBody, Class<T> responseType,
                               RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.POST, requestBody, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.PUT
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link T} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> T putForObject(String path, Object requestBody, Class<T> responseType,
                              RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#requestForObject(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.DELETE
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link T} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> T deleteForObject(String path, Class<T> responseType, RequestConfig... merging) throws IOException {
        return requestForObject(path, RequestMethod.DELETE, null, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and returns just the body
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param method The request method for this request.
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link HttpResponse} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> T requestForObject(String path, RequestMethod method, Object requestBody, Class<T> responseType,
                                  RequestConfig... merging) throws IOException {
        return request(path, method, requestBody, responseType, merging).getBody();
    }

    /**
     * Calls {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.POST
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link HttpResponse} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> HttpResponse<T> post(String path, Object requestBody, Class<T> responseType,
                                    RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.POST, requestBody, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.PUT
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link HttpResponse} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> HttpResponse<T> put(String path, Object requestBody, Class<T> responseType,
                                   RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.PUT, requestBody, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.GET
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link HttpResponse} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> HttpResponse<T> get(String path, Class<T> responseType,
                                   RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.GET, null, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.DELETE
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @return The {@link HttpResponse} if everything is fine, else null
     * @param <T> The required type of the response
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> HttpResponse<T> delete(String path, Class<T> responseType, RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.DELETE, null, responseType, merging);
    }

    /**
     * Calls {@link DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * with the passed parameters and RequestMethod.HEAD
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param merging The configs from which to construct this request
     * @return The {@link HttpResponse} if everything is fine, else null
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public HttpResponse<Void> head(String path, RequestConfig... merging) throws IOException {
        return request(path, RequestMethod.HEAD, null, Void.class, merging);
    }

    /**
     * Calls {@link DefaultRestClient#request(String, Object, Class, RequestConfig)} with the passed parameters
     * and the configs merged, method is converted into a config too.
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param method The request method for this request.
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @param <T> The required type of the response
     * @return The {@link HttpResponse} if everything is fine, else null
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> HttpResponse<T> request(String path, RequestMethod method, Object requestBody,
                                       Class<T> responseType, RequestConfig... merging) throws IOException {
        return request(path, requestBody, responseType, this.config.merge(method.toConfig()).merge(merging));
    }

    /**
     * Calls {@link DefaultRestClient#request(String, Object, Class, RequestConfig)} with the passed parameters
     * and the configs merged.
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param merging The configs from which to construct this request
     * @param <T> The required type of the response
     * @return The {@link HttpResponse} if everything is fine, else null
     * @throws IOException Thrown by HttpURLConnection methods
     */
    public <T> HttpResponse<T> request(String path, Object requestBody, Class<T> responseType,
                                       RequestConfig... merging) throws IOException {
        RequestConfig config = this.config.merge(merging);
        return request(path, requestBody, responseType, config);
    }


    /**
     * <p>
     * Main method for creating any request. Converts the config into a url using {@link RequestConfig#getBaseURL()} and
     * {@link RequestConfig#getParams()}. This url is then constructed into a HttpURLConnection using the Proxy defined in the
     * config if it is present.
     * </p>
     *
     * <p>
     * On this instance the rest of the config values are applied and the RequestInterceptors are called. If any of them
     * return false the request is canceled.
     * </p>
     *
     * <p>
     * The request body is written to the connections output stream. Now the request is completed and the repsonse parsing
     * starts.
     * </p>
     *
     * <p>
     *     The response code is then checked for it to be erroneous using {@link RequestConfig#getIgnoreStatusCode()}
     *     and if an error occurred a {@link HttpResponse} is constructed and sent to the error handler.
     * </p>
     *
     * <p>
     *     If no error has occurred the connection input stream is read and parsed to the requested type using the
     *     response transformer. This is when the ResponseInterceptors are called and if any of them return false the
     *     response is canceled. If everything passes we then return the {@link HttpResponse} with the response body.
     * </p>
     *
     * @param path The path of this request, full URL if no base URL is specified in the config or any of the overloads
     * @param requestBody The body to send with this request, applicable to POST and PUT only
     * @param responseType The type to parse the response as
     * @param config The config specifying the request
     * @param <T> The required type of the response
     * @return The {@link HttpResponse} if everything is fine, else null
     * @throws IOException Thrown by HttpURLConnection methods
     */
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
            PrimitiveBody body = new PrimitiveBody(connection.getErrorStream());
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

        if (responseType == PrimitiveBody.class || PRIMITIVE_CLASSES.contains(responseType)) {
            PrimitiveBody body = new PrimitiveBody(input);
            if (responseType == PrimitiveBody.class) {
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
