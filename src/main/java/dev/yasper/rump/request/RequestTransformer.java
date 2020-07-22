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

import dev.yasper.rump.Headers;

/**
 * Transformer interface for request object.
 */
public interface RequestTransformer {

    /**
     * Transform a request body object to the object that should be passed in the request.
     * @param data The request body that is to be transformed
     * @param headers The headers that could be used to transform the object
     * @return The transformed object
     */
    Object transform(Object data, Headers headers);

}
