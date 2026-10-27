package kz.taubakul.studentapi.service;

import java.util.List;
import kz.taubakul.studentapi.dto.StudentRequest;
import kz.taubakul.studentapi.dto.StudentResponse;
import kz.taubakul.studentapi.entity.Student;
import kz.taubakul.studentapi.exception.DuplicateEmailException;
import kz.taubakul.studentapi.exception.StudentNotFoundException;
import kz.taubakul.studentapi.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for students: the controller calls the service,
 * the service uses the repository.
 */
@Service
@Transactional(readOnly = true)
public class StudentService {

	private final StudentRepository repository;

	public StudentService(StudentRepository repository) {
		this.repository = repository;
	}

	public List<StudentResponse> findAll() {
		return repository.findAll(Sort.by("id")).stream()
				.map(StudentResponse::from)
				.toList();
	}

	public StudentResponse findById(Long id) {
		return StudentResponse.from(getStudent(id));
	}

	@Transactional
	public StudentResponse create(StudentRequest request) {
		String email = normalizeEmail(request.email());
		if (repository.existsByEmailIgnoreCase(email)) {
			throw new DuplicateEmailException(email);
		}
		Student student = new Student(
				request.firstName().trim(),
				request.lastName().trim(),
				email,
				request.groupName().trim(),
				request.course());
		return StudentResponse.from(repository.save(student));
	}

	@Transactional
	public void delete(Long id) {
		repository.delete(getStudent(id));
	}

	// Shared lookup for read, delete (and update): throws 404 if the id does not exist
	private Student getStudent(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new StudentNotFoundException(id));
	}

	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase();
	}
}
