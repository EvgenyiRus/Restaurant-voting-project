package restaurant.votingsystem.to;

import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

public class DishHistory extends BaseTo{
    private Dish dish;
    private List<MenuItem> menuItems;

    public DishHistory(Dish dish, List<MenuItem> menuItems) {
        this.dish = dish;
        this.menuItems = menuItems;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}
