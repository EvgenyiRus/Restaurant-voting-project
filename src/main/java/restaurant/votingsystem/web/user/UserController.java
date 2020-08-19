package restaurant.votingsystem.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.UserRepository;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.util.UserUtil;
import restaurant.votingsystem.util.VoteUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    static final String REST_URL = "/profile";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("Get user with id={}", authUser.getId());
        return userRepository.findById(authUser.getId()).orElseThrow();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody User user) {
        log.info("New user '{}' was added", user.getEmail());
        user=UserUtil.prepareToSave(user,user.getRoles());
        userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("User '{}' was updated", authUser.getUsername());
        user=UserUtil.prepareToSave(user,user.getRoles());
        user.setId(authUser.getId());
        userRepository.save(user);
    }

    @GetMapping(value = "/votes/history",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getVotes(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("Get votes user with id={}", authUser.getId());
        return VoteUtil.getRestaurantsByVotedUser(voteRepository.getAllVotesByUser(authUser.getId()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        userRepository.delete(authUser.getId());
    }
}
