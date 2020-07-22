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
package dev.yasper.rump.response;

import dev.yasper.rump.Headers;
import dev.yasper.rump.config.RequestConfig;

/**
 * Class encapsulating all relevant values to a http response
 * @param <T> The response body type
 */
public class HttpResponse<T> {

    private final Headers responseHeaders;
    private final int responseCode;
    private final String responseMessage;
    private final RequestConfig requestConfig;
    private final String url;
    private T body;

    /**
     * Constructor for HttpResponse
     * @param body body of type T
     * @param responseHeaders headers in the response
     * @param responseCode the response code
     * @param responseMessage the message of the response
     * @param requestConfig the request configuration with which this response was obtained
     * @param url the url that was requested (including base and params)
     */
    public HttpResponse(T body, Headers responseHeaders, int responseCode, String responseMessage, RequestConfig requestConfig,
                        String url) {
        this.body = body;
        this.responseHeaders = responseHeaders;
        this.responseCode = responseCode;
        this.requestConfig = requestConfig;
        this.url = url;
        this.responseMessage = responseMessage;
    }

    /**
     * Gets the response message
     * @return the response message
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Gets the requested url
     * @return the request url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the request config used to get this response
     * @return the request config
     */
    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    /**
     * Gets the response body as T
     * @return the response body
     */
    public T getBody() {
        return this.body;
    }

    /**
     * Used to transform the body in response interceptors
     * @param to the new body to set it to
     */
    public void setBody(T to) {
        this.body = to;
    }

    /**
     * Gets the response headers
     * @return the response headers
     */
    public Headers getResponseHeaders() {
        return this.responseHeaders;
    }

    /**
     * Gets the response code
     * @return the response code
     */
    public int getResponseCode() {
        return this.responseCode;
    }
}
