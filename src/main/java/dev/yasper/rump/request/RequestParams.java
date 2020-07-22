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
package dev.yasper.rump.request;

import dev.yasper.rump.config.RequestConfig;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * <p>
 * Class that contains request params. Can be constructed to a url part by calling {@link RequestParams#toURLPart()}.
 * Can otherwise also be provided via {@link RequestConfig#setParams(RequestParams)}.
 * </p>
 */
public class RequestParams {

    private final Map<String, Supplier<String>> requestParams = new HashMap<>();
    private boolean encoded = true;
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * Converts this RequestParams instance to URL part to be added at the end of a URL.
     * Uses {@link RequestParams#charset} and {@link RequestParams#encoded} to determine
     * how the URL should be constructed.
     *
     * @return The part of the URL to be appended at the end
     */
    public String toURLPart() {
        if (requestParams.size() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder("?");
        int index = 0;
        Set<String> keySet = requestParams.keySet();
        for (String key : keySet) {
            if (index > 0) {
                result.append("&");
            }

            result.append(key);
            result.append("=");

            String value = requestParams.get(key).get();
            result.append(isEncoded() ? URLEncoder.encode(value, getCharset()) : requestParams.get(key));
            index++;
        }

        return result.toString();
    }

    /**
     * Add a string request param
     *
     * @param key   The param's key
     * @param value The param's value
     * @return this {@link RequestParams} instance to be used for setter chaining
     */
    public RequestParams add(String key, String value) {
        return add(key, () -> value);
    }

    /**
     * Add an object request param, the object is converted to string by calling toString();
     *
     * @param key   The param's key
     * @param value The param's value
     * @return this {@link RequestParams} instance to be used for setter chaining
     */
    public RequestParams add(String key, Object value) {
        return add(key, value::toString);
    }

    /**
     * Add an supplier request param which will be evaluated upon constructing the url.
     *
     * @param key          The param's key
     * @param dynamicValue The param's value supplier
     * @return this {@link RequestParams} instance to be used for setter chaining
     */
    public RequestParams add(String key, Supplier<String> dynamicValue) {
        this.requestParams.put(key, dynamicValue);
        return this;
    }

    /**
     * True if the params should be encoded using {@link URLEncoder}
     *
     * @return {@link RequestParams#encoded}
     */
    public boolean isEncoded() {
        return this.encoded;
    }

    /**
     * @param encoded New value for {@link RequestParams#encoded}
     * @return this {@link RequestParams} instance to be used for setter chaining
     */
    public RequestParams setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    /**
     * The charset that should be passed to {@link URLEncoder#encode(String, String)} if the params
     * are encoded.
     *
     * @return {@link RequestParams#charset}
     */
    public Charset getCharset() {
        return this.charset;
    }

    /**
     * @param charset New value for {@link RequestParams#charset}
     * @return this {@link RequestParams} instance to be used for setter chaining
     */
    public RequestParams setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Converts this RequestParams instance to a RequestConfig instance for request overloading.
     * See {@link dev.yasper.rump.client.DefaultRestClient#request(String, RequestMethod, Object, Class, RequestConfig...)}
     * for more information.
     *
     * @return The RequestConfig instance
     */
    public RequestConfig toConfig() {
        return new RequestConfig()
                .setParams(this);
    }
}
