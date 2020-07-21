package dev.yasper.rump.interceptor;

import dev.yasper.rump.response.HttpResponse;

public interface ResponseInterceptor {

    boolean beforeResponse(HttpResponse<?> res);

}
