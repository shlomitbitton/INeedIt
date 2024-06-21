package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.*;
import i.need.it.IneedIt.enums.NeedingEventStatus;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.model.NeedingEvent;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.model.Vendor;
import i.need.it.IneedIt.repository.NeedingEventRepository;
import i.need.it.IneedIt.repository.UserRepository;
import i.need.it.IneedIt.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NeedingEventService {

    private final NeedingEventRepository needingEventRepository;
    private final UserRepository userRepository;

    private final VendorRepository vendorRepository;


    public ResponseEntity<HttpStatus> updateNeedingEventStatus(String needingEventId){
        Optional<NeedingEvent> needingEventToUpdate = needingEventRepository.findById(Long.valueOf(needingEventId));
        if(needingEventToUpdate.isPresent()){
            if(needingEventToUpdate.get().getNeedingEventStatus().equals(NeedingEventStatus.Need)){
                needingEventToUpdate.get().setNeedingEventStatus(NeedingEventStatus.Fulfilled);
            }else{
                needingEventToUpdate.get().setNeedingEventStatus(NeedingEventStatus.Need);
                needingEventToUpdate.get().setNeedingEventDateCreated(LocalDate.now());//zero out the date created
            }
            log.info("Needing event status {} has updated to {}", needingEventId, needingEventToUpdate.get().getNeedingEventStatus());
            needingEventRepository.save(needingEventToUpdate.get());
        }
        return ResponseEntity.ok().build();
    }

    public NeedingEventService(NeedingEventRepository needingEventRepository, UserRepository userRepository, VendorRepository vendorRepository) {
        this.needingEventRepository = needingEventRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
//        this.generateToken = generateToken;
    }

    public NeedingEventResponseDto createUpdateNeedingEvent(NeedingEventRequestDto needingEventRequestDto){
        Optional<User> user = userRepository.findUserByUserId(needingEventRequestDto.getUserId());
        Optional<Vendor> vendor = vendorRepository.findByVendorNameIgnoreCase(needingEventRequestDto.getVendorName());
        log.info("Vendor to update from request dto : "+needingEventRequestDto.getVendorName());
        NeedingEvent needingEvent = new NeedingEvent();
        NeedingEventResponseDto needingEventResponseDto = new NeedingEventResponseDto();
        if(user.isPresent()) {
            Map<String, List<NeedingEventResponseDto>> useerHasNeeds = getUserNeedingEvents(String.valueOf(user.get().getUserId()));
            if(!useerHasNeeds.isEmpty()) { //user has needs
                for (List<NeedingEventResponseDto> events : useerHasNeeds.values()) {
                    if (events.stream()
                            .anyMatch(itemNeeded -> itemNeeded.getItemNeededName().equalsIgnoreCase(needingEventRequestDto.getItemNeeded()))) {
                        //in case  the needing status is changed,  recent date created.
                        long needingEventId = events.stream().filter(itemNeeded -> itemNeeded.getItemNeededName().equalsIgnoreCase(needingEventRequestDto.getItemNeeded())).findFirst().get().getNeedingEventId();
                        needingEvent = needingEventRepository.findById(needingEventId).get();
                        needingEvent.setNeedingEventStatus(NeedingEventStatus.Need); //if the need had changed, it means it is in a needing status

                        // needingEvent.setNeedingEventDateCreated(LocalDate.now()); TODO: to enable when setting the toggle to need, the date need to be reset
                        if (vendor.isPresent() && StringUtils.isNotEmpty(needingEventRequestDto.getVendorName())) {
                            updateVendor(needingEventRequestDto.getVendorName(), vendor.get(), needingEvent);
                        }
                        needingEvent.setShoppingCategory(needingEventRequestDto.getShoppingCategory());
                        log.info("Updating shopping category");
                        log.info("A Needing event has been updated");
                    }else{
                        //save a new need
                        createNeedingEvent(needingEventRequestDto, needingEvent, user, vendor);
                        log.info("new Needing event has been created");
                    }
                }
            } else {//save a new needing event for the user
                createNeedingEvent(needingEventRequestDto, needingEvent, user, vendor);
                log.info("new Needing event has been created");
            }
            saveToDto(needingEvent, needingEventResponseDto);
        }
        return needingEventResponseDto;
    }

    private void createNeedingEvent(NeedingEventRequestDto needingEventRequestDto, NeedingEvent needingEvent, Optional<User> user, Optional<Vendor> vendor) {
        if (user.isPresent()) {
            needingEvent.setUser(user.get());
            needingEvent.setNeedingEventDateCreated(LocalDate.now());
            needingEvent.setNeedNotes(needingEventRequestDto.getNeedNotes());
            needingEvent.setPublicNeed(0);//By default, for a new need
            needingEvent.setShoppingCategory(ShoppingCategory.valueOf(String.valueOf(needingEventRequestDto.getShoppingCategory())));
            needingEvent.setItemNeeded(needingEventRequestDto.getItemNeeded());
            needingEvent.setNeedingEventStatus(NeedingEventStatus.Need);
            if (vendor .isPresent() && StringUtils.isNotEmpty(needingEventRequestDto.getVendorName())) {
                updateVendor(needingEventRequestDto.getVendorName(), vendor.get(), needingEvent);
            }
        }
    }

    private void saveToDto(NeedingEvent needingEvent, NeedingEventResponseDto needingEventResponseDto) {
        needingEventRepository.save(needingEvent);
        log.info("...and saved...");
        //populate the dto:
        needingEventResponseDto.setItemNeededName(needingEvent.getItemNeeded());
        needingEventResponseDto.setShoppingCategory(String.valueOf(needingEvent.getShoppingCategory()));
        needingEventResponseDto.setDaysListed(getDaysListed(needingEvent.getNeedingEventDateCreated()));
        needingEventResponseDto.setNeedNotes("");
        needingEventResponseDto.setIsPublic(needingEvent.getPublicNeed());
        needingEventResponseDto.setPotentialVendor(needingEvent.getVendor().getVendorName());
        needingEventResponseDto.setNeedingEventStatus(String.valueOf(needingEvent.getNeedingEventStatus()));
        needingEventResponseDto.setNeedingEventId(needingEvent.getNeedingEventId());
    }

    private void updateVendor(String updatedVendorName, Vendor vendor, NeedingEvent needingEvent) {
        log.info("Updating vendor: "+ updatedVendorName);
        if (vendor != null) {
            needingEvent.setVendor(vendor);
        } else {//add vendor to the vendor table
            VendorRequestDto newVendor = new VendorRequestDto();
            newVendor.setVendorName(updatedVendorName);
            createNewVendor(newVendor);
            needingEvent.setVendor(vendorRepository.findByVendorNameIgnoreCase(updatedVendorName).get());//now it should be there TODO: refactor the double call to the db
        }
    }


    private long getDaysListed(LocalDate dateCreated){
        return ChronoUnit.DAYS.between(dateCreated, LocalDate.now())+1;
    }

    public Map<String, List<NeedingEventResponseDto>> getUserNeedingEvents(String userId){
            List<NeedingEvent> needingEventsPerUser = needingEventRepository.findAll().stream().filter(user -> user.getUser().getUserId() == Long.parseLong(userId)).toList();

        Map<String, List<NeedingEventResponseDto>> mapOfUserNeedingEventDtos = new HashMap<>();
        for (NeedingEvent nepu : needingEventsPerUser) {
            NeedingEventResponseDto nrdto = getNeedingEventById(String.valueOf(nepu.getNeedingEventId()));
            List<NeedingEventResponseDto> list = mapOfUserNeedingEventDtos.computeIfAbsent(nrdto.getPotentialVendor(), k -> new ArrayList<>());
            list.add(nrdto);
            list.sort(Comparator.comparing(NeedingEventResponseDto::getItemNeededName, String.CASE_INSENSITIVE_ORDER));
        }
        return mapOfUserNeedingEventDtos;
                //sortNeeds(listOfNeedingEventDtoPerUser, true, false);
    }

    private List<NeedingEventResponseDto> structureNeedList(List<NeedingEventResponseDto> listOfNeedingEventDtoPerUser){

        listOfNeedingEventDtoPerUser.sort(
                Comparator.comparing(NeedingEventResponseDto::getNeedingEventStatus).reversed()
                        .thenComparing(NeedingEventResponseDto::getItemNeededName)
                        .thenComparing(NeedingEventResponseDto::getPotentialVendor)
                        .thenComparing(NeedingEventResponseDto::getShoppingCategory));
//                        .thenComparing(NeedingEventResponseDto::getDaysListed));
        return listOfNeedingEventDtoPerUser;
    }

    private List<NeedingEventResponseDto> sortNeeds(List<NeedingEventResponseDto> listOfNeedingEventDtoPerUser, boolean sortByVendor, boolean sortByCategory){
        if(sortByCategory){
            listOfNeedingEventDtoPerUser.sort(Comparator.comparing(NeedingEventResponseDto::getShoppingCategory)
                    .thenComparing(NeedingEventResponseDto::getItemNeededName));
        }
        if(sortByVendor){
            listOfNeedingEventDtoPerUser.sort(Comparator.comparing(NeedingEventResponseDto::getPotentialVendor)
                    .thenComparing(NeedingEventResponseDto::getItemNeededName));
        }
        return listOfNeedingEventDtoPerUser;
    }

    public List<String> getAllNeedingEventsResponseDto(){
        return needingEventRepository.streamAllItemsNeededByUserId();
    }

    public ResponseEntity<HttpStatus> createNewVendor(VendorRequestDto vendorRequestDto){
        Optional<Vendor> vendor = vendorRepository.findByVendorNameIgnoreCase(vendorRequestDto.getVendorName());
        if(vendor.isEmpty()){
            Vendor newVendor = new Vendor();
            newVendor.setVendorName(vendorRequestDto.getVendorName());
            vendorRepository.save(newVendor);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Transactional
    public NeedingEventResponseDto getNeedingEventById(String needingEventId) {
        NeedingEventResponseDto result = new NeedingEventResponseDto();
        log.info("Getting event Id: {}",needingEventId);
            Optional<NeedingEvent> needingEvent = needingEventRepository.findById(Long.valueOf(needingEventId));
            if (needingEvent.isEmpty()) {
                //return a page with a message of: event doesnt exist
            } else {
                return NeedingEventResponseDto.builder()
                        .itemNeededName(needingEvent.get().getItemNeeded())
                        .daysListed(getDaysListed(needingEvent.get().getNeedingEventDateCreated()))
                        .needNotes(needingEvent.get().getNeedNotes()== null? "":needingEvent.get().getNeedNotes())
                        .potentialVendor(needingEvent.get().getVendor().getVendorName())
                        .isPublic(needingEvent.get().getPublicNeed())
                        .shoppingCategory(String.valueOf(needingEvent.get().getShoppingCategory()))
                        .needingEventStatus(String.valueOf(needingEvent.get().getNeedingEventStatus()))
                        .needingEventId(needingEvent.get().getNeedingEventId())
                        .build();

            }
        return result;
    }

    public List<ShoppingCategory> getAllShoppingCategory() {
       return  new ArrayList<ShoppingCategory>(EnumSet.allOf(ShoppingCategory.class));
    }

    public ResponseEntity<HttpStatus> deleteNeed(Long needingEventId) {
        Optional<NeedingEvent> aNeedToBeDeleted = needingEventRepository.findById(needingEventId);
        if(aNeedToBeDeleted.isPresent()){
            needingEventRepository.delete(aNeedToBeDeleted.get());
            log.info("Need with id {} is being deleted", needingEventId);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            log.info("No need found with id: {}", needingEventId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<HttpStatus> updateNeedNotes(UpdateNeedNotesDto updateNeedNotesDto) {
        Optional<NeedingEvent> needEventToUpdate = needingEventRepository.findById(updateNeedNotesDto.getNeedEventId());
        if(needEventToUpdate.isPresent()){
            needEventToUpdate.get().setNeedNotes(updateNeedNotesDto.getNeedNotes());
            needingEventRepository.save(needEventToUpdate.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    public ResponseEntity<HttpStatus> changeNeedPublicStatus(String needingEventId) {
        Optional<NeedingEvent> needEventToUpdate = needingEventRepository.findById(Long.valueOf(needingEventId));
        if(needEventToUpdate.isPresent()){
            if(needEventToUpdate.get().getPublicNeed() == 0){
                needEventToUpdate.get().setPublicNeed(1);
            }else{
                needEventToUpdate.get().setPublicNeed(0);
            }
            needingEventRepository.save(needEventToUpdate.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public List<PublicNeedsResponseDto> getAllPublicNeeds() {
        List<Long> publicNeeds = needingEventRepository.getAllPublicNeeds();
        return publicNeeds.stream()
                .map(needingEventRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(needingEvent -> new PublicNeedsResponseDto(needingEvent.getNeedingEventId(), needingEvent.getItemNeeded()))
                .collect(Collectors.toList());
    }

}
