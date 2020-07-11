package restaurant.votingsystem.to;

import restaurant.votingsystem.model.Dish;

import java.util.List;

public class DishHistory extends BaseTo{
    private Dish dish;
    private List<MenuItemTo> menuItems;

    public DishHistory(Dish dish, List<MenuItemTo> menuItems) {
        this.dish = dish;
        this.menuItems = menuItems;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public List<MenuItemTo> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemTo> menuItems) {
        this.menuItems = menuItems;
    }

}
