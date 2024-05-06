package i.need.it.IneedIt.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class NeedingEventResponseDto extends StatusResponseDto {

    private String itemNeededName;
    private String shoppingCategory;
    private long daysListed;
    private String needingEventStatus;
    private long needingEventId;
    private String potentialVendor;
}
