package restaurant.votingsystem.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.web.json.JsonUtil;
import restaurant.votingsystem.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static restaurant.votingsystem.TestData.*;
import static restaurant.votingsystem.TestUtil.readFromJson;
import static restaurant.votingsystem.TestUtil.userHttpBasic;

class MenuItemControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void get() throws Exception {
        int idMenuItem = MENU_ITEMS_RESTAURANT.get(0).getId();
        perform(MockMvcRequestBuilders
                .get(REST_URL + "{restaurantId}/menus/{id}", RESTAURANT.getId(), idMenuItem)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(MENU_ITEMS_RESTAURANT.get(0))));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        MenuItem newMenuItem = new MenuItem(null, LocalDate.now(), valueOf(100), DISH4.getId(), RESTAURANT.getId());
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "{restaurantId}/menus", RESTAURANT.getId())
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newMenuItem))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.dish").exists())
                .andExpect(jsonPath("$.price").exists());
        MenuItem created = readFromJson(action, MenuItem.class);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.findById(newId).orElseThrow(), newMenuItem);
    }

    @Test
    @Transactional
    void update() throws Exception {
        MenuItem editMenuItem = new MenuItem(MENU_ITEMS_RESTAURANT.get(0).getId(), LocalDate.now(), valueOf(1000), DISH4);
        perform(MockMvcRequestBuilders
                .put(REST_URL + "{restaurantId}/menus/{id}", RESTAURANT.getId(), editMenuItem.getId())
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(editMenuItem))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.findById(editMenuItem.getId()).orElse(null), editMenuItem);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + "{restaurantId}/menus/{id}", RESTAURANT.getId(), MENU_ITEMS_RESTAURANT.get(0).getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> menuItemRepository.findById(MENU_ITEMS_RESTAURANT.get(0).getId()).orElseThrow());
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + "{restaurantId}/menus", RESTAURANT.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertTrue(menuItemRepository.getMenuOnDateByRestaurant(RESTAURANT.getId(), LocalDate.now()).isEmpty());
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
