package restaurant.votingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish,Integer> {

    Dish findById(int id);

    @Query("SELECT d FROM Dish d where d.description like %:name% ORDER BY d.description desc")
    List<Dish> getAllByDescription(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);

}
