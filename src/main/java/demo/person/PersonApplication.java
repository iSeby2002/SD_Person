package demo.person;

import demo.person.dtos.RegisterDTO;
import demo.person.entities.Person;
import demo.person.services.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PersonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonApplication.class, args);
	}

	@Bean
	CommandLineRunner init(PersonService personService) {
		return args -> {
			// Person
			List<Person> admins = personService.findAdmins();
			if(admins.isEmpty()) {
				RegisterDTO registerDTO = new RegisterDTO("Damian Mihai Sebastian", "admin", "admin", "admin");
				personService.createAdmin(registerDTO);
			}
		};
	}
}
