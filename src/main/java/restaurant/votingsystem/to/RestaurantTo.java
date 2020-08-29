package restaurant.votingsystem.to;

import restaurant.votingsystem.model.MenuItem;

import java.util.List;

public class RestaurantTo extends BaseTo {
    private String name;
    private List<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, List<MenuItem> menuItems) {
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


    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
