package real_time_leaderboard.user_service.repository;

import real_time_leaderboard.user_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Case-insensitive email search
    @Query("{ 'email' : { $regex: ?0, $options: 'i' } }")
    Optional<User> findByEmailIgnoreCase(String email);
}