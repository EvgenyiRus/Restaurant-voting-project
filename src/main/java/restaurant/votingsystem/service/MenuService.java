package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;
import restaurant.votingsystem.util.exception.NotContainException;
import restaurant.votingsystem.util.exception.NotSuchElementException;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private DishRepository dishRepository;

    public List<RestaurantTo> getAllMenus(LocalDate localDate) {
        log.info("Get menus of all restaurants");
        List<MenuItem> menuItems = menuItemRepository.getAllMenus(localDate);
        return RestaurantUtil.getRestaurantsMenus(menuItems);
    }

    public List<MenuItem> getAllMenusByRestaurant(int id, LocalDate localDate) {
        log.info("Get menu from restaurant with id={}", id);
        List<MenuItem> menuItems = menuItemRepository.getMenuOnDateByRestaurant(id, localDate);
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

    private MenuItem findMenuItemByRestaurant(int menuItemId, int restaurantId) {
        return menuItemRepository.findById(menuItemId)
                .filter(menuItem ->
                        menuItem.getRestaurant().getId() == restaurantId).orElse(null);
    }
}
