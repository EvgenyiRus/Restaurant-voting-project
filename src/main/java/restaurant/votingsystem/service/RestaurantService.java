package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.*;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.util.VoteUtil;
import restaurant.votingsystem.util.exception.NotContainException;
import restaurant.votingsystem.util.exception.NotSuchElementException;
import restaurant.votingsystem.util.exception.OverTimeVoteException;
import restaurant.votingsystem.web.user.AuthorizedUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static restaurant.votingsystem.config.VoteTime.TIME_VOTE;

@Service
public class RestaurantService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.getAll();
    }

    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant with id={} ", id);
        return restaurantRepository.findById(id).orElseThrow(
                () -> new NotSuchElementException(new String[]{"restaurant", String.valueOf(id)}));
    }

    public List<RestaurantTo> getAllMenus(LocalDate localDate) {
        log.info("Get menus of all restaurants");
        List<MenuItem> menuItems =menuItemRepository.getAllMenus(localDate).orElseThrow();
        return RestaurantUtil.getRestaurantsMenus(menuItems);
    }

    public List<MenuItem> getAllMenusByRestaurant(int id, LocalDate localDate) {
        log.info("Get menu from restaurant with id={}", id);
        List<MenuItem> menuItems = menuItemRepository.getMenuOnDateByRestaurant(id, localDate).orElseThrow();
        return RestaurantUtil.getMenusByRestaurant(menuItems);
    }

    public MenuItem getMenuItemByRestaurant(int id, int menuItemId) {
        log.info("Get menu item with id={}", menuItemId);
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
        return menuitem;
    }

    //get votes
    public List<Vote> getAllForRestaurants(int id, LocalDate localDate) {
        log.info("Get users who was voted for restaurant with id={} today", id);
        List<Vote> votes = voteRepository.getAllVotesByRestaurant(id, localDate.now()).orElseThrow();
        return VoteUtil.getVotedUsers(votes);
    }

    //delete restaurant
    public void delete(int id) {
        log.info("Restaurant with id={} was deleted", id);
        restaurantRepository.delete(id);
    }

    //delete menuItem
    public void deleteMenuItem(int id, int menuItemId) {
        log.info("MenuItem with id={} was deleted", menuItemId);
        menuItemRepository.delete(id, menuItemId, LocalDate.now());
    }

    //delete all menuItems for restaurant
    public void deleteMenu(int id) {
        log.info("Menu for restaurant with id={} was deleted", id);
        menuItemRepository.deleteAllForRestaurant(id, LocalDate.now());
    }

    //Add restaurant
    public Restaurant create(Restaurant restaurant) {
        log.info("New restaurant {} was added", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    //Add menuItem
    public MenuItem createMenuItem(MenuItem menuItem, int id) {
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
        //Response without restaurant
        return new MenuItem(created.getId(), created.getDate(), created.getPrice(), dish);
    }

    //Edit Restaurant
    public void update(Restaurant restaurant, int id) {
        log.info("Restaurant with id={} was updated", id);
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    //Edit MenuItem
    public void updateMenuItem(MenuItem menuItem, int restaurantId, int menuItemId) {
        log.info("Menuitem with id={} for restaurant with id={} was updated", menuItemId, restaurantId);
        if (findMenuItemByRestaurant(menuItemId, restaurantId) == null) {
            throw new NotContainException(
                    new String[]{
                            "restaurant with id"
                            , String.valueOf(restaurantId)
                            , "menu item with id"
                            , String.valueOf(menuItemId)});
        }
        menuItem.setId(menuItemId);
        menuItem.setRestaurantId(restaurantId);
        menuItemRepository.save(menuItem);
    }

    //Add vote
    public Vote createVote(int id, AuthorizedUser authUser) {
        Vote vote = new Vote();
        log.info("User {} voted for restaurant with id={}", authUser.getUsername(), id);
        vote.setRestaurantId(id);
        vote.setUserId(authUser.getId());
        voteRepository.save(vote);
        return vote;
    }

    //Edit vote
    public void updateVote(int id, AuthorizedUser authUser) {
        log.info("User {} changed vote", authUser.getUsername());
        if (LocalTime.now().isAfter(TIME_VOTE)) {
            throw new OverTimeVoteException();
        }
        Vote editVote = voteRepository.getVoteByUserToDay(authUser.getId(), LocalDate.now()).orElseThrow();
        editVote.setRestaurantId(id);
        voteRepository.save(editVote);
    }

    private MenuItem findMenuItemByRestaurant(int menuItemId, int restaurantId) {
        return menuItemRepository.findById(menuItemId)
                .filter(menuItem ->
                        menuItem.getRestaurant().getId() == restaurantId).orElse(null);
    }
}
