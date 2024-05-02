package i.need.it.IneedIt.repository;

import i.need.it.IneedIt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long userId);
    Optional<User> findUserByUsername(String userId);
}
