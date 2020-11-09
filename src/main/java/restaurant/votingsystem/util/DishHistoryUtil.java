package restaurant.votingsystem.util;

import restaurant.votingsystem.model.MenuItem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DishHistoryUtil {

    private DishHistoryUtil() {
    }

    public static List<MenuItem> getHistory(Collection<MenuItem> menuItems) {
        return menuItems.stream().map(DishHistoryUtil::getMenuItemWithoutDish)
                .collect(Collectors.toList());
    }

    private static MenuItem getMenuItemWithoutDish(MenuItem menuItem) {
        return new MenuItem(
                menuItem.getId(),
                menuItem.getDate(),
                menuItem.getPrice(),
                menuItem.getRestaurant()
        );
    }
}
