package restaurant.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.repository.UserRepository;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.util.VoteUtil;
import restaurant.votingsystem.web.user.AuthorizedUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/votes";
    static final LocalTime TIME_VOTE = LocalTime.of(11, 00, 00);
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<Vote> getAll() {
        log.info("Get votes all users today");
        return VoteUtil.getVotedUsers(
                voteRepository.getAllVotesByUsersOnDay(LocalDate.now())
        );
    }

    @GetMapping("/restaurants/{id}")
    public List<Vote> getAllVotes(@PathVariable int id) {
        log.info("Get users who vote for restaurant with id={}", id);
        return VoteUtil.getVotedUsers(
                voteRepository.getAllVotesByRestaurant(id, LocalDate.now())
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote, @AuthenticationPrincipal AuthorizedUser authUser) {
        if (LocalTime.now().isBefore(TIME_VOTE)) {
            log.info("User {} voted for restaurant with id={}", authUser.getUsername(), vote.getRestaurant().getId());
            Restaurant restaurant = restaurantRepository.findById(vote.getRestaurant().getId()).orElseThrow();
            User user = userRepository.findById(authUser.getId()).orElseThrow();
            if (restaurant == null || user == null) return null;
            vote.setUser(user);
            vote.setRestaurant(restaurant);
            voteRepository.save(vote);
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(vote.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateVote(@RequestBody Vote vote, @AuthenticationPrincipal AuthorizedUser authUser) {
        if (LocalTime.now().isBefore(TIME_VOTE)) {
            log.info("User {} changed vote with id={}", authUser.getUsername(), vote.getRestaurant().getId());
            Vote editVote = voteRepository.getVoteByUserToDay(authUser.getId(), LocalDate.now());
            if (editVote != null) {
                editVote.setRestaurant(vote.getRestaurant());
                voteRepository.save(editVote);
            }
        }
    }
}
