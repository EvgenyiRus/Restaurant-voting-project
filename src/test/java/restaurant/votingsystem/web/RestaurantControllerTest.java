package restaurant.votingsystem.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.InternalException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.util.exception.NotFoundException;
import restaurant.votingsystem.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static restaurant.votingsystem.TestData.*;
import static restaurant.votingsystem.TestUtil.userHttpBasic;

@SpringBootTest
@Sql(scripts = "classpath:db/data.sql", config = @SqlConfig(encoding = "UTF-8"))
class RestaurantControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    RestaurantController restaurantController;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "newRestaurant");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/restaurants")
                .with(userHttpBasic(USER))
                .content(objectMapper.writeValueAsString(newRestaurant))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value("newRestaurant"));
    }

    @Test
    void createMenuItem() {
    }

    @Test
    void getAll() {
        List<Restaurant> restaurants = restaurantController.getAll();
        assertNotNull(restaurants);
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/restaurants/{id}", RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(RESTAURANT)));
    }

    @Test
    void getNonExistent() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/restaurants/{id}",1111)
//                .with(userHttpBasic(USER)))
//                .andExpect(status().is5xxServerError())
//                .andDo(print());
//                //.andExpect(status().isBadRequest())
////                .andExpect(errorType(VALIDATION_ERROR))
//                //.andExpect(detailMessage(EXCEPTION_DUPLICATE_DATETIME))
//                //.andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException));
////                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NoSuchElementException.class))
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InternalException))
//                .andExpect(result -> assertEquals("No restaurant found with id="+1111, result.getResolvedException().getMessage()));
        assertNull(restaurantRepository.findById(1111).orElse(null));
        assertThrows(NoSuchElementException.class,
                () -> restaurantRepository.findById(1111).orElseThrow());
    }


    @Test
    void getWithMenu() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/restaurants/{id}/menus",RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(MENU_ITEMS_RESTAURANT)));

//        List<MenuItemTo> menuItemList = restaurantController.getAllMenusByRestaurant(RESTAURANT.getId());
//        assertNotNull(menuItemList);
//        assertEquals(MENU_ITEMS_RESTAURANT.size(), menuItemList.size());
    }

    @Test
    void getAllMenus() {
        assertNotNull(restaurantController.getAllMenus());
    }

    @Test
    void getMenuItemByRestaurant() {
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/restaurants/{id}", RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> restaurantRepository.findById(RESTAURANT.getId()).orElseThrow());
    }

    @Test
    void deleteMenuItem() {
    }

    @Test
    void update() throws Exception {
        Restaurant editRestaurant= new Restaurant(100014, "editName");
        mockMvc.perform(MockMvcRequestBuilders
                .put("/restaurants/{id}", editRestaurant.getId())
                .with(userHttpBasic(USER))
                .content(objectMapper.writeValueAsString(editRestaurant))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertEquals(restaurantRepository.findById(editRestaurant.getId()).orElse(null), RESTAURANT);
    }

    @Test
    void updateMenuItem() {
    }

    @Test
    void findMenuItemByRestaurant() {
    }
}