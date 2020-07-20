package nl.yasper.rump.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResponseBody {

    private final InputStream responseStream;
    private final String parsed;

    public ResponseBody(InputStream responseStream) {
        this.responseStream = responseStream;
        this.parsed = parse();
    }

    public BufferedReader getAsReader() {
        return new BufferedReader(new InputStreamReader(responseStream));
    }

    public String getAsString() {
        return parsed;
    }

    public String parse(boolean includeLines) {
        if (includeLines) {
            return getAsString();
        }

        return getAsString().replaceAll("\n", "");
    }

    public int getAsInt() {
        return Integer.parseInt(getAsString());
    }

    public double getAsDouble() {
        return Double.parseDouble(getAsString());
    }

    public <T> T getAs(Class<T> type) throws IOException {
        String asString = getAsString();
        if (type == String.class) {
            return type.cast(asString);
        } else if (type == Integer.class) {
            return type.cast(getAsInt());
        } else if (type == Double.class) {
            return type.cast(getAsDouble());
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(asString, type);
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
