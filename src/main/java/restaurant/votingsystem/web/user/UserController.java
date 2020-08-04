package restaurant.votingsystem.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    static final String REST_URL = "/users";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping(value = "/profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public User getOne(@AuthenticationPrincipal AuthorizedUser currentUser) {
        log.info("Get user with id={}", currentUser.getId());
        return userRepository.findById(currentUser.getId()).orElseThrow();
    }

//    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> createWithLocation(@Validated(View.Web.class) @RequestBody User user) {
//        User created = super.create(user);
//        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(REST_URL + "/{id}")
//                .buildAndExpand(created.getId()).toUri();
//        return ResponseEntity.created(uriOfNewResource).body(created);
//    }
//

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userRepository.delete(id);
    }
//
//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    public void update(@RequestBody User user, @PathVariable int id) throws BindException {
//        checkAndValidateForUpdate(user, id);
//        service.update(user);
//    }
//
//    @GetMapping("/by")
//    public User getByMail(@RequestParam String email) {
//        return super.getByMail(email);
//    }
//
//    @Override
//    @PatchMapping("/{id}")
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
//        super.enable(id, enabled);
//    }
}
