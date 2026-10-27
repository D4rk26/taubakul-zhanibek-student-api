package kz.taubakul.studentapi.repository;

import kz.taubakul.studentapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for students. Spring Data JPA generates the implementation
 * (save, findAll, findById, deleteById, ...) at runtime.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

	boolean existsByEmailIgnoreCase(String email);

	// Is the email used by any other student? (used on update)
	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
