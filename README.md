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