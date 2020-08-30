package restaurant.votingsystem.to;

import java.time.LocalDate;

public class MenuItemTo extends BaseTo {

    private LocalDate date;
    private double price;
    private int restaurantId;
    private int dishId;

    public MenuItemTo(Integer id, LocalDate date, int dishId, double price) {
        super(id);
        this.date = date;
        this.price = price;
        this.dishId = dishId;
    }

    public MenuItemTo(Integer id, LocalDate date, double price, int restaurantId) {
        super(id);
        this.date = date;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public MenuItemTo() {
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

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurant(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }
}
