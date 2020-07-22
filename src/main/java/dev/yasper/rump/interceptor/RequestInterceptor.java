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
package dev.yasper.rump.interceptor;

import dev.yasper.rump.config.RequestConfig;

import java.net.HttpURLConnection;

/**
 * Interface for intercepting requests and modifying the connection where necessary.
 */
public interface RequestInterceptor {

    /**
     * Method executed before the request is made.
     * @param mergedURL The complete URL for this request (includes the base, the path and the params)
     * @param connection The connection which is being constructed for this request, you can modify properties on this
     *                   object
     * @param config The request configuration from which the request was constructed, these properties are modifiable
     *               but won't have an impact on the outstanding connection as they won't be reapplied
     * @return {@code true} iff the request should be made, {@code false} if it should be canceled.
     */
    boolean beforeRequest(String mergedURL, HttpURLConnection connection, RequestConfig config);
}
