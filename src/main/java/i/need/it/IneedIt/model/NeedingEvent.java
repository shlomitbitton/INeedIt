package i.need.it.IneedIt.model;

import i.need.it.IneedIt.enums.NeedingEventStatus;
import i.need.it.IneedIt.enums.ShoppingCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

//this entity represent the needing event which includes the item product
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


    @Column(name = "ITEM_NEEDED")
    @NotNull
    private String itemNeeded;


    @Column(name = "SHOPPING_CATEGORY")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ShoppingCategory shoppingCategory;

    @Column(name = "NEEDING_EVENT_DATE_CREATED")
    @NotNull
    private LocalDate needingEventDateCreated;

    @Column(name = "NEEDING_EVENT_STATUS")
    @Enumerated(EnumType.STRING)
    @NotNull
    private NeedingEventStatus needingEventStatus;

    @Column(name = "NEED_NOTES")
    private String needNotes;

//    @Column(name = "DAYS_LISTED" )
//    private long daysListed; //how long do I have this item in the needing list

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_ID")
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @NotNull
    private User user;


}
