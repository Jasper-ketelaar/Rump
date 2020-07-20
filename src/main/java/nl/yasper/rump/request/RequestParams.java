package nl.yasper.rump.request;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestParams {

    private Map<String, String> requestParams = new HashMap<>();

    public String toURLPart(boolean encodeValues) {
        StringBuilder result = new StringBuilder("?");
        int index = 0;
        Set<String> keySet = requestParams.keySet();
        for (String key : requestParams.keySet()) {
            if (index > 0) {
                result.append("&");
            }
            result.append(key);
            result.append("=");
            result.append(requestParams.get(key));
            index++;
        }

        String resultString = result.toString();
        return encodeValues ? URLEncoder.encode(resultString, StandardCharsets.UTF_8) : resultString;
    }

}
