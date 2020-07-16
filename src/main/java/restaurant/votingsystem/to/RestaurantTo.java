package restaurant.votingsystem.to;

import java.util.List;

public class RestaurantTo extends BaseTo {
    private String name;
    private List<MenuItemTo> menuItems;

    public RestaurantTo(Integer id, String name, List<MenuItemTo> menuItems) {
        super(id);
        this.name = name;
        this.menuItems = menuItems;
    }

    public RestaurantTo(Integer id,String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<MenuItemTo> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemTo> menuItems) {
        this.menuItems = menuItems;
    }
}
