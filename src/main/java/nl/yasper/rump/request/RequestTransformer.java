package nl.yasper.rump.request;

import java.util.Map;

public interface RequestTransformer {

    Object transform(Object data, Map<String, String> headers);

}
