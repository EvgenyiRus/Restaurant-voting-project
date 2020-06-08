package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.repository.DishRepository;

import java.util.List;

@Service
public class DishService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishRepository repository;

    public Dish get(int id) {
        log.info("get dish {} ", id);
        return repository.findById(id);
    }

    public Dish save(Dish dish) {
        log.info("{} was added", dish);
        return repository.save(dish);
    }

//    @Cacheable("dishes")
    public List<Dish> getAll(String description) {
        log.info("get all dishes that the description contains {}",description);
        return repository.getAllByDescription(description);
    }

    public int delete(int id) {
        log.info("dish with id={} was deleted", id);
        return repository.delete(id);
    }
}
