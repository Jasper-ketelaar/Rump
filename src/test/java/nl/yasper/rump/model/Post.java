package nl.yasper.rump.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Post {

    @JsonProperty
    int userId;

    @JsonProperty
    int id;

    @JsonProperty
    String title;

    @JsonProperty
    String body;

}
