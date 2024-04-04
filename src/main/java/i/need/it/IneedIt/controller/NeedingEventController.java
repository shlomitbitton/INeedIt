package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import i.need.it.IneedIt.dto.VendorRequestDto;
import i.need.it.IneedIt.service.NeedingEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class NeedingEventController {

    private final NeedingEventService needingEventService;

    public NeedingEventController(NeedingEventService needingEventService) {
        this.needingEventService = needingEventService;
    }



    /*
    create new needing event
     */

    @PostMapping(value="/createNewNeedingEvent")
    public @ResponseBody NeedingEventResponseDto crateNeedingEvent(@RequestBody NeedingEventRequestDto needingEventDto){
        log.info("User creating a new needing event");
        return needingEventService.createNewNeedingEvent(needingEventDto);
    }
    /*
        This endpoint present all the needs of a user
    */
    @GetMapping(value="/allUserNeeds")
    public List<String> getAllUserNeedingEvent(@RequestParam(value = "userId") String userId){
        return needingEventService.getUserNeedingEvents(userId);
    }

    @GetMapping(value="/allNeedingEvents")
    public List<String> getAllNeedingEvent(){
        return needingEventService.getAllNeedingEventsResponseDto();
    }

    /*
    Present a single needing event by event id
     */
    @GetMapping(value="/needingEvent")
    public @ResponseBody ResponseEntity<NeedingEventResponseDto> fulfilledNeedingEvent(@RequestParam(value = "needingEventId") String needingEventId){
        return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);
    }
    @PostMapping(value = "/createNewVendor")
    public ResponseEntity<HttpStatus> createNewVendor(@RequestBody VendorRequestDto vendorRequestDto){
        return needingEventService.createNewVendor(vendorRequestDto);
    }



}
