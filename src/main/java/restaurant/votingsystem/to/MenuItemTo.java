package restaurant.votingsystem.to;

import java.time.LocalDate;

public class MenuItemTo extends BaseTo{
    private LocalDate date;
    private double price;
    private RestaurantTo restaurant;
    private DishTo dish;

    public MenuItemTo(Integer id, LocalDate date, double price, DishTo dish) {
        super(id);
        this.date = date;
        this.price = price;
        this.dish = dish;
    }

    public MenuItemTo(Integer id, LocalDate date, double price, RestaurantTo restaurant) {
        super(id);
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RestaurantTo getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantTo restaurant) {
        this.restaurant = restaurant;
    }

    public DishTo getDish() { return dish; }

    public void setDish(DishTo dish) { this.dish = dish; }
}
