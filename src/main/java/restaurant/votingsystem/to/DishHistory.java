package restaurant.votingsystem.to;

import java.util.List;

public class DishHistory {
    private String dishDescription;
    private List<MenuItemTo> menuItems;

    public DishHistory(String dishDescription, List<MenuItemTo> menuItems) {
        this.dishDescription = dishDescription;
        this.menuItems = menuItems;
    }

    public String getDish() {
        return dishDescription;
    }

    public void setDish(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public List<MenuItemTo> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemTo> menuItems) {
        this.menuItems = menuItems;
    }

}
