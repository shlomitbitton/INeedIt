package i.need.it.IneedIt.model;

import i.need.it.IneedIt.enums.NeedingEventStatus;
import i.need.it.IneedIt.enums.ShoppingCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column(name = "PUBLIC_NEED")
    private int publicNeed;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_ID")
    private Vendor vendor;


    @OneToMany(mappedBy = "need")
    private List<UserNeeds> userNeeds;//each userNeed record is showing the relationship between a need and the users.


}
