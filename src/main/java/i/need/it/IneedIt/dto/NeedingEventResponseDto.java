package i.need.it.IneedIt.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class NeedingEventResponseDto {

    private long needingEventId;
    private String itemName; //TODO: ItemNeeded name
}
