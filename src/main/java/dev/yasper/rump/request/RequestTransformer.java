package dev.yasper.rump.request;

import dev.yasper.rump.Headers;

import java.util.Map;

public interface RequestTransformer {

    Object transform(Object data, Headers headers);

}
