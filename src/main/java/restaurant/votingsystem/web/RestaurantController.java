package restaurant.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.repository.UserRepository;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.to.DishTo;
import restaurant.votingsystem.to.MenuItemTo;
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

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    //get restaurant
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant with id={} ", id);
        return restaurantRepository.findById(id).orElseThrow();
        //() -> new NotFoundException("No restaurant found with id={} " + id));
    }

    //get all menus restaurants
    @GetMapping("/menus")
    public List<RestaurantTo> getAllMenus() {
        log.info("Get menus of all restaurants");
        return RestaurantUtil.getRestaurantsMenus(
                menuItemRepository.getAllMenus(LocalDate.now())
        );
    }

    //get menu restaurant
    @GetMapping("/{id}/menus")
    public List<MenuItemTo> getAllMenusByRestaurant(@PathVariable int id) {
        log.info("Get menu from restaurant with id={}", id);
        return RestaurantUtil.getMenusByRestaurant(
                menuItemRepository.getMenuOnDateByRestaurant(id, LocalDate.now())
        );
    }

    //get menuitem
    @GetMapping("/{id}/menus/{menuItemId}")
    public MenuItemTo getMenuItemByRestaurant(@PathVariable int id, @PathVariable int menuItemId) {
        log.info("Get menu item with id={}", menuItemId);
        MenuItem menuitem = menuItemRepository.findById(menuItemId).orElse(null);
        if (menuitem == null || id != menuitem.getRestaurant().id())
            return null;
        return new MenuItemTo(
                menuitem.getId(),
                menuitem.getDate(),
                new DishTo(
                        menuitem.getDish().getDescription()
                ),
                menuitem.getPrice()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Restaurant with id={} was deleted", id);
        restaurantRepository.delete(id);
    }

    @DeleteMapping("/menus/{menuItemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable int menuItemId) {
        log.info("MenuItem with id={} was deleted", menuItemId);
        menuItemRepository.delete(menuItemId);
    }

    //Add restaurant
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        log.info("New restaurant was added");
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //Add menuItem
    @PostMapping(value = "/{id}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem, @PathVariable int id) {
        log.info("New menu item for restaurant with id={} was added", id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        if (restaurant == null) return null;
        menuItem.setRestaurant(restaurant);
        menuItemRepository.save(menuItem);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(menuItem.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(menuItem);
    }

    //Edit Restaurant
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("Restaurant with id={} was updated", restaurant.getId());
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    //Edit MenuItem
    @PutMapping(value = "/{id}/menus/{menuItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateMenuItem(@RequestBody MenuItem menuItem, @PathVariable int id, @PathVariable int menuItemId) {
        log.info("Menuitem with menuItemId={} for restaurant with id={} was updated", menuItemId, id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(null);
        if (restaurant != null) {
            menuItem.setRestaurant(restaurant);
            menuItem.setId(menuItemId);
            if (findMenuItemByRestaurant(menuItem.getId(), menuItem.getRestaurant().getId()) != null)
                menuItemRepository.save(menuItem);
        }
    }

    //For Service
    public MenuItem findMenuItemByRestaurant(int menuItemId, int restaurantId) {
        return menuItemRepository.findById(menuItemId)
                .filter(menuItem ->
                        menuItem.getRestaurant().getId() == restaurantId).orElse(null);
    }

}
