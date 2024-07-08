package i.need.it.IneedIt.model;

import i.need.it.IneedIt.model.embeddable.UserNeedsId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_needs", schema = "ineedit")
public class UserNeeds {

    @EmbeddedId
    private UserNeedsId userNeedsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("needId")
    @JoinColumn(name = "need_id")
    private NeedingEvent need;

}
