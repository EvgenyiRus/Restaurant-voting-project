package restaurant.votingsystem.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.config.VoteTime;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.web.Restaurant.RestaurantController;
import restaurant.votingsystem.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
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

    static {
        VoteTime.TIME_VOTE = LocalTime.of(23, 59, 59);
    }

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    RestaurantController restaurantController;

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
        assertNotNull(restaurantController.getMenus());
    }

    @Test
    void getMenuItemByRestaurant() throws Exception {
        int idMenuItem = MENU_ITEMS_RESTAURANT.get(0).getId();
        perform(MockMvcRequestBuilders
                .get(REST_URL + "{id}/menus/{id}", RESTAURANT.getId(), idMenuItem)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(MENU_ITEMS_RESTAURANT.get(0))));
    }

    @Test
    void getVotesForRestaurants() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + "{id}/votes", RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER_USER.contentJson(RESTAURANT_VOTES));
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
    void createMenuItem() throws Exception {
        MenuItem newMenuItem = new MenuItem(null, LocalDate.now(), 100, DISH4.getId(), RESTAURANT.getId());
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + RESTAURANT.getId() + "/menus")
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
    void createVote() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT, USER);
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + RESTAURANT.getId() + "/votes")
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(newVote))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.date").exists());
        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.findById(newId).orElseThrow(), newVote);
    }

    @Test
    void createDuplicateVote() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT, VOTED_USER);
        perform(MockMvcRequestBuilders
                .post(REST_URL + RESTAURANT.getId() + "/votes")
                .with(userHttpBasic(VOTED_USER))
                .content(JsonUtil.writeValue(newVote))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("DATA_ERROR"));
    }

    @Test
    @Transactional
    void createOverTimeVote() throws Exception {
        VoteTime.TIME_VOTE = LocalTime.of(00,00,01);
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT, USER);
        perform(MockMvcRequestBuilders
                .post(REST_URL + RESTAURANT.getId() + "/votes")
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(newVote))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("WRONG_REQUEST"));
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

    @Test
    void deleteMenuItem() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + "{id}/menus/{idMenuItem}", RESTAURANT.getId(), MENU_ITEMS_RESTAURANT.get(0).getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> restaurantRepository.findById(MENU_ITEMS_RESTAURANT.get(0).getId()).orElseThrow());
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL + RESTAURANT.getId() + "/menus")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> menuItemRepository.getMenuOnDateByRestaurant(RESTAURANT.getId(), LocalDate.now()).orElseThrow());
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
    @Transactional
    void updateMenuItem() throws Exception {
        MenuItem editMenuItem = new MenuItem(MENU_ITEMS_RESTAURANT.get(0).getId(), LocalDate.now(), 1000, DISH4);
        perform(MockMvcRequestBuilders
                .put(REST_URL + RESTAURANT.getId() + "/menus/{id}", editMenuItem.getId())
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(editMenuItem))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.findById(editMenuItem.getId()).orElse(null), editMenuItem);
    }

    @Test
    @Transactional
    void updateVote() throws Exception {
        Vote editVote = new Vote(RESTAURANT_VOTES.get(0).getId(), LocalDate.now(), RESTAURANT2, VOTED_USER);
        perform(MockMvcRequestBuilders
                .put(REST_URL + RESTAURANT2.getId() + "/votes")
                .with(userHttpBasic(VOTED_USER))
                .content(JsonUtil.writeValue(editVote))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        VOTE_MATCHER.assertMatch(voteRepository.findById(editVote.getId()).orElse(null), editVote);
    }
}