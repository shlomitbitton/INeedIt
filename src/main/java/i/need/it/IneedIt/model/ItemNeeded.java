package i.need.it.IneedIt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ItemNeeded {

    private ShoppingCategory shoppingCategory;
    private String itemName;
}
