package restaurant.votingsystem.web.menuItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.service.MenuItemService;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.web.restaurant.RestaurantController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/{restaurantId}/menus")
    public List<MenuItem> getMenusByRestaurant(@PathVariable int restaurantId) {
        List<MenuItem> menuItems = menuItemService.getAllByRestaurant(restaurantId, LocalDate.now());
        return RestaurantUtil.getWithMenu(menuItems);
    }

    @GetMapping("/{restaurantId}/menus/{id}")
    public MenuItem get(@PathVariable int restaurantId, @PathVariable int id) {
        MenuItem menuitem = menuItemService.getByRestaurant(restaurantId, id);
        return RestaurantUtil.getMenuItem(menuitem);
    }

    @PostMapping(value = "/{restaurantId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> create(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId) {
        MenuItem created = menuItemService.create(menuItem, restaurantId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RestaurantController.REST_URL + "/{restaurantId}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restaurantId}/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId, @PathVariable int id) {
        menuItemService.updateMenuItem(menuItem, restaurantId, id);
    }

    @DeleteMapping("/{restaurantId}/menus/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        menuItemService.delete(restaurantId, id);
    }

    @DeleteMapping("/{restaurantId}/menus")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenuByRestaurant(@PathVariable int restaurantId) {
        menuItemService.deleteAllByRestaurant(restaurantId);
    }
}
