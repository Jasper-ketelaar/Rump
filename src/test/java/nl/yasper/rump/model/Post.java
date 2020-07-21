package nl.yasper.rump.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Getter
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
