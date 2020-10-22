package restaurant.votingsystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalTime;

@SpringBootApplication
@PropertySource("classpath:db/hsqldb.properties")
public class VotingSystemApplication {

	@Value("${voteTime.hours}")
	private static int hours;

	@Value("${voteTime.minutes}")
	private static int minutes;

	@Value("${voteTime.seconds}")
	private static int seconds;

	public static LocalTime TIME_VOTE=LocalTime.of(hours, minutes, seconds);

	public static void main(String[] args) {
		SpringApplication.run(VotingSystemApplication.class, args);
	}
}
