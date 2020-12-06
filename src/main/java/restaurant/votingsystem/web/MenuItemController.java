package restaurant.votingsystem.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.service.MenuItemService;
import restaurant.votingsystem.util.RestaurantUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuItemController {
    public static final String REST_URL = "/restaurants/{restaurantId}";

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/menus")
    public List<MenuItem> getMenusByRestaurant(@PathVariable int restaurantId) {
        List<MenuItem> menuItems = menuItemService.getAllByRestaurant(restaurantId, LocalDate.now());
        return RestaurantUtil.getWithMenu(menuItems);
    }

    @GetMapping("/menus/{id}")
    public MenuItem get(@PathVariable int restaurantId, @PathVariable int id) {
        MenuItem menuitem = menuItemService.getByRestaurant(restaurantId, id);
        return RestaurantUtil.getMenuItem(menuitem);
    }

    @PostMapping(value = "/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> create(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId) {
        MenuItem created = menuItemService.create(menuItem, restaurantId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RestaurantController.REST_URL + "/{restaurantId}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId, @PathVariable int id) {
        menuItemService.updateMenuItem(menuItem, restaurantId, id);
    }

    @DeleteMapping("/menus/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        menuItemService.delete(restaurantId, id);
    }

    @DeleteMapping("/menus")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenuByRestaurant(@PathVariable int restaurantId) {
        menuItemService.deleteAllByRestaurant(restaurantId);
    }
}
