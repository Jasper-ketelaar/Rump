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

import java.util.List;
import java.util.Map;

public class HttpResponse<T> {

    private final Headers responseHeaders;
    private final int responseCode;
    private final String responseMessage;
    private final RequestConfig requestConfig;
    private final String url;
    private T body;

    public HttpResponse(T body, Headers responseHeaders, int responseCode, String responseMessage, RequestConfig requestConfig,
                        String url) {
        this.body = body;
        this.responseHeaders = responseHeaders;
        this.responseCode = responseCode;
        this.requestConfig = requestConfig;
        this.url = url;
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getUrl() {
        return url;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public T getBody() {
        return this.body;
    }

    public void setBody(T to) {
        this.body = to;
    }

    public Headers getResponseHeaders() {
        return this.responseHeaders;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
