package restaurant.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("Get  restaurant with id={} ", id);
        return restaurantRepository.findById(id).orElseThrow();
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}/menus")
    public List<Dish> getWithMenu(@PathVariable int id) {
        log.info("Get menu from the restaurant with id={}", id);
        return menuItemRepository.getMenuOnDateByRestaurant(id, LocalDate.now());
    }

    @GetMapping("/menus")
    public List<RestaurantTo> getAllMenusTo() {
        log.info("Get menus of all restaurants");
        return RestaurantUtil.getRestaurantsMenus(menuItemRepository.getAllMenus(LocalDate.now()));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Restaurant with id={} was deleted", id);
        restaurantRepository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("Restaurant with id={} was updated", restaurant.getId());
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant restaurant) {
        log.info("New restaurant '{}' was added");
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
