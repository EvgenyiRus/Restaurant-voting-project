package restaurant.votingsystem.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.service.VoteService;
import restaurant.votingsystem.util.VoteUtil;
import restaurant.votingsystem.web.user.AuthorizedUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    public static final String REST_URL = "/restaurants/{restaurantId}";

    @Autowired
    private VoteService voteService;

    @GetMapping("/votes")
    public List<Vote> getAllByRestaurantToDay(@PathVariable int restaurantId) {
        List<Vote> votes = voteService.getByRestaurantForDate(restaurantId, LocalDate.now());
        return VoteUtil.getVotedUsers(votes);
    }

    @PostMapping(value = "/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> create(@PathVariable int restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        Vote vote = voteService.create(restaurantId, authUser);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RestaurantController.REST_URL + "/{id}")
                .buildAndExpand(vote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }

    @PutMapping(value = "/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        voteService.update(restaurantId, authUser);
    }
}
