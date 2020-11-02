package restaurant.votingsystem.util;

import restaurant.votingsystem.model.MenuItem;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DishHistoryUtil {

    private DishHistoryUtil() {
    }

    public static List<MenuItem> getHistory(Collection<MenuItem> menuItems) {
        Map<LocalDate, List<MenuItem>> m = menuItems.stream().collect(Collectors.groupingBy(MenuItem::getDate));

        return menuItems.stream().map(DishHistoryUtil::getMenuItemWithoutDish)
                .collect(Collectors.toList());
    }

    public static List<MenuItem> get(Collection<MenuItem> menuItems) {
        return menuItems.stream()
                .map(menuItem -> new MenuItem(
                        menuItem.getId(),
                        menuItem.getPrice(),
                        menuItem.getDish(),
                        menuItem.getRestaurant())
                )
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
