package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import i.need.it.IneedIt.dto.VendorRequestDto;
import i.need.it.IneedIt.enums.NeedingEventStatus;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.model.NeedingEvent;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.repository.NeedingEventRepository;
import i.need.it.IneedIt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class NeedingEventService {

    private final NeedingEventRepository needingEventRepository;
    private UserRepository userRepository;

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
    }

    public NeedingEventResponseDto createNewNeedingEvent(NeedingEventRequestDto needingEventRequestDto){

        NeedingEvent needingEvent = new NeedingEvent();
        needingEvent.setUser(new User());//TODO: get from session
        needingEvent.setDaysListed(1);
        needingEvent.setPurchasingResource(new ArrayList<>());//TODO: Will be set automatically as options
        needingEvent.setNeedingEventDateCreated(LocalDate.now());
        needingEvent.setShoppingCategory(needingEventRequestDto.getShoppingCategory());
        needingEvent.setItemNeeded(needingEventRequestDto.getItemNeeded());
        needingEventRepository.save(needingEvent);

        NeedingEventResponseDto needingEventResponseDto = new NeedingEventResponseDto();
        needingEventResponseDto.setItemName(String.valueOf(needingEvent.getItemNeeded()));
        log.info("new Needing event has been created");
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.map(needingEvent,needingEventResponseDto);
        return needingEventResponseDto;
    }
       // return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);

    public List<NeedingEventResponseDto> getUserNeedingEvents(long userId){

        List<NeedingEvent> userNeedingEventList = needingEventRepository.findByUserId(userId);
        if(!userNeedingEventList.isEmpty()) {
            List<NeedingEventResponseDto> listOfNeedingEventResponseDtoOfUser = new ArrayList<>();
            NeedingEventResponseDto nerd = new NeedingEventResponseDto();

            for (NeedingEvent ne : userNeedingEventList) {
                log.info("ne: "+ ne.getNeedingEventId());
                nerd.setNeedingEventId(ne.getNeedingEventId());
                nerd.setItemName(ne.getItemNeeded().name());
                listOfNeedingEventResponseDtoOfUser.add(nerd);
            }
            return listOfNeedingEventResponseDtoOfUser;
        }
       return new ArrayList<>();
    }
}
