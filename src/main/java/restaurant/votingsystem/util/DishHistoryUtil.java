package restaurant.votingsystem.util;

import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.to.DishHistory;
import restaurant.votingsystem.to.MenuItemTo;
import restaurant.votingsystem.to.RestaurantTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DishHistoryUtil {

    public DishHistoryUtil() {
    }

    public static List<DishHistory> getHistoryDish(Collection<MenuItem> restaurantsMenuItems) {
        Map<Dish, List<MenuItem>> allMenuItemsGroupingByDish = restaurantsMenuItems.stream().
                collect(Collectors.groupingBy(MenuItem::getDish));

        List<DishHistory> dishHistory = new ArrayList<>();
        for (Map.Entry<Dish, List<MenuItem>> entry : allMenuItemsGroupingByDish.entrySet()) {
            List<MenuItemTo> menuItemTos=entry.getValue().stream()
                    .map(menuItem ->
                            new MenuItemTo(
                                    menuItem.getId(),
                                    menuItem.getDate(),
                                    menuItem.getPrice(),
                                    new RestaurantTo(
                                            menuItem.getRestaurant().getId(),
                                            menuItem.getRestaurant().getName())))
                    .collect(Collectors.toList());

            dishHistory.add(new DishHistory(
                    entry.getKey().getDescription(),
                    menuItemTos));
        }
        return dishHistory;
    }
}
