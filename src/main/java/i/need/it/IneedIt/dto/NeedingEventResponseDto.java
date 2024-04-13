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
    private LocalDate daysListed;
    private String needingEventStatus;
}
