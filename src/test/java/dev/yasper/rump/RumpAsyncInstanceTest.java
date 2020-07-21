package dev.yasper.rump;

import dev.yasper.rump.client.AsyncRestClient;
import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.model.Post;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;

public class RumpAsyncInstanceTest {

    private final AsyncRestClient ars;

    public RumpAsyncInstanceTest() {
        RequestConfig config = new RequestConfig()
                .setBaseURL("https://jsonplaceholder.typicode.com/");
        ars = Rump.createAsync(config, Executors.newFixedThreadPool(1));
    }

    @Test
    public void testGetObject() {
        ars.get("posts/1", Post.class).thenAccept(post -> {
            Post match = new Post()
                    .setId(1)
                    .setUserId(1)
                    .setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
                    .setBody("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
            Assert.assertEquals(match, post.getBody());
            Assert.assertEquals(post.getResponseCode(), 200);
        });
    }
}
