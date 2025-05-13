package cz.cvut.fel.eduportal;

import cz.cvut.fel.eduportal.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EduportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduportalApplication.class, args);
	}
}
