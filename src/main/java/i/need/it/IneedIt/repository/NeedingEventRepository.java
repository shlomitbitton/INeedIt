package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.NeedingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeedingEventRepository extends JpaRepository<NeedingEvent, Long>{
}
