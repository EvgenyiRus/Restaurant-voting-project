package restaurant.votingsystem.to;

import restaurant.votingsystem.model.Restaurant;

import java.time.LocalDate;

public class MenuItemTo extends BaseTo{
    private LocalDate date;
    private double price;
    private RestaurantTo restaurant;

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
}
