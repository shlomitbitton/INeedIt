package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.config.SecurityUtils;
import i.need.it.IneedIt.dto.*;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.model.NeedingEvent;
import i.need.it.IneedIt.model.Vendor;
import i.need.it.IneedIt.service.NeedingEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class NeedingEventController {

    private final NeedingEventService needingEventService;
    private final StatusResponseDto errorResponse = new StatusResponseDto(HttpStatus.FORBIDDEN.value(), "Access Denied");

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
    public Map<String, List<NeedingEventResponseDto>> getUserNeeds(@RequestParam(value = "user-id") String userId){
        Object currentUserId = SecurityUtils.getCurrentUserId();
        if(Objects.equals(currentUserId, userId)) {
            return needingEventService.getUserNeedingEvents(userId);
        }
        return null;
    }



    @PostMapping(value="make-need-public")
    public ResponseEntity<HttpStatus> changeNeedPublicStatus(@RequestParam(value = "needing-event-id") String needingEventId){
        boolean isUpdated = needingEventService.changeNeedPublicStatus(needingEventId);
        if(isUpdated){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add-update-vendor")
    public ResponseEntity<HttpStatus> addUpdateVendor(@RequestBody VendorRequestDto vendorRequestDto){
        if(vendorRequestDto.getVendorName() != null){
            Vendor vendor = needingEventService.createNewVendor(vendorRequestDto);
            if(vendor != null)   {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
    updating needing event status
     */
    @PostMapping(value = "/update-needing-event-status")
    public ResponseEntity<HttpStatus> updateNeedingEventStatus(@RequestParam(value = "needing-event-id") String needingEventId){
    Optional<NeedingEvent> needingEvent= needingEventService.getNeedingEventByNeedingEventId(needingEventId);
    Object currentUserId = SecurityUtils.getCurrentUserId();
    if(needingEvent.isPresent() && currentUserId != null
            && needingEvent.get().getUserNeeds().stream().anyMatch(user->user.getUser().getUserId() == Long.parseLong(currentUserId.toString()))){
        boolean isUpdated = needingEventService.updateNeedingEventStatus(needingEventId, needingEvent.get());
        if(isUpdated){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
        Object currentUserId = SecurityUtils.getCurrentUserId();
        if(currentUserId != null && needingEventService.deleteNeed(needingEventId, currentUserId.toString())){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/update-need-notes")
    public ResponseEntity<HttpStatus> updateNedNotes(@RequestBody UpdateNeedNotesDto updateNeedNotesDto){
        boolean isUpdated = needingEventService.updateNeedNotes(updateNeedNotesDto);
        if(isUpdated){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/public-needs")
    public List<PublicNeedsResponseDto> getAllPublicNeeds(){
        return needingEventService.getAllPublicNeeds();
    }

}
