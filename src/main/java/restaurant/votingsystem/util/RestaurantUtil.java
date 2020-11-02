package restaurant.votingsystem.util;

import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.to.RestaurantTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static List<RestaurantTo> getAllWithMenus(Collection<MenuItem> menuItems) {
        Map<Restaurant, List<MenuItem>> allMenuItemsGroupingByRestaurant = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getRestaurant));

        List<RestaurantTo> restaurantTos = new ArrayList<>();
        for (Map.Entry<Restaurant, List<MenuItem>> entry : allMenuItemsGroupingByRestaurant.entrySet()) {
            List<MenuItem> menuItemTos = getWithMenu(entry.getValue());

            restaurantTos.add(
                    new RestaurantTo(
                            entry.getKey().getId(),
                            entry.getKey().getName(),
                            menuItemTos)
            );
        }
        return restaurantTos;
    }

    public static List<MenuItem> getWithMenu(Collection<MenuItem> menuItems) {
        return menuItems.stream()
                .map(menuItem -> new MenuItem(
                        menuItem.getId(),
                        menuItem.getDate(),
                        menuItem.getPrice(),
                        menuItem.getDish())
                )
                .collect(Collectors.toList());
    }

    public static MenuItem getMenuItem(MenuItem menuItem) {
        return new MenuItem(
                menuItem.getId(),
                menuItem.getDate(),
                menuItem.getPrice(),
                menuItem.getDish());
    }
}
