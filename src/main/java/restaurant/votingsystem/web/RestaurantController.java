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
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.util.exception.NotContainException;
import restaurant.votingsystem.util.exception.NotSuchElementException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static restaurant.votingsystem.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    //get restaurant
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant with id={} ", id);
        return restaurantRepository.findById(id).orElseThrow(
                () -> new NotSuchElementException(new String[]{"restaurant", String.valueOf(id)}));
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
    public List<MenuItem> getAllMenusByRestaurant(@PathVariable int id) {
        log.info("Get menu from restaurant with id={}", id);
        //todo check restaurant
        List<MenuItem> menuItems = menuItemRepository.getMenuOnDateByRestaurant(id, LocalDate.now());
        return RestaurantUtil.getMenusByRestaurant(menuItems);
    }

    //get menuitem
    @GetMapping("/{id}/menus/{menuItemId}")
    public MenuItem getMenuItemByRestaurant(@PathVariable int id, @PathVariable int menuItemId) {
        log.info("Get menu item with id={}", menuItemId);
        //todo check restaurant
        MenuItem menuitem = menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new NotSuchElementException(new String[]{"menu item", String.valueOf(menuItemId)})
        );
        if (id != menuitem.getRestaurant().id()) {
            throw new NotContainException(
                    new String[]{
                            "restaurant with id"
                            , String.valueOf(id)
                            , "menu item with id"
                            , String.valueOf(menuItemId)});
        }
        return RestaurantUtil.getMenuItemByRestaurant(menuitem);
    }

    //delete restaurant
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Restaurant with id={} was deleted", id);
        restaurantRepository.delete(id);
    }

    //delete menuItem
    @DeleteMapping("/menus/{menuItemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable int menuItemId) {
        log.info("MenuItem with id={} was deleted", menuItemId);
        menuItemRepository.delete(menuItemId);
    }

    //delete all menuItems for restaurant
    @DeleteMapping("/{id}/menus")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable int id) {
        log.info("Menu for restaurant with id={} was deleted", id);
        menuItemRepository.deleteAllForRestaurant(id);
    }

    //Add restaurant
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("New restaurant {} was added", restaurant.getName());
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //Add menuItem
    @PostMapping(value = "/{id}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        log.info("New menu item for restaurant with id={} was added", id);
        Dish dish = dishRepository.findById(menuItem.getDishId()).orElseThrow(
                () -> new NotSuchElementException(new String[]{"dish", String.valueOf(menuItem.getDishId())}));
        MenuItem created = menuItemRepository.save(
                new MenuItem(
                        menuItem.getId()
                        , LocalDate.now()
                        , menuItem.getPrice()
                        , menuItem.getDishId()
                        , id
                )
        );

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        //Response without restaurant
        return ResponseEntity.created(uriOfNewResource).body(
                new MenuItem(created.getId()
                        , created.getDate()
                        , created.getPrice()
                        , dish
                )
        );
    }

    //Edit Restaurant
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Restaurant update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("Restaurant with id={} was updated", id);
        assureIdConsistent(restaurant, id);
        restaurant.setId(id);
        Restaurant editRestaurant = restaurantRepository.save(restaurant);

        return editRestaurant;
    }

    //Edit MenuItem
    @PutMapping(value = "/{restaurantId}/menus/{menuItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateMenuItem(
            @Valid @RequestBody MenuItem menuItem,
            @PathVariable int restaurantId,
            @PathVariable int menuItemId)
    {
        log.info("Menuitem with menuItemId={} for restaurant with id={} was updated", menuItemId, restaurantId);
        if (findMenuItemByRestaurant(menuItemId, restaurantId) == null) {
            throw new NotContainException(
                    new String[]{
                            "restaurant with id"
                            , String.valueOf(restaurantId)
                            , "menu item with id"
                            , String.valueOf(menuItemId)});
        }
        //todo check dish
        menuItemRepository.update(menuItem.getDishId(), restaurantId, menuItemId, menuItem.getPrice());
    }

    //For Service
    public MenuItem findMenuItemByRestaurant(int menuItemId, int restaurantId) {
        return menuItemRepository.findById(menuItemId)
                .filter(menuItem ->
                        menuItem.getRestaurant().getId() == restaurantId).orElse(null);
    }
}
