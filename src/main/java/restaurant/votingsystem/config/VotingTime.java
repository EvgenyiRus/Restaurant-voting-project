package restaurant.votingsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalTime;

@Configuration
@PropertySource("classpath:vote.properties")
public class VotingTime {
    @Bean
    VoteTime voteTime() {
        return new VoteTime();
    }

    public static class VoteTime {
        @Value("${voteTime.hours}")
        private int hours;

        @Value("${voteTime.minutes}")
        private int minutes;

        @Value("${voteTime.seconds}")
        private int seconds;

        public LocalTime getVoteTime() {
            return LocalTime.of(hours, minutes, seconds);
        }
    }
}