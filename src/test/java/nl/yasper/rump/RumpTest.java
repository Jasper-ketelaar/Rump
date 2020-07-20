package nl.yasper.rump;

import nl.yasper.rump.model.Post;
import nl.yasper.rump.response.HttpResponse;
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

}
