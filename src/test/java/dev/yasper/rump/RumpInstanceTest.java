package dev.yasper.rump;

import dev.yasper.rump.client.DefaultRestClient;
import dev.yasper.rump.config.RequestConfig;
import dev.yasper.rump.model.Post;
import dev.yasper.rump.response.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class RumpInstanceTest {

    private final DefaultRestClient drs;

    public RumpInstanceTest() {
        RequestConfig config = new RequestConfig()
                .setIgnoreStatusCode((code) -> code >= 400)
                .setBaseURL("https://jsonplaceholder.typicode.com/")
                .addRequestInterceptor((mergedURL, connection, config1) -> {
                    if (mergedURL.startsWith("https://www.mydomain.com")) {
                        connection.setRequestProperty("Authorization", "Bearer myDomainToken");
                    }
                    return true;
                });
        drs = Rump.createDefault(config);
    }

    @Test
    public void testOtherBase() throws IOException {
        HttpResponse<String> google = drs.get("https://www.google.nl/", String.class,
                new RequestConfig()
                        .setBaseURL("")
        );
        Assert.assertEquals(google.getResponseCode(), 200);
    }

    @Test
    public void testGetObject() {
        try {
            HttpResponse<Post> post = drs.get("posts/1", Post.class);
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
}
