package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.NeedingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedingEventRepository extends JpaRepository<NeedingEvent, Long>{

//    List<NeedingEvent> findByUserId(Long userId);

    @Query("SELECT itemNeeded FROM NeedingEvent")
    List<String> streamAllItemsNeededByUserId();

    @Query("SELECT itemNeeded FROM NeedingEvent where user = ?1")
    List<String> streamAllItemsNeededByUserId(String userId);

}
