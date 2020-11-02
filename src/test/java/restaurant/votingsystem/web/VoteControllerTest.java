package restaurant.votingsystem.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.web.json.JsonUtil;
import restaurant.votingsystem.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static restaurant.votingsystem.TestData.*;
import static restaurant.votingsystem.TestUtil.readFromJson;
import static restaurant.votingsystem.TestUtil.userHttpBasic;
import static restaurant.votingsystem.VotingSystemApplication.TIME_VOTE;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';

    static {
        TIME_VOTE = LocalTime.of(23, 59, 59);
    }

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + "{id}/votes", RESTAURANT.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER_USER.contentJson(RESTAURANT_VOTES));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "{id}/votes", RESTAURANT.getId()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT, USER);
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "{id}/votes", RESTAURANT.getId())
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
    void createDuplicate() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT, VOTED_USER);
        perform(MockMvcRequestBuilders
                .post(REST_URL + "{id}/votes", RESTAURANT.getId())
                .with(userHttpBasic(VOTED_USER))
                .content(JsonUtil.writeValue(newVote))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"));
    }

    @Test
    @Transactional
    void updateOverTimeVoteException() throws Exception {
        TIME_VOTE = LocalTime.of(11, 00, 01);
        Vote editVote = new Vote(USER_VOTES.get(0).getId(), LocalDate.now(), RESTAURANT, USER);
        perform(MockMvcRequestBuilders
                .put(REST_URL + "{id}/votes", RESTAURANT.getId())
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(editVote))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("WRONG_REQUEST"));
    }

    @Test
    @Transactional
    void update() throws Exception {
        TIME_VOTE = LocalTime.of(23, 59, 59);
        Vote editVote = new Vote(RESTAURANT_VOTES.get(0).getId(), LocalDate.now(), RESTAURANT2, VOTED_USER);
        perform(MockMvcRequestBuilders
                .put(REST_URL + "{id}/votes", RESTAURANT2.getId())
                .with(userHttpBasic(VOTED_USER))
                .content(JsonUtil.writeValue(editVote))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        VOTE_MATCHER.assertMatch(voteRepository.findById(editVote.getId()).orElse(null), editVote);
    }
}
