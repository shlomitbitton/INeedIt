package i.need.it.IneedIt.dto;

import i.need.it.IneedIt.enums.ItemNeeded;
import i.need.it.IneedIt.enums.ShoppingCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NeedingEventRequestDto {

    private ItemNeeded itemNeeded;
    private ShoppingCategory shoppingCategory;

}
