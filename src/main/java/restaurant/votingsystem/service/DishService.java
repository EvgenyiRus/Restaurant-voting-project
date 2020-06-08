package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.repository.DishRepository;

@Service
public class DishService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishRepository repository;

    //public DishService(DishRepository dishRepository) {
    //    repository = dishRepository;
    //}

    public Dish get(int id) {
        log.info("get dish {} ", id);
        return repository.findById(id);
    }
}
