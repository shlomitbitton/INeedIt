package i.need.it.IneedIt.dto;

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

    private String itemNeeded;
    private ShoppingCategory shoppingCategory;
    private long userId;
    private String vendorName;

}
