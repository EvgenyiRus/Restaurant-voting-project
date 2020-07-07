package restaurant.votingsystem.util;

import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.to.DishHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DishHistoryUtil {
    public DishHistoryUtil() {
    }

    public static List<DishHistory> getHistoryDish(Collection<MenuItem> restaurantsMenuItems) {
        Map<Dish, List<MenuItem>> allMenuItemsGroupingByDish =
                restaurantsMenuItems.stream().
                        collect(Collectors.groupingBy(MenuItem::getDish));

        List<DishHistory> dishHistoryTos = new ArrayList<>();
        for (Map.Entry<Dish, List<MenuItem>> entry : allMenuItemsGroupingByDish.entrySet()) {
            dishHistoryTos.add(new DishHistory(entry.getKey(),entry.getValue()));
        }
        return dishHistoryTos;
    }

}
