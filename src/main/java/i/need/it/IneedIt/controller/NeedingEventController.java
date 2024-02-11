package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class NeedingEventController {


    @PostMapping(value="/newNeedingEvent")
    public @ResponseBody ResponseEntity<NeedingEventResponseDto> crateNeedingEvent(@RequestBody NeedingEventRequestDto needingEvent){
        return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);
    }

    @GetMapping(value="/allNeeds")
    public List<NeedingEventResponseDto> getAllNeedingEvent(@RequestParam NeedingEventRequestDto needingEvent){
        return new ArrayList<>();
    }

    @PostMapping(value="/fulfilledNeedingEvent/{needingEventId}")
    public @ResponseBody ResponseEntity<NeedingEventResponseDto> fulfilledNeedingEvent(@RequestParam Long needingEventId){
        return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);
    }

}
