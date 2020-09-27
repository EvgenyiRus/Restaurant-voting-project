package restaurant.votingsystem;

import restaurant.votingsystem.model.*;
import restaurant.votingsystem.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

public class TestData {
    public static TestMatcher<MenuItem> MENU_ITEM_MATCHER = TestMatcher.usingFieldsComparator(MenuItem.class, "dish", "restaurant", "dishId", "restaurantId");
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator(Restaurant.class);
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator(Dish.class);
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsComparator(Vote.class, "user", "restaurant", "userId", "restaurantId");
    public static TestMatcher<Vote> VOTE_MATCHER_USER = TestMatcher.usingFieldsComparator(Vote.class, "user");
    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsComparator(User.class, "password", "roles", "votes");

    public static final Restaurant RESTAURANT = new Restaurant(100014, "Каудаль");
    public static final Restaurant RESTAURANT2 = new Restaurant(100015, "В гостях");
    public static final Restaurant RESTAURANT4 = new Restaurant(100016, "У Марифа");
    public static final Restaurant RESTAURANT3 = new Restaurant(100017, "Солнечный");

    public static final Dish DISH = new Dish(100002, "Суп борщ");
    public static final Dish DISH1 = new Dish(100004, "Жаркое из баранины");
    public static final Dish DISH2 = new Dish(100007, "Десерт мороженное");
    public static final Dish DISH3 = new Dish(100010, "Апельсиновый сок");
    public static final Dish DISH4 = new Dish(100009, "Трюфель");

    public static final User VOTED_USER = new User(100000, "User", "user@yandex.ru", "password", Role.USER);
    public static final User USER = new User(100020, "User", "gosha@gmail.com", "gosha", Role.USER);
    public static final User ADMIN = new User(100001, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);

    //for RESTAURANT
    public static final List<MenuItem> MENU_ITEMS_RESTAURANT = List.of(
            new MenuItem(1, LocalDate.now(), 255, DISH),
            new MenuItem(2, LocalDate.now(), 500.65, DISH1),
            new MenuItem(3, LocalDate.now(), 100, DISH2),
            new MenuItem(4, LocalDate.now(), 150.34, DISH3)
    );

    public static final List<Vote> RESTAURANT_VOTES = List.of(
            new Vote(1, LocalDate.now(), VOTED_USER),
            new Vote(2, LocalDate.now(), ADMIN)
    );

    public static final List<Vote> VOTED_USER_VOTES = List.of(
            new Vote(1, LocalDate.now(), RESTAURANT),
            new Vote(3, LocalDate.now().minusDays(1), RESTAURANT),
            new Vote(8, LocalDate.now().minusDays(2), RESTAURANT3)
    );

    //for DISH
    public static final List<MenuItem> DISH_HISTORY = List.of(
            new MenuItem(1, LocalDate.now(), 255, RESTAURANT),
            new MenuItem(9, LocalDate.now(), 267, RESTAURANT4),
            new MenuItem(18, LocalDate.now().minusDays(1), 255.5, RESTAURANT)
    );

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}
