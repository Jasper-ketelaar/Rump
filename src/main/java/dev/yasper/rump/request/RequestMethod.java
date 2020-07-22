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

/**
 * Enum containing all different types of REST methods
 */
public enum RequestMethod {
    GET, POST, PUT, DELETE, PATCH, HEAD;

    /**
     * <p>
     * Transforms the request method to a configurable value usable as a config overload.
     * </p>
     *
     * @return A RequestConfig instance where {@link RequestConfig#getMethod()} will return this {@link RequestMethod}
     */
    public RequestConfig toConfig() {
        return new RequestConfig()
                .setMethod(this);
    }
}