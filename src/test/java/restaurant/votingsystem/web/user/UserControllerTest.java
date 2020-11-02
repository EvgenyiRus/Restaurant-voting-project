package restaurant.votingsystem.web.user;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import restaurant.votingsystem.model.Role;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.repository.UserRepository;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.util.UserUtil;
import restaurant.votingsystem.web.AbstractControllerTest;
import restaurant.votingsystem.web.json.JsonUtil;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restaurant.votingsystem.TestData.*;
import static restaurant.votingsystem.TestUtil.readFromJson;
import static restaurant.votingsystem.TestUtil.userHttpBasic;

class UserControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserController.REST_URL + '/';

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void register() throws Exception {
        User newUser = new User(null, "newName", "newEmail@mail.ru");
        newUser = UserUtil.prepareToSave(newUser, Collections.singleton(Role.USER), passwordEncoder);
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, "newPassword")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.findById(newId).orElseThrow(), newUser);
    }

    @Test
    void update() throws Exception {
        User editUser = new User(USER.getId(), "editName", "edit@mail.ru");
        perform(MockMvcRequestBuilders
                .put(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(editUser, "editPassword"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
        USER_MATCHER.assertMatch(userRepository.findById(editUser.getId()).orElseThrow(), editUser);
    }

    @Test
    void getVotes() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + "votes/history")
                .with(userHttpBasic(VOTED_USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(USER_VOTES)));

        Assert.assertNotEquals(voteRepository.getAllByUser(USER.getId()), USER_VOTES);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThrows(NoSuchElementException.class,
                () -> userRepository.findById(USER.getId()).orElseThrow());
    }
}