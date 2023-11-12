package usersService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import usersService.model.CustomUser;
import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("DELETE FROM CUSTOM_USER u WHERE u.email = :email")
    void deleteByEmail(String email);
}
