package restaurant.votingsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import restaurant.votingsystem.model.Dish;

import java.util.List;

@Repository
public class DishRestRepository  {

    @Autowired
    DishRepository dishRepository;

    private List<Dish> getAll(){
        return dishRepository.findAll();
    }
}
