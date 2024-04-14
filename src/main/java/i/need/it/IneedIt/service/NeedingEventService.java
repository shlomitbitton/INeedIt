package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import i.need.it.IneedIt.dto.VendorRequestDto;
import i.need.it.IneedIt.enums.NeedingEventStatus;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.model.NeedingEvent;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.model.Vendor;
import i.need.it.IneedIt.repository.NeedingEventRepository;
import i.need.it.IneedIt.repository.UserRepository;
import i.need.it.IneedIt.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class NeedingEventService {

    private final NeedingEventRepository needingEventRepository;
    private final UserRepository userRepository;

    private final VendorRepository vendorRepository;


    public ResponseEntity<HttpStatus> updateNeedingEventStatus(String needingEventId){
        Optional<NeedingEvent> needingEventToUpdate = needingEventRepository.findById(Long.valueOf(needingEventId));
        if(needingEventToUpdate.isPresent()){
            needingEventToUpdate.get().setNeedingEventStatus(NeedingEventStatus.Fulfilled);
            needingEventRepository.save(needingEventToUpdate.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public NeedingEventService(NeedingEventRepository needingEventRepository, UserRepository userRepository, VendorRepository vendorRepository) {
        this.needingEventRepository = needingEventRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
    }

    public NeedingEventResponseDto createNewNeedingEvent(NeedingEventRequestDto needingEventRequestDto){
        Optional<User> user = userRepository.findUserById(needingEventRequestDto.getUserId());
        Optional<Vendor> vendor = vendorRepository.findVendorByVendorName(needingEventRequestDto.getVendorName());
        NeedingEvent needingEvent = new NeedingEvent();
        NeedingEventResponseDto needingEventResponseDto = new NeedingEventResponseDto();
        if(user.isPresent()) {
            needingEvent.setUser(user.get());//TODO: get from session
            needingEvent.setNeedingEventDateCreated(LocalDate.now());
            needingEvent.setShoppingCategory(ShoppingCategory.valueOf(String.valueOf(needingEventRequestDto.getShoppingCategory())));
            needingEvent.setItemNeeded(needingEventRequestDto.getItemNeeded());
            needingEvent.setNeedingEventStatus(NeedingEventStatus.Need);
            if(vendor.isPresent()) {
                needingEvent.setVendor(vendor.get());
            }else{//add vendor to the vendor table
               VendorRequestDto newVendor = new VendorRequestDto();
               newVendor.setVendorName(needingEventRequestDto.getVendorName());
               createNewVendor(newVendor);
               needingEvent.setVendor(vendorRepository.findVendorByVendorName(needingEventRequestDto.getVendorName()).get());//now it should be there TODO: refactor the double call to the db
            }

            needingEventRepository.save(needingEvent);

            needingEventResponseDto.setItemNeededName(needingEvent.getItemNeeded());
            needingEventResponseDto.setShoppingCategory(String.valueOf(needingEvent.getShoppingCategory()));
            needingEventResponseDto.setDaysListed(ChronoUnit.DAYS.between(LocalDate.now(), needingEvent.getNeedingEventDateCreated()));
            //needingEventResponseDto.setUserId(needingEvent.getUser().getId());
            needingEventResponseDto.setNeedingEventStatus(String.valueOf(needingEvent.getNeedingEventStatus()));
            log.info("new Needing event has been created");

        }
        return needingEventResponseDto;
    }

    public List<String> getUserNeedingEvents(String userId){
        return needingEventRepository.streamAllItemsNeededByUserId(userId);

    }

    public List<String> getAllNeedingEventsResponseDto(){
        return needingEventRepository.streamAllItemsNeededByUserId();
    }

    public ResponseEntity<HttpStatus> createNewVendor(VendorRequestDto vendorRequestDto){
        Optional<Vendor> vendor = vendorRepository.findVendorByVendorName(vendorRequestDto.getVendorName());
        if(vendor.isEmpty()){
            Vendor newVendor = new Vendor();
            newVendor.setVendorName(vendorRequestDto.getVendorName());
            vendorRepository.save(newVendor);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public NeedingEventResponseDto getNeedingEventById(String needingEventId) {
        NeedingEventResponseDto result = new NeedingEventResponseDto();
        log.info("Getting event Id: {}",needingEventId);
            Optional<NeedingEvent> needingEvent = needingEventRepository.findById(Long.valueOf(needingEventId));
            if (needingEvent.isEmpty()) {
                //return a page with a message of: event doesnt exist
            } else {
                return NeedingEventResponseDto.builder()
                        .itemNeededName(needingEvent.get().getItemNeeded())
                        .daysListed(ChronoUnit.DAYS.between(needingEvent.get().getNeedingEventDateCreated(),LocalDate.now()))
                        .shoppingCategory(String.valueOf(needingEvent.get().getShoppingCategory()))
                        .needingEventStatus(String.valueOf(needingEvent.get().getNeedingEventStatus()))
                        .build();

            }
        return result;
    }
}
