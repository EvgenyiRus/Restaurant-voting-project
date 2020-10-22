package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.util.exception.NotSuchElementException;

import java.util.List;

@Service
public class RestaurantService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant with id={} ", id);
        return restaurantRepository.findById(id).orElseThrow(
                () -> new NotSuchElementException(new String[]{"restaurant", String.valueOf(id)}));
    }

    //delete restaurant
    public void delete(int id) {
        log.info("Restaurant with id={} was deleted", id);
        restaurantRepository.delete(id);
    }

    //Add restaurant
    public Restaurant create(Restaurant restaurant) {
        log.info("New restaurant {} was added", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    //Edit Restaurant
    public void update(Restaurant restaurant, int id) {
        log.info("Restaurant with id={} was updated", id);
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }
}
