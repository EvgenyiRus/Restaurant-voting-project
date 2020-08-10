package restaurant.votingsystem.util;

import restaurant.votingsystem.model.User;
import restaurant.votingsystem.model.Vote;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {

    public VoteUtil() {
    }

    public static List<Vote> getVotedUsers(Collection<Vote> votedUsers) {
        List<Vote> votedUsersTos = votedUsers.stream()
                .map(votedUser ->
                        new Vote(
                                votedUser.getId(),
                                votedUser.getDate(),
                                new User(
                                        votedUser.getUser().getId(),
                                        votedUser.getUser().getName(),
                                        votedUser.getUser().getEmail()
                                )
                        )).collect(Collectors.toList());
        return votedUsersTos;
    }

    public static List<Vote> getRestaurantsByVotedUser(Collection<Vote> votedUsers) {
        List<Vote> votedUsersTos = votedUsers.stream()
                .map(votedUser ->
                        new Vote(
                                votedUser.getId(),
                                votedUser.getDate(),
                                votedUser.getRestaurant()
                        )).collect(Collectors.toList());
        return votedUsersTos;
    }
}
