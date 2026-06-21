package kr.seenby.hidden_bussan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HiddenBussanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiddenBussanApplication.class, args);
	}

}
