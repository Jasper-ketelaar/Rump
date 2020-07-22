package dev.yasper.rump.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yasper.rump.Headers;

public class JacksonRequestTransformer implements RequestTransformer {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public Object transform(Object data, Headers headers) {
        try {
            return om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
