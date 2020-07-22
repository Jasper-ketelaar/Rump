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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Response transformer implementation that uses Jackson to transform the response object to the
 * requested type.
 */
public class JacksonResponseTransformer implements ResponseTransformer {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public <T> T transform(InputStream from, Class<T> toType) throws IOException {
        return om.readValue(from, toType);
    }
}
