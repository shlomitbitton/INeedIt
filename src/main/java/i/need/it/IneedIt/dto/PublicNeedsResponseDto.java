package i.need.it.IneedIt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PublicNeedsResponseDto {

    private Long needingEventId;
    private String itemNeededName;
}
