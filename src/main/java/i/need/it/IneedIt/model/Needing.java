package i.need.it.IneedIt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Needing {

    private ShoppingCategory shoppingCategory;
    private String itemName;
    private LocalDate dateCreated;

    private long daysInList; //how long do I have this item in the needing list

    private List<String> purchasingResource;
}
