package restaurant.votingsystem.to;

import java.util.List;

public class RestaurantTo extends BaseTo {
    private String name;
    private List<DishTo> dishes;

    public RestaurantTo(Integer id, String name, List<DishTo> dishes) {
        super(id);
        this.name = name;
        this.dishes = dishes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DishTo> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishTo> dishes) {
        this.dishes = dishes;
    }

}
