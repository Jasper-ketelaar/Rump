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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Used for parsing primitive body types that won't be properly accepted using a json parser.
 */
public class PrimitiveBody {

    private final InputStream responseStream;
    private final String parsed;

    /**
     * Construct a primitive body
     * @param responseStream the response stream to parse from
     */
    public PrimitiveBody(InputStream responseStream) {
        this.responseStream = responseStream;
        this.parsed = parse();
    }

    /**
     * Get the body as a buffered reader
     * @return a {@link BufferedReader} instsance
     */
    public BufferedReader getAsReader() {
        return new BufferedReader(new InputStreamReader(responseStream));
    }

    /**
     * Get the body as a string
     * @param includeLines include line breaks
     * @return the string as requested
     */
    public String getAsString(boolean includeLines) {
        if (includeLines) {
            return getAsString();
        }

        return getAsString().replaceAll("\n", "");
    }

    /**
     * @return body as String
     */
    public String getAsString() {
        return parsed;
    }

    /**
     * @return body as int
     */
    public int getAsInt() {
        return Integer.parseInt(getAsString());
    }

    /**
     * @return body as double
     */
    public double getAsDouble() {
        return Double.parseDouble(getAsString());
    }

    /**
     * @return body as short
     */
    public short getAsShort() {
        return Short.parseShort(getAsString());
    }

    /**
     * @return body as byte
     */
    public byte getAsByte() {
        return Byte.parseByte(getAsString());
    }

    /**
     * @return body as long
     */
    public long getAsLong() {
        return Long.parseLong(getAsString());
    }

    /**
     * @return body as float
     */
    public float getAsFloat() {
        return Float.parseFloat(getAsString());
    }

    /**
     * @return body as boolean
     */
    public boolean getAsBoolean() {
        return Boolean.parseBoolean(getAsString());
    }

    /**
     * @return body as char
     */
    public char getAsCharacter() {
        String str = getAsString();
        if (str.length() == 0) {
            return (char) 0;
        }

        return str.charAt(0);
    }

    /**
     * Generic primitive get as method
     * @param type the primitive box type's class
     * @param <T> the returned value type
     * @return the value as type T
     */
    public <T> T getAs(Class<T> type) {
        String asString = getAsString();
        if (type == String.class) {
            return type.cast(asString);
        } else if (type == Integer.class) {
            return type.cast(getAsInt());
        } else if (type == Double.class) {
            return type.cast(getAsDouble());
        } else if (type == Short.class) {
            return type.cast(getAsShort());
        } else if (type == Long.class) {
            return type.cast(getAsLong());
        } else if (type == Float.class) {
            return type.cast(getAsFloat());
        } else if (type == Byte.class) {
            return type.cast(getAsByte());
        } else if (type == Boolean.class) {
            return type.cast(getAsBoolean());
        } else {
            throw new IllegalStateException("This state should not be reachable");
        }
    }

    private String parse() {
        try {
            try (BufferedReader reader = this.getAsReader()) {
                String line;
                StringBuilder resultB = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    resultB.append(line);
                    resultB.append("\n");
                }

                return resultB.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
