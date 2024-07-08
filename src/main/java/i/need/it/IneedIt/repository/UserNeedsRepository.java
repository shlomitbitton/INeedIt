package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.model.UserNeeds;
import i.need.it.IneedIt.model.embeddable.UserNeedsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserNeedsRepository extends JpaRepository<UserNeeds, UserNeedsId> {

    List<UserNeeds> findByUser(User user);
    @Query("SELECT un FROM UserNeeds un WHERE un.user.userId = :userId")
    List<UserNeeds> findByUserId(Long userId);
}
