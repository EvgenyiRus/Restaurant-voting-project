package restaurant.votingsystem.util;

import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
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
        Map<Restaurant, List<MenuItem>> allMenuItemsGroupingByRestaurant =
                restaurantsMenuItems.stream()
                        .collect(Collectors.groupingBy(MenuItem::getRestaurant));

        List<RestaurantTo> restaurantTos = new ArrayList<>();
        for (Map.Entry<Restaurant, List<MenuItem>> entry : allMenuItemsGroupingByRestaurant.entrySet()) {
            List<Dish> dishes = new ArrayList<>();
            entry.getValue().stream().forEach(menuItem -> dishes.add(menuItem.getDish()));
            restaurantTos.add(new RestaurantTo(entry.getKey().getId(), entry.getKey().getName(), dishes));
        }

        return restaurantTos;
    }

}
