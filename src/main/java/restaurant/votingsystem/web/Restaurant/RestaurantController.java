package restaurant.votingsystem.web.Restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.service.RestaurantService;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.util.VoteUtil;
import restaurant.votingsystem.web.user.AuthorizedUser;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String REST_URL = "/restaurants";

    @Autowired
    RestaurantService restaurantService;

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return restaurantService.get(id);
    }

    @GetMapping("/menus")
    public List<RestaurantTo> getMenus() {
        return restaurantService.getAllMenus(LocalDate.now());
    }

    @GetMapping("/{id}/menus")
    public List<MenuItem> getMenusByRestaurant(@PathVariable int id) {
        List<MenuItem> menuItems = restaurantService.getAllMenusByRestaurant(id, LocalDate.now());
        return RestaurantUtil.getMenusByRestaurant(menuItems);
    }

    @GetMapping("/{id}/menus/{menuItemId}")
    public MenuItem getMenuItemByRestaurant(@PathVariable int id, @PathVariable int menuItemId) {
        MenuItem menuitem = restaurantService.getMenuItemByRestaurant(id, menuItemId);
        return RestaurantUtil.getMenuItemByRestaurant(menuitem);
    }

    @GetMapping("/{id}/votes")
    public List<Vote> getVotesForRestaurants(@PathVariable int id) {
        List<Vote> votes = restaurantService.getAllForRestaurants(id, LocalDate.now());
        return VoteUtil.getVotedUsers(votes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        restaurantService.delete(id);
    }

    @DeleteMapping("/{id}/menus/{menuItemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable int id, @PathVariable int menuItemId) {
        restaurantService.deleteMenuItem(id, menuItemId);
    }

    //delete menu for restaurant
    @DeleteMapping("/{id}/menus")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable int id) {
        restaurantService.deleteMenu(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = restaurantService.create(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/{id}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        MenuItem created = restaurantService.createMenuItem(menuItem, id);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        restaurantService.update(restaurant, id);
    }

    @PutMapping(value = "/{id}/menus/{menuItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateMenuItem(
            @Valid @RequestBody MenuItem menuItem,
            @PathVariable int id,
            @PathVariable int menuItemId) {
        restaurantService.updateMenuItem(menuItem, id, menuItemId);
    }

    @Transactional
    @PostMapping(value = "/{id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createVote(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        Vote vote = restaurantService.createVote(id, authUser);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping(value = "/{id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateVote(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        restaurantService.updateVote(id, authUser);
    }
}
