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

    /*
    create new needing event
     */

    @PostMapping(value="/createNewNeedingEvent")
    public @ResponseBody ResponseEntity<NeedingEventResponseDto> crateNeedingEvent(@RequestBody NeedingEventRequestDto needingEvent){
        return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);
    }
    /*
        This endpoint present all the needs of a user
    */
    @GetMapping(value="/allNeeds/{userId}")
    public List<NeedingEventResponseDto> getAllNeedingEvent(@RequestParam ("userId") Long userId){
        return new ArrayList<>();
    }

    /*
    Present a single needing event by event id
     */
    @GetMapping(value="/needingEvent/{needingEventId}")
    public @ResponseBody ResponseEntity<NeedingEventResponseDto> fulfilledNeedingEvent(@RequestParam ("needingEventId") Long needingEventId){
        return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);
    }


}
