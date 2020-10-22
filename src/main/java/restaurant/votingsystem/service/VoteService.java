package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.VoteRepository;
import restaurant.votingsystem.util.VoteUtil;
import restaurant.votingsystem.util.exception.OverTimeVoteException;
import restaurant.votingsystem.web.user.AuthorizedUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static restaurant.votingsystem.VotingSystemApplication.TIME_VOTE;

@Service
public class VoteService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteRepository voteRepository;

    //get votes
    public List<Vote> getAllForRestaurants(int id, LocalDate localDate) {
        log.info("Get users who was voted for restaurant with id={} today", id);
        List<Vote> votes = voteRepository.getAllVotesByRestaurant(id, localDate.now());
        return VoteUtil.getVotedUsers(votes);
    }

    //Add vote
    public Vote createVote(int restaurantId, AuthorizedUser authUser) {
        Vote vote = new Vote();
        log.info("User {} voted for restaurant with id={}", authUser.getUsername(), restaurantId);
        vote.setRestaurantId(restaurantId);
        vote.setUserId(authUser.getId());
        voteRepository.save(vote);
        return vote;
    }

    //Edit vote
    @Transactional
    public void updateVote(int restaurantId, AuthorizedUser authUser) {
        log.info("User {} changed vote", authUser.getUsername());
        if (LocalTime.now().isAfter(TIME_VOTE)) {
            throw new OverTimeVoteException();
        }
        Vote editVote = voteRepository.getVoteByUserToDay(authUser.getId(), LocalDate.now()).orElseThrow();
        editVote.setRestaurantId(restaurantId);
        voteRepository.save(editVote);
    }
}
