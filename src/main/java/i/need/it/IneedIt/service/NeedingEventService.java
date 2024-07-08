package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.*;
import i.need.it.IneedIt.enums.NeedingEventStatus;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.model.NeedingEvent;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.model.UserNeeds;
import i.need.it.IneedIt.model.Vendor;
import i.need.it.IneedIt.model.embeddable.UserNeedsId;
import i.need.it.IneedIt.repository.NeedingEventRepository;
import i.need.it.IneedIt.repository.UserNeedsRepository;
import i.need.it.IneedIt.repository.UserRepository;
import i.need.it.IneedIt.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

@Slf4j
@Component
public class NeedingEventService {

    private final NeedingEventRepository needingEventRepository;
    private final UserRepository userRepository;
    private final UserNeedsRepository userNeedsRepository;

    private final VendorRepository vendorRepository;


    public Optional<NeedingEvent> getNeedingEventByNeedingEventId(String needingEventId){
        return needingEventRepository.findById(parseLong(needingEventId));
    }

    public boolean updateNeedingEventStatus(String needingEventId, NeedingEvent needingEventToUpdate) {
        if (needingEventToUpdate.getNeedingEventStatus().equals(NeedingEventStatus.Need)) {
            needingEventToUpdate.setNeedingEventStatus(NeedingEventStatus.Fulfilled);
        } else {
            needingEventToUpdate.setNeedingEventStatus(NeedingEventStatus.Need);
            needingEventToUpdate.setNeedingEventDateCreated(LocalDate.now());//zero out the date created
        }
        try {
            log.info("Needing event status {} has updated to {}", needingEventId, needingEventToUpdate.getNeedingEventStatus());
            needingEventRepository.save(needingEventToUpdate);
        } catch (Exception exception) {
            log.error("needing event was not updated");
            return false;
        }
        return true;
    }

    public NeedingEventService(NeedingEventRepository needingEventRepository, UserRepository userRepository, UserNeedsRepository userNeedsRepository, VendorRepository vendorRepository) {
        this.needingEventRepository = needingEventRepository;
        this.userRepository = userRepository;
        this.userNeedsRepository = userNeedsRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public NeedingEventResponseDto createUpdateNeedingEvent(NeedingEventRequestDto needingEventRequestDto){
        Optional<User> user = userRepository.findUserByUserId(needingEventRequestDto.getUserId());
//        Optional<Vendor> vendor = vendorRepository.findByVendorNameIgnoreCase(needingEventRequestDto.getVendorName());
        log.info("Vendor to update from request dto : "+needingEventRequestDto.getVendorName());
        NeedingEvent needingEvent = new NeedingEvent();
        NeedingEventResponseDto needingEventResponseDto = new NeedingEventResponseDto();
        if(user.isPresent()) {
            boolean itemHasFoundInUsersList = false;
            Map<String, List<NeedingEventResponseDto>> userHasNeeds = getUserNeedingEvents(String.valueOf(user.get().getUserId()));
            if(!userHasNeeds.isEmpty()) { //user has needs
                for (List<NeedingEventResponseDto> events : userHasNeeds.values()) {//needs to iterate thoguh the entire events
                    //find if the user has this need already
                    itemHasFoundInUsersList = events.stream()
                            .anyMatch(itemNeeded -> itemNeeded.getItemNeededName().equalsIgnoreCase(needingEventRequestDto.getItemNeeded()));
                    if (itemHasFoundInUsersList) {//check if this item already exists
                        log.info("this item {} already exists in the user list ", needingEventRequestDto.getItemNeeded());
                        //in case  the needing status is changed,  recent date created.
                        long needingEventId = events.stream().filter(itemNeeded -> itemNeeded.getItemNeededName().equalsIgnoreCase(needingEventRequestDto.getItemNeeded())).findFirst().get().getNeedingEventId();
                        Optional<NeedingEvent> ne = needingEventRepository.findById(needingEventId);
                        if(ne.isPresent()){
                            needingEvent = ne.get();
                        }
                        needingEvent.setNeedingEventStatus(NeedingEventStatus.Need); //if the need had changed, it means it is in a needing status

                        // needingEvent.setNeedingEventDateCreated(LocalDate.now()); TODO: to enable when setting the toggle to need, the date need to be reset
                        if (StringUtils.isNotEmpty(needingEventRequestDto.getVendorName())) {
                            updateVendor(needingEventRequestDto.getVendorName(), needingEvent);
                        }
                        needingEvent.setShoppingCategory(needingEventRequestDto.getShoppingCategory());
                        log.info("Updating shopping category");
                        log.info("A Needing event has been updated");
                        break;
                    }
                }
            }
            if(userHasNeeds.isEmpty() || !itemHasFoundInUsersList) {//save a new needing event for the user
                createNeedingEvent(needingEventRequestDto, needingEvent, user);
                log.info("First needing event has been created");
            }
            needingEventRepository.save(needingEvent);
            createUserNeedsRelationship(user.get(), needingEvent);
            saveToDto(needingEvent, needingEventResponseDto);
        }
        return needingEventResponseDto;
    }

    protected void createUserNeedsRelationship(User user, NeedingEvent needingEvent){
        List<UserNeeds> listOfUserNeedsRelationships= userNeedsRepository.findByUser(user);
        for(UserNeeds un : listOfUserNeedsRelationships){
            log.info(un.getNeed().getItemNeeded()+ " " + un.getNeed().getNeedingEventId());
        }
        Optional<UserNeeds> existingUserNeedRelation = listOfUserNeedsRelationships.stream().filter(need -> need.getNeed().getItemNeeded().equals(needingEvent.getItemNeeded())).findFirst();
        if(existingUserNeedRelation.isEmpty()){ //relationship doesnt exists, create one
            UserNeedsId userNeedsId = new UserNeedsId();
            userNeedsId.setNeedId(needingEvent.getNeedingEventId());
            userNeedsId.setUserId(user.getUserId());

            UserNeeds userNeeds = new UserNeeds();
            userNeeds.setUserNeedsId(userNeedsId);
            userNeeds.setUser(user);
            userNeeds.setNeed(needingEvent);

            userNeedsRepository.save(userNeeds);
            log.info("updated a new user-need relationship");
        }else{
            log.info("User Need Relationship already exist");
        }
    }

    private void createNeedingEvent(NeedingEventRequestDto needingEventRequestDto, NeedingEvent needingEvent, Optional<User> user) {
        if (user.isPresent()) {
//            needingEvent.setUserNeeds(user.get());
            needingEvent.setNeedingEventDateCreated(LocalDate.now());
            needingEvent.setNeedNotes(needingEventRequestDto.getNeedNotes());
            needingEvent.setPublicNeed(0);//By default, for a new need
            needingEvent.setShoppingCategory(ShoppingCategory.valueOf(String.valueOf(needingEventRequestDto.getShoppingCategory())));
            needingEvent.setItemNeeded(needingEventRequestDto.getItemNeeded());
            needingEvent.setNeedingEventStatus(NeedingEventStatus.Need);
            if (StringUtils.isNotEmpty(needingEventRequestDto.getVendorName())) {
                updateVendor(needingEventRequestDto.getVendorName(), needingEvent);
            }
        }
    }

    private void saveToDto(NeedingEvent needingEvent, NeedingEventResponseDto needingEventResponseDto) {
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

    private void updateVendor(String vendorName, NeedingEvent needingEvent) {
        Optional<Vendor> vendor = vendorRepository.findByVendorNameIgnoreCase(vendorName);
        if (vendor.isPresent()) {
            needingEvent.setVendor(vendor.get());
            log.info("Updating exsiting vendor: "+ vendorName);
        } else {//add vendor to the vendor table
            log.info("create a new vendor");
            VendorRequestDto newVendor = new VendorRequestDto();
            newVendor.setVendorName(vendorName);
            needingEvent.setVendor(createNewVendor(newVendor));
        }
    }


    private long getDaysListed(LocalDate dateCreated){
        return ChronoUnit.DAYS.between(dateCreated, LocalDate.now())+1;
    }

    public Map<String, List<NeedingEventResponseDto>> getUserNeedingEvents(String userId){

        List<UserNeeds> needingEventsPerUser = userNeedsRepository.findByUserId(parseLong(userId));//this gives the records which associated with that userId
        Map<String, List<NeedingEventResponseDto>> mapOfUserNeedingEventDtos = new HashMap<>();
        for (UserNeeds nepu : needingEventsPerUser) {
            NeedingEventResponseDto nrdto = getNeedingEventById(String.valueOf(nepu.getUserNeedsId().getNeedId()));
            List<NeedingEventResponseDto> list = mapOfUserNeedingEventDtos.computeIfAbsent(nrdto.getPotentialVendor(), k -> new ArrayList<>());
            list.add(nrdto);
            list.sort(Comparator.comparing(NeedingEventResponseDto::getItemNeededName, String.CASE_INSENSITIVE_ORDER));
        }

        return mapOfUserNeedingEventDtos;
                //sortNeeds(listOfNeedingEventDtoPerUser, true, false);
    }


//    private List<NeedingEventResponseDto> sortNeeds(List<NeedingEventResponseDto> listOfNeedingEventDtoPerUser, boolean sortByVendor, boolean sortByCategory){
//        if(sortByCategory){
//            listOfNeedingEventDtoPerUser.sort(Comparator.comparing(NeedingEventResponseDto::getShoppingCategory)
//                    .thenComparing(NeedingEventResponseDto::getItemNeededName));
//        }
//        if(sortByVendor){
//            listOfNeedingEventDtoPerUser.sort(Comparator.comparing(NeedingEventResponseDto::getPotentialVendor)
//                    .thenComparing(NeedingEventResponseDto::getItemNeededName));
//        }
//        return listOfNeedingEventDtoPerUser;
//    }

//    public List<String> getAllNeedingEventsResponseDto(){
//        return needingEventRepository.streamAllItemsNeededByUserId();
//    }

    public Vendor createNewVendor(VendorRequestDto vendorRequestDto){
        Optional<Vendor> vendor = vendorRepository.findByVendorNameIgnoreCase(vendorRequestDto.getVendorName());
        if(vendor.isEmpty()){
            Vendor newVendor = new Vendor();
            newVendor.setVendorName(vendorRequestDto.getVendorName());
            vendorRepository.save(newVendor);
            return newVendor;
        }
        return null;
    }

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

    public boolean deleteNeed(Long needingEventId, String userId) {
            //look for the relatioship:
            Optional<User> user = userRepository.findUserByUserId(Long.valueOf(userId));
            if (user.isPresent()) {
                Optional<UserNeeds> potentialNeedingEventRelatioshipExists = userNeedsRepository.findByUser(user.get()).stream().filter(n -> n.getNeed().getNeedingEventId() == needingEventId).findFirst();
                potentialNeedingEventRelatioshipExists.ifPresent(userNeedsRepository::delete);
                Optional<NeedingEvent> aNeedToBeDeleted = needingEventRepository.findById(needingEventId);
                if (aNeedToBeDeleted.isPresent()) {
                    needingEventRepository.delete(aNeedToBeDeleted.get());
                    log.info("Need with id {} is being deleted", needingEventId);
                    return true;
                }
            } else {
                log.info("No need found with id: {}", needingEventId);
            }
        return false;
    }

    public boolean updateNeedNotes(UpdateNeedNotesDto updateNeedNotesDto) {
        Optional<NeedingEvent> needEventToUpdate = needingEventRepository.findById(updateNeedNotesDto.getNeedEventId());
        if(needEventToUpdate.isPresent()){
            needEventToUpdate.get().setNeedNotes(updateNeedNotesDto.getNeedNotes());
            needingEventRepository.save(needEventToUpdate.get());
            return true;
        }
        return false;
    }



    public boolean changeNeedPublicStatus(String needingEventId) {
        Optional<NeedingEvent> needEventToUpdate = needingEventRepository.findById(Long.valueOf(needingEventId));
        if(needEventToUpdate.isPresent()){
            if(needEventToUpdate.get().getPublicNeed() == 0){
                needEventToUpdate.get().setPublicNeed(1);
            }else{
                needEventToUpdate.get().setPublicNeed(0);
            }
            needingEventRepository.save(needEventToUpdate.get());
            return true;
        }
        return false;
    }

    public List<PublicNeedsResponseDto> getAllPublicNeeds() {
        List<Long> publicNeeds = needingEventRepository.getAllPublicNeeds();
        return publicNeeds.stream()
                .map(needingEventRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(needingEvent -> {
                    // Safely get the first userId from userNeeds or provide a default value
                    Optional<Long> firstUserIdOptional = needingEvent.getUserNeeds().stream()
                            .map(u -> u.getUser().getUserId())
                            .findFirst();
                    // Use the userId if present, or use a default value (e.g., -1L)
                    Long userId = firstUserIdOptional.orElse(-1L);//TODO: for now we will get only the first user owner and not the entire list
                    // Create the DTO with safe or default values
                    return new PublicNeedsResponseDto(
                            needingEvent.getNeedingEventId(),
                            needingEvent.getItemNeeded(),
                            needingEvent.getNeedNotes(),
                            userId
                    );
                })
                .collect(Collectors.toList());
    }

}
