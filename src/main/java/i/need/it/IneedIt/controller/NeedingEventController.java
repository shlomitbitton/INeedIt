package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import i.need.it.IneedIt.dto.VendorRequestDto;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.service.NeedingEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class NeedingEventController {

    private final NeedingEventService needingEventService;

    public NeedingEventController(NeedingEventService needingEventService) {
        this.needingEventService = needingEventService;
    }



    /*
    create new needing event
     */

    @PostMapping(value="/addUpdateNeedingEvent")
    public @ResponseBody NeedingEventResponseDto addUpdateNeedingEvent(@RequestBody NeedingEventRequestDto needingEventDto){
        log.info("User creating a new needing event");
        return needingEventService.createNewNeedingEvent(needingEventDto);
    }
    /*
        This endpoint present all the needs of a user
    */
    @GetMapping(value="/allNeedsByUser")
    public List<NeedingEventResponseDto> getAllUserNeedingEvent(@RequestParam(value = "userId") String userId){
        return needingEventService.getUserNeedingEvents(userId);
    }

    @GetMapping(value="/allNeedingEvents")
    public List<String> getAllNeedingEvent(){
        return needingEventService.getAllNeedingEventsResponseDto();
    }

    @PostMapping(value = "/addUpdateVendor")
    public ResponseEntity<HttpStatus> addUpdateVendor(@RequestBody VendorRequestDto vendorRequestDto){
        return needingEventService.createNewVendor(vendorRequestDto);
    }
    /*
    updating needing event status
     */
    @PostMapping(value = "/updateNeedingEventStatus")
    public ResponseEntity<HttpStatus> updateNeedingEventStatus(@RequestParam(value = "needingEventId") String needingEventId){
        return needingEventService.updateNeedingEventStatus(needingEventId);
    }

    @GetMapping(value = "/needingEvent")
    public NeedingEventResponseDto getNeedingEventById(@RequestParam(value = "needingEventId") String needingEventId){
        log.info("Get needing event By Id");
        return needingEventService.getNeedingEventById(needingEventId);
    }

    @GetMapping(value="/getAllShoppingCategory")
    public List<ShoppingCategory> getAllShoppingCategory(){
        return needingEventService.getAllShoppingCategory();
    }

    @PostMapping(value="/deleteNeed")
    public ResponseEntity<HttpStatus> deleteNeed(@RequestParam(value = "needingEventId") String needingEventId){
        return needingEventService.deleteNeed(needingEventId);
    }
}
