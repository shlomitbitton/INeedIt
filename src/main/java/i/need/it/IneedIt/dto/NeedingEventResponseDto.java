package i.need.it.IneedIt.dto;

import i.need.it.IneedIt.enums.ItemNeeded;
import i.need.it.IneedIt.enums.ShoppingCategory;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class NeedingEventResponseDto {

//    private long needingEventId;
//    private String itemName; //TODO: ItemNeeded name
    private ItemNeeded itemNeeded;
    private ShoppingCategory shoppingCategory;
    private long userId;
    private LocalDate daysListed;
}
