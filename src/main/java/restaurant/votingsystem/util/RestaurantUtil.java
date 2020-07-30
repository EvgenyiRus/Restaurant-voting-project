package restaurant.votingsystem.util;

import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.to.DishTo;
import restaurant.votingsystem.to.MenuItemTo;
import restaurant.votingsystem.to.RestaurantTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public RestaurantUtil() {
    }

    public static List<RestaurantTo> getRestaurantsMenus(Collection<MenuItem> restaurantsMenuItems) {
        Map<Restaurant, List<MenuItem>> allMenuItemsGroupingByRestaurant = restaurantsMenuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getRestaurant));

        List<RestaurantTo> restaurantTos = new ArrayList<>();
        for (Map.Entry<Restaurant, List<MenuItem>> entry : allMenuItemsGroupingByRestaurant.entrySet()) {
            List<MenuItemTo> menuItemTos=getMenusByRestaurant(entry.getValue());

            restaurantTos.add(new RestaurantTo(
                    entry.getKey().getId(),
                    entry.getKey().getName(),
                    menuItemTos));
        }
        return restaurantTos;
    }

    public static List<MenuItemTo> getMenusByRestaurant(Collection<MenuItem> restaurantsMenuItems) {
        List<MenuItemTo> menuItemTos=restaurantsMenuItems.stream()
                .map(menuItem ->
                        new MenuItemTo(
                                menuItem.getId(),
                                menuItem.getDate(),
                                menuItem.getPrice(),
                                new DishTo(menuItem.getDish().getId(),menuItem.getDish().getDescription())
                        )).collect(Collectors.toList());
        return menuItemTos;
    }

}
