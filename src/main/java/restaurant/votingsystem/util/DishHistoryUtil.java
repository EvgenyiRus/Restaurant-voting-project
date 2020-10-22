package restaurant.votingsystem.util;

import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DishHistoryUtil {

    public DishHistoryUtil() {
    }

    public static List<MenuItem> getHistoryDish(Collection<MenuItem> menuItems) {
        return menuItems.stream().map(DishHistoryUtil::CreateMenuItemWithoutDish)
                .collect(Collectors.toList());
    }

    private static MenuItem CreateMenuItemWithoutDish(MenuItem menuItem) {
        return new MenuItem(
                menuItem.getId(),
                menuItem.getDate(),
                menuItem.getPrice(),
                menuItem.getRestaurant()
        );
    }
}
