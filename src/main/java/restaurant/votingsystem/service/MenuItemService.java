package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"menuItems"})
public class MenuItemService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private DishRepository dishRepository;

    @Cacheable(key = "#restaurantId")
    public List<MenuItem> getAllByRestaurant(int restaurantId, LocalDate localDate) {
        log.info("Get menu from restaurant with id={}", restaurantId);
        List<MenuItem> menuItems = menuItemRepository.getMenuOnDateByRestaurant(restaurantId, localDate);
        return RestaurantUtil.getWithMenu(menuItems);
    }

    @Cacheable(key = "#id")
    public MenuItem getByRestaurant(int restaurantId, int id) {
        log.info("Get menu item with id={}", id);
        return menuItemRepository.findById(id).filter(menuItem ->
                menuItem.getRestaurantId() == restaurantId).orElseThrow(
                () -> new NotFoundException(
                        new String[]{"restaurant with id=", String.valueOf(restaurantId), "menu item with id=", String.valueOf(id)},
                        NotFoundException.NOT_CONTAIN_EXCEPTION));
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public MenuItem create(MenuItem menuItem, int restaurantId) {
        log.info("New menu item for restaurant with id={} was added", restaurantId);
        Dish dish = dishRepository.findById(menuItem.getDishId()).orElseThrow(
                () -> new NotFoundException(
                        new String[]{"dish", String.valueOf(menuItem.getDishId())}, NotFoundException.NOT_FOUND_EXCEPTION));
        MenuItem created = menuItemRepository.save(
                new MenuItem(menuItem.getId(), LocalDate.now(), menuItem.getPrice(), menuItem.getDishId(), restaurantId)
        );
        //Response without restaurant
        return new MenuItem(created.getId(), created.getDate(), created.getPrice(), dish);
    }

    @CacheEvict(allEntries = true)
    public void delete(int restaurantId, int id) {
        log.info("MenuItem with id={} was deleted", id);
        menuItemRepository.delete(restaurantId, id, LocalDate.now());
    }

    @CacheEvict(allEntries = true)
    public void deleteAllByRestaurant(int restaurantId) {
        log.info("Menu for restaurant with id={} was deleted", restaurantId);
        menuItemRepository.deleteAllForRestaurant(restaurantId, LocalDate.now());
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void updateMenuItem(MenuItem menuItem, int restaurantId, int id) {
        log.info("Menuitem with id={} for restaurant with id={} was updated", id, restaurantId);
        if (getByRestaurant(restaurantId, id) != null) {
            menuItem.setId(id);
            menuItem.setRestaurantId(restaurantId);
            menuItemRepository.save(menuItem);
        }
    }
}
