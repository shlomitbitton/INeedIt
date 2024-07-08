package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.NeedingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedingEventRepository extends JpaRepository<NeedingEvent, Long>{


    @Query("SELECT needingEventId FROM NeedingEvent where publicNeed = 1")
    List<Long> getAllPublicNeeds();

    @Query("SELECT ne FROM NeedingEvent ne JOIN ne.userNeeds u WHERE u.user.userId = :userId")
    List<NeedingEvent> findByUserId(@Param("userId") Long userId);

}
