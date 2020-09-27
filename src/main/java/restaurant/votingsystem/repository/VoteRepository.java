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

    @Query("select v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant " +
            "where v.date=:date and v.restaurant.id=:id")
    Optional<List<Vote>> getAllVotesByRestaurant(int id, LocalDate date);

    @Query("select v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant " +
            "where v.user.id=:id and v.date=:date")
    Optional<Vote> getVoteByUserToDay(int id, LocalDate date);

    @Query("select v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant " +
            "where v.user.id=:id")
    Optional<List<Vote>> getAllVotesByUser(int id);

}
