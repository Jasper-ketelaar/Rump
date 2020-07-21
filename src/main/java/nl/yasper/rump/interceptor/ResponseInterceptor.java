package nl.yasper.rump.interceptor;

import nl.yasper.rump.response.HttpResponse;

public interface ResponseInterceptor {

    boolean beforeResponse(HttpResponse<?> res);

}
