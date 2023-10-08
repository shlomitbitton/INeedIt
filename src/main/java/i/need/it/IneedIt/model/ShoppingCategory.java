package i.need.it.IneedIt.model;

import i.need.it.IneedIt.enums.ShoppingCategoryName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShoppingCategory {

    private ShoppingCategoryName categoryName;
}
