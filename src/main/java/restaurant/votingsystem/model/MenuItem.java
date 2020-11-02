package restaurant.votingsystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "menu_items", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_create", "restaurant_id", "dish_id"},
        name = " menus_items_idx")})
public class MenuItem implements HasId {
    @Id
    @SequenceGenerator(name = "MENU_ITEMS_SEQ", sequenceName = "MENU_ITEMS_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_ITEMS_SEQ")
    private Integer id;

    @Column(name = "date_create", columnDefinition = "DATE default now")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Fetch(FetchMode.JOIN)
    private Dish dish;

    @Column(name = "price", nullable = false, columnDefinition = "Decimal default 0")
    @Range(min = 0, max = 99999)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Fetch(FetchMode.JOIN)
    private Restaurant restaurant;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "dish_id")
    private Integer dishId;

    public MenuItem() {
    }

    public MenuItem(Integer id, LocalDate date, BigDecimal price, Dish dish, Restaurant restaurant) {
        this.id = id;
        this.date = date;
        this.dish = dish;
        this.price = price.setScale(2);
        this.restaurant = restaurant;
    }

    public MenuItem(Integer id, LocalDate date, BigDecimal price, Dish dish) {
        this.id = id;
        this.date = date;
        this.dish = dish;
        this.price = price.setScale(2);
    }

    public MenuItem(Integer id, LocalDate date, BigDecimal price, Restaurant restaurant) {
        this.id = id;
        this.date = date;
        this.price = price.setScale(2);
        this.restaurant = restaurant;
    }

    public MenuItem(Integer id, LocalDate date, BigDecimal price, Integer dishId, Integer restaurantId) {
        this.id = id;
        this.date = date;
        this.price = price.setScale(2);
        this.dishId = dishId;
        this.restaurantId = restaurantId;
    }

    public MenuItem(Integer id, BigDecimal price, Dish dish, Restaurant restaurant) {
        this.id = id;
        this.price = price.setScale(2);
        this.dish = dish;
        this.restaurant = restaurant;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {

        this.price = price.setScale(2);
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
