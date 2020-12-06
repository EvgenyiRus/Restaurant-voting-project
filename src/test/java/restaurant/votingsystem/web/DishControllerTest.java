package restaurant.votingsystem.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.web.json.JsonUtil;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restaurant.votingsystem.TestData.*;
import static restaurant.votingsystem.TestUtil.readFromJson;
import static restaurant.votingsystem.TestUtil.userHttpBasic;

class DishControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DishController.REST_URL + '/';

    @Autowired
    DishRepository dishRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + DISH.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(DISH)));
    }

    @Test
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + "{id}/history", DISH.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(DISH_HISTORY)));
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
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + DISH.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> dishRepository.findById(DISH.getId()).orElseThrow());
    }

    @Test
    @Transactional
    void update() throws Exception {
        Dish editDish = new Dish(DISH.getId(), "editDish");
        perform(MockMvcRequestBuilders
                .put(REST_URL + DISH.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(editDish))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        DISH_MATCHER.assertMatch(dishRepository.findById(editDish.getId()).orElseThrow(), editDish);
    }

    @Test
    @Transactional
    void createWithLocation() throws Exception {
        Dish newDish = new Dish(null, "newName");
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish))
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(newDish))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);

        DISH_MATCHER.assertMatch(dishRepository.findById(newDish.getId()).orElseThrow(), newDish);
    }
}