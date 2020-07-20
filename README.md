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

## Usage
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
