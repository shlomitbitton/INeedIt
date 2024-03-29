package i.need.it.IneedIt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//this entity represent the needing event
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "needing_event", schema = "ineedit")
public class NeedingEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NEEDING_EVENT_ID")
    private long needingEventId;

//    @Column(name = "ITEM_NEEDED")
//    private ItemNeeded itemNeeded;

    @Column(name = "NEEDING_EVENT_DATE")
    private LocalDate needingEventDateCreated;

    @Column(name = "DAYS_LISTED" )
    private long daysListed; //how long do I have this item in the needing list

    @OneToMany(mappedBy = "needingEvent", cascade = CascadeType.ALL, fetch = FetchType.EAGER) //one item can be purchased from many vendors
    private List<Vendor> purchasingResource = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}
