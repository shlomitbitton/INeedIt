package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.NeedingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedingEventRepository extends JpaRepository<NeedingEvent, Long>{


    @Query("SELECT itemNeeded FROM NeedingEvent")
    List<String> streamAllItemsNeededByUserId();

    @Query("SELECT needingEventId FROM NeedingEvent where user.userId = ?1")
    List<Long> getAllItemsNeededByUserId(String userId);

    @Query("SELECT needingEventId FROM NeedingEvent where publicNeed = 1")
    List<Long> getAllPublicNeeds();

}
