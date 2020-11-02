package restaurant.votingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Query("select mi FROM MenuItem mi JOIN FETCH mi.dish JOIN FETCH mi.restaurant " +
            "where mi.date=:date and mi.restaurant.id=:restaurantId")
    List<MenuItem> getMenuOnDateByRestaurant(@Param("restaurantId") Integer restaurantId, @Param("date") LocalDate date);

    @Query("SELECT mi FROM MenuItem mi JOIN FETCH mi.dish JOIN FETCH mi.restaurant " +
            "where mi.date=:date order by mi.restaurant.name asc")
    List<MenuItem> getAllMenus(@Param("date") LocalDate date);

    @Query("SELECT mi FROM MenuItem mi JOIN FETCH mi.restaurant JOIN FETCH mi.dish " +
            "where mi.dish.id=:dishId order by mi.date desc")
    List<MenuItem> getHistoryDish(@Param("dishId") Integer dishId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.restaurant.id=:restaurantId and mi.id=:id and mi.date=:date")
    int delete(@Param("restaurantId") int restaurantId, @Param("id") int id, @Param("date") LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.restaurant.id=:restaurantId and mi.date=:date")
    int deleteAllForRestaurant(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);
}
