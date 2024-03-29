package i.need.it.IneedIt.model;

import i.need.it.IneedIt.enums.ShoppingCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "item_needed", schema = "ineedit")
public class ItemNeeded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_NEEDED_ID")
    private long itemNeededId;

    @Column(name = "SHOPPING_CATEGORY")
    private ShoppingCategory shoppingCategory;

    @Column(name = "ITEM_NAME")
    private String itemName;
}
