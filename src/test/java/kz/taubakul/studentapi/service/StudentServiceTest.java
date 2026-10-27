package kz.taubakul.studentapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kz.taubakul.studentapi.dto.StudentRequest;
import kz.taubakul.studentapi.dto.StudentResponse;
import kz.taubakul.studentapi.entity.Student;
import kz.taubakul.studentapi.exception.DuplicateEmailException;
import kz.taubakul.studentapi.exception.StudentNotFoundException;
import kz.taubakul.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for StudentService: no Spring context, the repository is a Mockito mock.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentRepository repository;

	@InjectMocks
	private StudentService service;

	@Test
	void createRejectsDuplicateEmail() {
		StudentRequest request = new StudentRequest(
				"Zhanibek", "Taubakul", "taubakulzhanibek@gmail.com", "DevOps-2026", 3);
		when(repository.existsByEmailIgnoreCase("taubakulzhanibek@gmail.com")).thenReturn(true);

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(DuplicateEmailException.class)
				.hasMessageContaining("taubakulzhanibek@gmail.com");
		verify(repository, never()).save(any());
	}

	@Test
	void createTrimsFieldsAndLowercasesEmail() {
		StudentRequest request = new StudentRequest(
				" Zhanibek ", " Taubakul ", " Zhanibek.Taubakul@Example.com ", " DevOps-2026 ", 3);
		when(repository.existsByEmailIgnoreCase("zhanibek.taubakul@example.com")).thenReturn(false);
		when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

		StudentResponse created = service.create(request);

		assertThat(created.firstName()).isEqualTo("Zhanibek");
		assertThat(created.lastName()).isEqualTo("Taubakul");
		assertThat(created.email()).isEqualTo("zhanibek.taubakul@example.com");
		assertThat(created.groupName()).isEqualTo("DevOps-2026");
	}

	@Test
	void updateRejectsEmailOfAnotherStudent() {
		Student existing = new Student("Zhanibek", "Taubakul", "zhanibek@example.com", "DevOps-2026", 3);
		StudentRequest request = new StudentRequest(
				"Zhanibek", "Taubakul", "taubakulzhanibek@gmail.com", "DevOps-2026", 3);
		when(repository.findById(7L)).thenReturn(Optional.of(existing));
		when(repository.existsByEmailIgnoreCaseAndIdNot("taubakulzhanibek@gmail.com", 7L)).thenReturn(true);

		assertThatThrownBy(() -> service.update(7L, request))
				.isInstanceOf(DuplicateEmailException.class);
		verify(repository, never()).saveAndFlush(any());
	}

	@Test
	void findByIdThrowsWhenStudentDoesNotExist() {
		when(repository.findById(42L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.findById(42L))
				.isInstanceOf(StudentNotFoundException.class)
				.hasMessage("Student with id 42 not found");
	}
}
