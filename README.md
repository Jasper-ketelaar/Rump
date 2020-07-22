# Rump
Rump is a REST client for Java that attempts to resemble Axios from JavaScript. 

[![Actions Status](https://github.com/Jasper-ketelaar/Rump/workflows/Verify/badge.svg)](https://github.com/Jasper-ketelaar/Rump/actions)
[![Maven Status](https://maven-badges.herokuapp.com/maven-central/dev.yasper/rump/badge.svg)](https://search.maven.org/artifact/dev.yasper/rump/1.0.5/jar)
## Install with Maven
```xml
<dependency>
  <groupId>dev.yasper</groupId>
  <artifactId>rump</artifactId>
  <version>1.0.5</version>
</dependency>
```

## Why
I was working on a project and none of the currently available REST clients for Java seemed to match my needs. I am a pretty big fan of Axios for JavaScript
so I decided to make a version of that for Java that would fit my needs.

## Features
- Create rest requests synchronously
- Create rest requests asynchronously
- Custom response mapper support by modifying request config (By default Rump uses Jackson)
- Customer request body mapper support (By default Rump uses Jackson)
- Request transformation before sending it out
- Intercept requests/responses to modify them
- Canceling of requests by using a request interceptor
- Canceling of responses by using a response interceptor
- Creation of instances is fast and easy due to chaining of calls, and the ability to have multiple configs per request

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

## Request Configuration
Requests are configured using the `RequestConfig` class. By default request config supports most
configuratble options for a `HttpURLConnection` however any option not supported by this
class can be modified using the `connectionConsumer` in `RequestConfig`. I can also add more options
to the request configuration, just create an issue.

## HttpResponse
The HttpResponse contains the following values:
- The response code
- The response message
- The config used for the request that generated this response
- The headers that the server responded with
- The response body mapped as the type you requested

The HttpResponse object will also be returned in case of an error, however
the response body will not be mapped to your requested type but will be kept as
a string.

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

### Overriding config defaults
With an instance created by Rump you can set the defaults yourself by the RequestConfig
instance you passed. If you want to modify the defaults used by the backing instance for the static
methods inside `Rump.java` then you can modify the constant `Rump.DEFAULT_CONFIG`. If you want
to change the default base url for all static methods for example:
```java 
Rump.DEFAULT_CONFIG.setBaseURL("https://www.new-default.org");
```

## Mappers
Rump supports configurable object mapping, but by default it uses Jackson. You can map request bodies
using the `RequestTransformer` interface and overriding the config value. You can map response
bodies by using the `ResponseTransformer` interface and overriding the config value. You can look at
`JacksonRequestTransformer` and `JacksonResponseTransformer` for examples.

## Interceptors
Interceptors can be used to intercept requests before they are fired and 
intercept responses before they are returned. For the request this can be used 
to modify the config or cancel the request completely. For the response these can be used
to add a default error handler or a default response data transformation.

### Request interceptor
A request interceptor could look like this:
```java
RequestConfig config = new RequestConfig()
        .addRequestInterceptor((mergedURL, connection, config1) -> {
            if (mergedURL.startsWith("https://www.mydomain.com")) {
                connection.setRequestProperty("Authorization", "Bearer myDomainToken");
            }
            return true;
        });
DefaultRestClient drs = Rump.createDefault(config);
```

Here we check if the domain we are sending a request to matches `https://www.mydomain.com` and if 
so we will append our authorization token for this domain to the header of that specific request.
Please note that Rump applies the config values before calling the request interceptor
so modifying the config values will have no effect on the request. Unless you added
a response specific config value such as adding a response interceptor at this stage. 

### Response interceptor
A response interceptor could look like this:
```java
RequestConfig config = new RequestConfig()
        .setBaseURL("https://jsonplaceholder.typicode.com/")
        .addResponseInterceptor(res -> {
            Object body = res.getBody();
            if (body instanceof Post) {
                Post post = (Post) body;
                post.setTitle(String.format("[INTERCEPTED] %s", post.getTitle()));
            }
            return true;
        });

DefaultRestClient drs = Rump.createDefault(config);
```

Now whenever the response body is of type Post the interceptor will activate and 
modify the post's title appending [INTERCEPTED]. A sample request could be like this:
```java
HttpResponse<Post> res = drs.get("posts/1", Post.class);
System.out.println(res.getBody().getTitle());
```
Output:
```
[INTERCEPTED] sunt aut facere repellat provident occaecati excepturi optio reprehenderit
```

## Error handling
Response status errors (any response status code above 299) are thrown as `HttpStatusCodeException`. 
These are thrown as completion errors within the async functionality of Rump. You are able to modify
what status codes are considered as errors by adjusting `ignoreStatusCode` in `RequestConfig`. For example:
```java
RequestConfig config = new RequestConfig()
        .setIgnoreStatusCode((code) -> code < 400);
```
Config now doesn't throw an error if the status code is between 300 and 399.

The `HttpStatusCodeException` contains a `HttpResponse<String>` where the error body is of string value.
You can therefore easily inspect values of the error response the same way you would
check values of a successful response.

`RequestConfig` also has a value `exceptionHandler` that can be modified to change the default
exception behaviour. `DefaultExceptionHandler` is the default exception handler that simply
thros the `HttpStatusCodeException` that it is passed.
