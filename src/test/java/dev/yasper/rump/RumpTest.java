package dev.yasper.rump;

import com.fasterxml.jackson.databind.JsonNode;
import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.model.Post;
import dev.yasper.rump.request.RequestParams;
import dev.yasper.rump.response.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class RumpTest {

    @Test
    public void testGetRequest() {
        try {
            HttpResponse<String> result = Rump.get("https://www.google.nl/", String.class);
            Assert.assertNotNull(result);
            Assert.assertEquals(result.getResponseCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetObject() {
        try {

            HttpResponse<Post> post = Rump.get("https://jsonplaceholder.typicode.com/posts/1", Post.class);
            Post match = new Post()
                    .setId(1)
                    .setUserId(1)
                    .setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
                    .setBody("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
            Assert.assertEquals(match, post.getBody());
            Assert.assertEquals(post.getResponseCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void overrideDefaultConfig() {
        RequestConfig params = new RequestParams()
                .add("test", 1)
                .toConfig();

        RequestConfig headers = new Headers()
                .setAuthentication("Bearer token")
                .setContentType(Headers.ContentType.APPLICATION_JSON)
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
                .toConfig();

        try {
            // Params from default config are now overridden and headers from default config are now merged together
            HttpResponse<Post> res = Rump.get("https://jsonplaceholder.typicode.com/posts/1", Post.class, params, headers);
            Post match = new Post()
                    .setId(1)
                    .setUserId(1)
                    .setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
                    .setBody("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
            Assert.assertEquals(match, res.getBody());
            Assert.assertEquals(res.getResponseCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPost() throws IOException {
        Post match = new Post()
                .setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
                .setBody("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        HttpResponse<JsonNode> postRes = Rump.post("https://jsonplaceholder.typicode.com/posts/", match, JsonNode.class);
        Assert.assertEquals(postRes.getBody().get("id").asInt(), 101);
    }

}
