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

import dev.yasper.rump.response.HttpResponse;

public interface ResponseInterceptor {

    /**
     * Method executed before the response is returned, can be used to change response data where necessary.
     * @param res The {@link HttpResponse} which will be returned
     * @return {@code true} iff this response should be forwarded, {@code false} if null should be returned.
     */
    boolean beforeResponse(HttpResponse<?> res);

}
