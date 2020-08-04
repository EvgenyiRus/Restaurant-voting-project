package restaurant.votingsystem.web.dish;

import restaurant.votingsystem.to.MenuItemTo;

import java.util.List;

public class DishHistory {
    private String description;
    private List<MenuItemTo> menuItems;

    public DishHistory(String description, List<MenuItemTo> menuItems) {
        this.description = description;
        this.menuItems = menuItems;
    }

    public String getDish() {
        return description;
    }

    public void setDish(String description) {
        this.description = description;
    }

    public List<MenuItemTo> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemTo> menuItems) {
        this.menuItems = menuItems;
    }

}
