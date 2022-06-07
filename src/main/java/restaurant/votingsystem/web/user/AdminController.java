package restaurant.votingsystem.web.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.UserRepository;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.util.UserUtil;
import restaurant.votingsystem.util.VoteUtil;
import restaurant.votingsystem.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "The Admin API", description = "Work with users")
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    static final String REST_URL = "/admin/users";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return userRepository.getAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("Get user with id={}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(new String[]{"user", String.valueOf(id)}, NotFoundException.NOT_FOUND_EXCEPTION));
    }

    @GetMapping(value = "/{id}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getVotes(@PathVariable int id) {
        log.info("Get votes user with id={}", id);
        List<Vote> userVotes = voteRepository.getAllByUser(id);
        return VoteUtil.getRestaurantsByVotedUser(userVotes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("User with id='{}' was deleted", id);
        userRepository.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("New user '{}' was added", user.getName());
        User created = UserUtil.prepareToSave(user, user.getRoles(), passwordEncoder);
        userRepository.save(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        log.info("User '{}' was updated", user.getName());
        user = UserUtil.prepareToSave(user, user.getRoles(), passwordEncoder);
        user.setId(id);
        userRepository.save(user);
    }
}
