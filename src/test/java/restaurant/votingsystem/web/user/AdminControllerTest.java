package restaurant.votingsystem.web.user;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import restaurant.votingsystem.model.AbstractBaseEntity;
import restaurant.votingsystem.model.Role;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

@SpringBootTest
@Sql(scripts = "classpath:db/data.sql", config = @SqlConfig(encoding = "UTF-8"))
class AdminControllerTest {

    @Autowired
    AdminController adminController;

    @Autowired
    UserRepository userRepository;

    @Test
    void getAll() {
    }

    @Test
    void get() {
    }

    @Test
    void getVotes() {
    }

    @Test
    void delete() {
    }

    @Test
    void createWithLocation() {
        User createdUser = new User(AbstractBaseEntity.START_SEQ+1, "testName", "TESTEmail@mail.ru","password");
        adminController.createWithLocation(createdUser);
        User newUser = userRepository.findById(createdUser.getId()).orElse(null);
        assertNotNull(newUser);
        assertEquals(newUser.getEmail(), "testemail@mail.ru");
        assertTrue(CoreMatchers.is(newUser.getRoles()).matches(Collections.singleton(Role.USER)));
    }

    @Test
    void update() {
    }
}