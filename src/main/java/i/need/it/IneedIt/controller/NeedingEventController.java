package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.config.SecurityUtils;
import i.need.it.IneedIt.dto.*;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.service.NeedingEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api")
public class NeedingEventController {

    private final NeedingEventService needingEventService;
    StatusResponseDto errorResponse = new StatusResponseDto(HttpStatus.FORBIDDEN.value(), "Access Denied");

    public NeedingEventController(NeedingEventService needingEventService) {
        this.needingEventService = needingEventService;
    }



    /*
    create new needing event
     */

    @PostMapping(value="/add-update-needing-event")
    public ResponseEntity<StatusResponseDto>addUpdateNeedingEvent(@RequestBody NeedingEventRequestDto needingEventDto){
        Object currentUserId = SecurityUtils.getCurrentUserId();
        if(currentUserId != null && currentUserId.toString().equals(needingEventDto.getUserId().toString())) {
            log.info("User creating a new needing event");
            return ResponseEntity.status(HttpStatus.OK).body(needingEventService.createUpdateNeedingEvent(needingEventDto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    /*
        This endpoint present all the needs of a user
    */
    @GetMapping(value="/all-needs-by-user")
    public List<NeedingEventResponseDto> getUserNeeds(@RequestParam(value = "user-id") String userId){
        Object currentUserId = SecurityUtils.getCurrentUserId();
        if(Objects.equals(currentUserId, userId)) {
            return needingEventService.getUserNeedingEvents(userId);
        }
        return null;
    }



    @PostMapping(value="make-need-public")
    public ResponseEntity<HttpStatus> changeNeedPublicStatus(@RequestParam(value = "needing-event-id") String needingEventId){
        return needingEventService.changeNeedPublicStatus(needingEventId);
    }

    @PostMapping(value = "/add-update-vendor")
    public ResponseEntity<HttpStatus> addUpdateVendor(@RequestBody VendorRequestDto vendorRequestDto){
        return needingEventService.createNewVendor(vendorRequestDto);
    }
    /*
    updating needing event status
     */
    @PostMapping(value = "/update-needing-event-status")
    public ResponseEntity<HttpStatus> updateNeedingEventStatus(@RequestParam(value = "needing-event-id") String needingEventId){
        return needingEventService.updateNeedingEventStatus(needingEventId);
    }

    /*
    needing event by id
     */
    @GetMapping(value = "/needing-event")
    public NeedingEventResponseDto getNeedingEventById(@RequestParam(value = "needing-event-id") String needingEventId){
        log.info("Get needing event By Id");
        return needingEventService.getNeedingEventById(needingEventId);
    }

    @GetMapping(value="/shopping-category")
    public List<ShoppingCategory> getAllShoppingCategory(){
        return needingEventService.getAllShoppingCategory();
    }

    @DeleteMapping(value="/delete-need/{needing-event-id}")
    public ResponseEntity<HttpStatus> deleteNeed(@PathVariable("needing-event-id") Long needingEventId){
        return needingEventService.deleteNeed(needingEventId);
    }

    @PostMapping(value="/update-need-notes")
    public ResponseEntity<HttpStatus> updateNedNotes(@RequestBody UpdateNeedNotesDto updateNeedNotesDto){
        return needingEventService.updateNeedNotes(updateNeedNotesDto);
    }

}
