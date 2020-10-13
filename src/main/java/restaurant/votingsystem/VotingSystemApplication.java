package restaurant.votingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:db/hsqldb.properties")
public class VotingSystemApplication {

	public static void main(String[] args) {
	    SpringApplication.run(VotingSystemApplication.class, args);
	}

}
