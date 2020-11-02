package restaurant.votingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("select v FROM Vote v JOIN FETCH v.user " +
            "where v.restaurant.id=:restaurantId and v.date=:date")
    List<Vote> getAllByRestaurantOnDay(int restaurantId, LocalDate date);

    @Query("select v FROM Vote v JOIN FETCH v.restaurant " +
            "where v.user.id=:userId and v.date=:date")
    Optional<Vote> getAllByUserOnDay(int userId, LocalDate date);

    @Query("select v FROM Vote v JOIN FETCH v.restaurant " +
            "where v.user.id=:userId")
    List<Vote> getAllByUser(int userId);

}
