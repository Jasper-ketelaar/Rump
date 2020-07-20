package nl.yasper.rump.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JacksonResponseTransformer implements ResponseTransformer {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public <T> T transform(InputStream from, Class<T> toType) throws IOException {
        return om.readValue(from, toType);
    }
}
