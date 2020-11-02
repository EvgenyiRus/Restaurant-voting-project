package restaurant.votingsystem.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.web.json.JsonUtil;
import restaurant.votingsystem.web.menuItem.MenuItemController;
import restaurant.votingsystem.web.restaurant.RestaurantController;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static restaurant.votingsystem.TestData.*;
import static restaurant.votingsystem.TestUtil.readFromJson;
import static restaurant.votingsystem.TestUtil.userHttpBasic;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantController restaurantController;

    @Autowired
    MenuItemController menuItemController;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + RESTAURANT.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(RESTAURANT)));
    }

    @Test
    void getNotExistent() throws Exception {
        int id = 0;
        perform(MockMvcRequestBuilders
                .get(REST_URL + id)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NoSuchElementException.class))
                .andExpect(jsonPath("$.details").value("Not found restaurant with id " + id))
                .andExpect(jsonPath("$.type").value("DATA_NOT_FOUND"));

        assertNull(restaurantRepository.findById(id).orElse(null));
        assertThrows(NoSuchElementException.class,
                () -> restaurantRepository.findById(id).orElseThrow());
    }

    @Test
    void getWithMenu() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + "{id}/menus", RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(MENU_ITEMS_RESTAURANT)));
    }

    @Test
    void getAllMenus() {
        assertNotNull(restaurantController.getAllWithMenus());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @Transactional
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "newRestaurant");
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newRestaurant))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value("newRestaurant"));

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(newId).orElseThrow(), newRestaurant);
    }

    @Test
    @Transactional
    void update() throws Exception {
        Restaurant editRestaurant = new Restaurant(RESTAURANT.getId(), "editName");
        perform(MockMvcRequestBuilders
                .put(REST_URL + editRestaurant.getId())
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(editRestaurant))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(editRestaurant.getId()).orElse(null), editRestaurant);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + "{id}", RESTAURANT.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> restaurantRepository.findById(RESTAURANT.getId()).orElseThrow());
    }

    @Test
    void deleteWithoutAccess() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + "{id}", RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}