package restaurant.votingsystem.config;

import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
public class VoteTime {
    public static LocalTime TIME_VOTE = LocalTime.of(11, 00, 00);
}
