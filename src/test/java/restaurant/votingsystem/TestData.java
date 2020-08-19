package restaurant.votingsystem;

import restaurant.votingsystem.model.*;

import java.time.LocalDate;
import java.util.List;

public class TestData {

    public static final Restaurant RESTAURANT = new Restaurant(100014, "Каудаль");

    public static final Dish DISH = new Dish(100002, "Суп борщ");
    public static final Dish DISH1 = new Dish(100004, "Жаркое из баранины");
    public static final Dish DISH2= new Dish(100007, "Десерт мороженное");
    public static final Dish DISH3 = new Dish(100010, "Апельсиновый сок");

    public static final User USER = new User(100000, "User", "user@yandex.ru", "password", Role.USER);
    public static final User ADMIN = new User(100001, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);

    public static final List<MenuItem> MENU_ITEMS_RESTAURANT = List.of(
            new MenuItem(1, LocalDate.now(), DISH, 255, RESTAURANT),
            new MenuItem(2, LocalDate.now(), DISH1, 500.65, RESTAURANT),
            new MenuItem(3, LocalDate.now(), DISH2, 100, RESTAURANT),
            new MenuItem(4, LocalDate.now(), DISH3, 150.34, RESTAURANT)
    );

    public static final List<Vote> VOTES_USER = List.of(
            new Vote()
    );
}
