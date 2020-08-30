package restaurant.votingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "menu_items", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "restaurant_id", "dish_id"},
        name = " menus_items_idx")})
public class MenuItem implements HasId {
    @Id
    @SequenceGenerator(name = "MENU_ITEMS_SEQ", sequenceName = "MENU_ITEMS_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_ITEMS_SEQ")
    private Integer id;

    @Column(name = "date", columnDefinition = "DATE default now")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date = LocalDate.now();

    //@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Dish dish;

    @Column(name = "price", nullable = false, columnDefinition = "Integer default 0")
    @NotNull
    @Range(min = 0, max = 99999)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Restaurant restaurant;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "dish_id")
    private Integer dishId;

    public MenuItem() {
    }

    public MenuItem(Integer id, LocalDate date, double price, Dish dish, Restaurant restaurant) {
        this.id = id;
        this.date = date;
        this.dish = dish;
        this.price = price;
        this.restaurant = restaurant;
    }

    public MenuItem(Integer id, LocalDate date, double price, Dish dish) {
        this.id = id;
        this.date = date;
        this.dish = dish;
        this.price = price;
    }

    public MenuItem(Integer id, LocalDate date, double price, Restaurant restaurant) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }

    public MenuItem(Integer id, LocalDate date, double price, Integer dishId, Integer restaurantId) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.dishId = dishId;
        this.restaurantId = restaurantId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Dish getDish() {
        return dish;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }
}
