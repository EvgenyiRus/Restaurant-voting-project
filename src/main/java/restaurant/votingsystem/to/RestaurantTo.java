package restaurant.votingsystem.to;

import restaurant.votingsystem.model.Dish;

import java.util.List;
import java.util.Objects;

public class RestaurantTo extends BaseTo {

    private String name;

    private List<Dish> dishes;

    public RestaurantTo(Integer id, String name, List<Dish> dishes) {
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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return name.equals(that.name) &&
                dishes.equals(that.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dishes);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "name='" + name + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}
