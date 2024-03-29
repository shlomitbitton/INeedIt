package i.need.it.IneedIt.service;

import i.need.it.IneedIt.model.ItemNeeded;
import i.need.it.IneedIt.model.NeedingEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class NeedingEventService {


    public NeedingEvent createNewNeedingEvent(ItemNeeded itemNeeded){
        return NeedingEvent.builder()
                .needingEventDateCreated(LocalDate.now()).daysListed(0).build();
    }
}
