package i.need.it.IneedIt.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserNeedsId implements Serializable {

    private long userId;
    private long needId;
}
