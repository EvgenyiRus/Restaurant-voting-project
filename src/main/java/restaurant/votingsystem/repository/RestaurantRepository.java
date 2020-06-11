package restaurant.votingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {

    @Query("select r from Restaurant r order by r.name")
    List<Restaurant> getAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);
}
