package restaurant.votingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.votingsystem.model.Vote;
import restaurant.votingsystem.repository.VoteRepository;

@Service
public class VoteService {

    @Autowired
    VoteRepository voteRepository;

    public Vote findVoteByUser(int userId, int voteId) {
        return voteRepository.findById(voteId)
                .filter(vote ->
                        vote.getUser().getId() == userId).orElse(null);
    }
}
