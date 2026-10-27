package kz.taubakul.studentapi.config;

import java.util.List;
import kz.taubakul.studentapi.entity.Student;
import kz.taubakul.studentapi.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Inserts individual test data on startup, but only when the table is empty,
 * so restarting the application never creates duplicates.
 */
@Configuration
public class DataInitializer {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	@Bean
	CommandLineRunner seedStudents(StudentRepository repository) {
		return args -> {
			if (repository.count() > 0) {
				log.info("Students table already has data, seeding skipped");
				return;
			}
			repository.saveAll(List.of(
					new Student("Zhanibek", "Taubakul", "taubakulzhanibek@gmail.com", "DevOps-2026", 3),
					new Student("Aruzhan", "Serikova", "aruzhan.serikova@example.com", "DevOps-2026", 3),
					new Student("Dias", "Nurlanov", "dias.nurlanov@example.com", "SE-2027", 2)));
			log.info("Seeded 3 students (author: Taubakul Zhanibek)");
		};
	}
}
