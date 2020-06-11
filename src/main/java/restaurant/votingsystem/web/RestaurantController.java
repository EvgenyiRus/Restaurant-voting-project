package restaurant.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.service.RestaurantService;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;

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

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}/menus")
    public List<Dish> getWithMenu(@PathVariable int id) {
        log.info("Get the menu from the restaurant with id={}", id);
        return menuItemRepository.getMenuOnDateByRestaurant(id, LocalDate.now());
    }

    @GetMapping("/menus")
    public List<RestaurantTo> getAllMenusTo() {
        log.info("Get the menu of all restaurants");
        return RestaurantUtil.getRestaurantsMenus(menuItemRepository.getAllMenus(LocalDate.now()));
    }
}
