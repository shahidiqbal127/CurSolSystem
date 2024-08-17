package uniproj.cursol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CursolApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursolApplication.class, args);
	}

}
