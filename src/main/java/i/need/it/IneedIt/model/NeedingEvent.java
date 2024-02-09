package i.need.it.IneedIt.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
//this entity represent the needing event
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeedingEvent {

    private ItemNeeded itemNeeded;
    private LocalDate dateCreatedEvent;

    private long daysInList; //how long do I have this item in the needing list

    private List<Vendor> purchasingResource;
}
