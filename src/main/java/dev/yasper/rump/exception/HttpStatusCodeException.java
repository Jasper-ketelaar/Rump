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
package dev.yasper.rump.exception;

import dev.yasper.rump.response.HttpResponse;

/**
 * Class describing the exception thrown on an erroneous status code
 * (any value above 299 is considered erroneous by the HTTP specification)
 */
public class HttpStatusCodeException extends RuntimeException {

    private final HttpResponse<String> errorResponse;

    /**
     * Constructor for this exception
     * @param errorResponse An HttpResponse containing the {@link java.net.HttpURLConnection} data and the error body
     */
    public HttpStatusCodeException(HttpResponse<String> errorResponse) {
        super(errorResponse.getResponseMessage());
        this.errorResponse = errorResponse;
    }

    /**
     * Get the response for this exception
     * @return the {@link HttpResponse} containing the error
     */
    public HttpResponse<String> getErrorResponse() {
        return errorResponse;
    }
}
