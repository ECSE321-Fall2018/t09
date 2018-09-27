package ca.mcgill.ecse321.rideshare9;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ca.mcgill.ecse321.rideshare9.entity.Role;
import ca.mcgill.ecse321.rideshare9.repository.RoleRepository;

@SpringBootApplication
public class Rideshare9Application {
	 
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(Rideshare9Application.class, args);
	}
	@Bean
    public CommandLineRunner demoData(RoleRepository rr) {
        return args -> { 
    		Role admin = new Role(); 
    		admin.setRole("ROLE_ADMIN");
    		Role driver = new Role(); 
    		driver.setRole("ROLE_DRIVER");
    		Role passenger = new Role(); 
    		passenger.setRole("ROLE_PASSENGER");
    		rr.save(admin); 
    		rr.save(driver);
    		rr.save(passenger);
        }; 
    }
}