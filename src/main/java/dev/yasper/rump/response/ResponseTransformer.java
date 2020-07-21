package dev.yasper.rump.response;

import java.io.IOException;
import java.io.InputStream;

public interface ResponseTransformer {

    <T> T transform(InputStream from, Class<T> toType) throws IOException;

}
