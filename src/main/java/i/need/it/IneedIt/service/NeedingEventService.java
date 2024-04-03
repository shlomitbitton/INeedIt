package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.NeedingEventRequestDto;
import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import i.need.it.IneedIt.enums.ItemNeeded;
import i.need.it.IneedIt.enums.ShoppingCategory;
import i.need.it.IneedIt.model.NeedingEvent;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.repository.NeedingEventRepository;
import i.need.it.IneedIt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class NeedingEventService {

    private final NeedingEventRepository needingEventRepository;
    private final UserRepository userRepository;

    public NeedingEventService(NeedingEventRepository needingEventRepository, UserRepository userRepository) {
        this.needingEventRepository = needingEventRepository;
        this.userRepository = userRepository;
    }

    public NeedingEventResponseDto createNewNeedingEvent(NeedingEventRequestDto needingEventRequestDto){
        Optional<User> user = userRepository.findUserById(needingEventRequestDto.getUserId());
        NeedingEvent needingEvent = new NeedingEvent();
        NeedingEventResponseDto needingEventResponseDto = new NeedingEventResponseDto();
        if(user.isPresent()) {
            needingEvent.setUser(user.get());//TODO: get from session
            //        needingEvent.setPurchasingResource(new ArrayList<>());//TODO: Will be set automatically as options
            needingEvent.setNeedingEventDateCreated(LocalDate.now());
            needingEvent.setShoppingCategory(ShoppingCategory.valueOf(String.valueOf(needingEventRequestDto.getShoppingCategory())));
            needingEvent.setItemNeeded(ItemNeeded.valueOf(String.valueOf(needingEventRequestDto.getItemNeeded())));
            needingEventRepository.save(needingEvent);

            needingEventResponseDto.setItemNeeded(needingEvent.getItemNeeded());
            needingEventResponseDto.setShoppingCategory(needingEvent.getShoppingCategory());
            needingEventResponseDto.setDaysListed(LocalDate.ofEpochDay(ChronoUnit.DAYS.between(LocalDate.now(), needingEvent.getNeedingEventDateCreated())));
            needingEventResponseDto.setUserId(needingEvent.getUser().getId());
            log.info("new Needing event has been created");

        }
        return needingEventResponseDto;
    }
       // return new ResponseEntity<NeedingEventResponseDto>(HttpStatus.OK);

    public List<String> getUserNeedingEvents(String userId){
        return needingEventRepository.streamAllItemsNeededByUserId(userId);

/*
        List<NeedingEvent> userNeedingEventList = needingEventRepository.findByUserId(userId);
        if(!userNeedingEventList.isEmpty()) {
            userNeedingEventList.stream().
            List<NeedingEventResponseDto> listOfNeedingEventResponseDtoOfUser = new ArrayList<>();

            NeedingEventResponseDto nerd = new NeedingEventResponseDto();

            for (NeedingEvent ne : userNeedingEventList) {
                log.info("ne: "+ ne.getNeedingEventId());
                nerd.set.setNeedingEventId(ne.getNeedingEventId());
                nerd.setItemName(ne.getItemNeeded().name());
                listOfNeedingEventResponseDtoOfUser.add(nerd);
            }
            return listOfNeedingEventResponseDtoOfUser;
        }
       return new ArrayList<>();
*/
    }

    public List<String> getAllNeedingEventsResponseDto(){
        return needingEventRepository.streamAllItemsNeededByUserId();
    }
}
