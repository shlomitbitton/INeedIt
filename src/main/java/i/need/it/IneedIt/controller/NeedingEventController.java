package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;


@RestController
public class NeedingEventController {


    public EntityResponse<NeedingEventResponseDto> crateNeedingEvent(@RequestParam NeedingEventRequestDto needingEvent){
        return (EntityResponse<NeedingEventResponseDto>) new NeedingEventResponseDto();
    }
}
