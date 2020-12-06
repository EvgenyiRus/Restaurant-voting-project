package restaurant.votingsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Configuration
@PropertySource("classpath:vote.properties")
public class VotingTime {
    @Bean
    VoteTime voteTime() {
        return new VoteTime();
    }

    public static class VoteTime {
        @Value("${voteTime.time}")
        @DateTimeFormat(pattern = "HH:mm:ss")
        private LocalTime voteTime;

        public LocalTime getVoteTime() {
            return voteTime;
        }
    }
}