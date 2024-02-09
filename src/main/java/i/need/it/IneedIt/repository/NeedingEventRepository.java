package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.NeedingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeedingEventRepository extends JpaRepository<NeedingEvent, Long>{
}
