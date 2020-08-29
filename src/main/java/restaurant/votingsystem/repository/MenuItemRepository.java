package restaurant.votingsystem.repository;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Query("select mi FROM MenuItem mi JOIN FETCH mi.dish JOIN FETCH mi.restaurant " +
            "where mi.date=:date and mi.restaurant.id=:id")
    List<MenuItem> getMenuOnDateByRestaurant(@Param("id") Integer id, @Param("date") LocalDate date);

    @Query("SELECT mi FROM MenuItem mi JOIN FETCH mi.dish JOIN FETCH mi.restaurant " +
            "where mi.date=:date order by mi.restaurant.name asc")
    List<MenuItem> getAllMenus(@Param("date") LocalDate date);

    @Query("SELECT mi FROM MenuItem mi JOIN FETCH mi.dish JOIN FETCH mi.restaurant " +
            "where mi.dish.id=:id order by mi.date desc")
    List<MenuItem> getHistoryDish(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.id=:id")
    int delete(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.restaurant.id=:id")
    int deleteAllForRestaurant(@Param("id") int id);
}
