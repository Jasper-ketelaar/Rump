package dev.yasper.rump.request;

import dev.yasper.rump.config.RequestConfig;

public enum RequestMethod {
    GET, POST, PUT, DELETE, PATCH, HEAD;

    public RequestConfig toConfig() {
        return new RequestConfig()
                .setMethod(this);
    }
}