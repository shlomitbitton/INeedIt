package i.need.it.IneedIt.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class NeedingEventResponseDto {

    private String itemNeededName;
    private String shoppingCategory;
    private long daysListed;
    private String needingEventStatus;
    private long needingEventId;
    private String potentialVendor;
}
