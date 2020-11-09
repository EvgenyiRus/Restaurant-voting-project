package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@CacheConfig(cacheNames={"restaurants"})
public class RestaurantService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Transactional
    @Cacheable(cacheNames = "menuItems", key = "#localDate")
    public List<RestaurantTo> getAllWithMenus(LocalDate localDate) {
        log.info("Get menus of all restaurants");
        List<MenuItem> menuItems = menuItemRepository.getAllMenus(localDate);
        return RestaurantUtil.getAllWithMenus(menuItems);
    }

    @Cacheable
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    @Cacheable(key = "#id")
    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant with id={} ", id);
        return restaurantRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        new String[]{"restaurant", String.valueOf(id)}, NotFoundException.NOT_FOUND_EXCEPTION));
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        log.info("Restaurant with id={} was deleted", id);
        restaurantRepository.delete(id);
    }

    @CacheEvict(allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("New restaurant {} was added", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(allEntries = true)
    public void update(Restaurant restaurant, int id) {
        log.info("Restaurant with id={} was updated", id);
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }
}
