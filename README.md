# Rump

Rump is a REST client for Java that attempts to resemble Axios from JavaScript. 

## Why
I was working on a project and none of the currently available REST clients for Java seemed to match my needs. I am a pretty big fan of Axios for JavaScript
so I decided to make a version of that for Java that would fit my needs.

## Features
- Create rest requests synchronously
- Create rest requests asynchronously
- Custom response mapper support by modifying request config (By default Rump uses Jackson)
- Request transformation before sending it out

## TODO
- Interceptors
- Custom error handling support
- Canceling requests
- Request params

## Basic Usage
There's three different ways of making REST api calls with Rump:

### 1. Via the Rump class you can make requests directly.

a) Synchronous
```java
try {
    HttpResponse<Post> res = Rump.get("https://jsonplaceholder.typicode.com/posts/1", Post.class);
    Post post = res.getBody();
    // Do something with post
} catch (IOException e) {
    e.printStackTrace();
}
```

b) Asynchronous
```java
Rump.getAsync("https://jsonplaceholder.typicode.com/posts/1", Post.class).thenAccept(res -> {
    Post post = res.getBody();
    // Do something with post
});

```

### 2. Synchronously using a DefaultRestClient instance
```java
RequestConfig config = new RequestConfig()
                .setBaseURL("https://jsonplaceholder.typicode.com/");
DefaultRestClient drs = Rump.createDefault(config);

try {
    HttpResponse<Post> res = drs.get("posts/1", Post.class);
    Post post = res.getBody();
    // Do something with post
} catch (IOException e) {
    e.printStackTrace();
}
```

### 3. Asynchronously using an AsyncRestClient instance
```java
RequestConfig config = new RequestConfig()
                .setBaseURL("https://jsonplaceholder.typicode.com/");
AsyncRestClient ars = Rump.createAsync(config, Executors.newFixedThreadPool(1));
ars.get("posts/1", Post.class).thenAccept(res -> {
    Post post = res.getBody();
    // Do something with post
});
```

## Overriding config values
Sometimes your default config values, whether they come from a constructed instance or
simply the default values supplied by Rump, are not enough for a specific request.

This is where config overriding comes in. Rump supports config overriding
by appending any number of configs to a request. A call could look like this:
` Rump.get("https://jsonplaceholder.typicode.com/posts/1", Post.class, config1, config2, config3);`.
In this example the precedence taken is whatever config supplies the value last.
For example, if the default config, config1, config2 and config3 all have a different value set for reqeust params
then the values of config3 will be used in the request.

To make it easier to construct config instances based of certain configurable request
values, Rump gives you the option to convert a certain configurable instance such as 
RequestHeaders to a RequestConfig instance. An example could look like this:
```java
RequestConfig params = new RequestParams()
                .add("test", 1)
                .toConfig();

RequestConfig headers = new RequestHeaders()
        .setAuthentication("Bearer token")
        .setContentType(RequestHeaders.ContentType.JSON)
        .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
        .toConfig();

try {
    // Params from default config are now overridden and headers from default config are now merged together
    HttpResponse<Post> res = Rump.get("https://jsonplaceholder.typicode.com/posts/1", Post.class, params, headers);
    Post post = res.getBody();  
    // Do something with post
} catch (IOException e) {
    e.printStackTrace();
}
```