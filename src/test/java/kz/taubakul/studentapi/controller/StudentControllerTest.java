package kz.taubakul.studentapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kz.taubakul.studentapi.entity.Student;
import kz.taubakul.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the REST API: full Spring context, H2 database (profile "test").
 * Every test runs in a transaction that is rolled back, so tests do not affect each other.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentControllerTest {

	private static final String STUDENT_JSON = """
			{
			  "firstName": "Zhanibek",
			  "lastName": "Taubakul",
			  "email": "zhanibek.taubakul.test@example.com",
			  "groupName": "DevOps-2026",
			  "course": 3
			}
			""";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentRepository repository;

	@Autowired
	private BuildProperties buildProperties;

	private Student saveStudent(String email) {
		return repository.save(new Student("Nurlan", "Taubakul", email, "DevOps-2026", 2));
	}

	@Test
	void createStudentReturns201WithLocation() throws Exception {
		mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(STUDENT_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", notNullValue()))
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.firstName").value("Zhanibek"))
				.andExpect(jsonPath("$.lastName").value("Taubakul"))
				.andExpect(jsonPath("$.email").value("zhanibek.taubakul.test@example.com"))
				.andExpect(jsonPath("$.course").value(3));
	}

	@Test
	void getAllStudentsReturnsSeededData() throws Exception {
		mockMvc.perform(get("/api/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[*].email", hasItem("taubakulzhanibek@gmail.com")));
	}

	@Test
	void getStudentById() throws Exception {
		Student saved = saveStudent("nurlan.taubakul@example.com");

		mockMvc.perform(get("/api/students/{id}", saved.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(saved.getId()))
				.andExpect(jsonPath("$.firstName").value("Nurlan"))
				.andExpect(jsonPath("$.lastName").value("Taubakul"));
	}

	@Test
	void getMissingStudentReturns404WithJsonError() throws Exception {
		mockMvc.perform(get("/api/students/{id}", 999999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Student with id 999999 not found"))
				.andExpect(jsonPath("$.path").value("/api/students/999999"));
	}

	@Test
	void deleteStudentReturns204AndStudentIsGone() throws Exception {
		Student saved = saveStudent("delete.taubakul@example.com");

		mockMvc.perform(delete("/api/students/{id}", saved.getId()))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/students/{id}", saved.getId()))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteMissingStudentReturns404() throws Exception {
		mockMvc.perform(delete("/api/students/{id}", 999999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void invalidStudentReturns400WithFieldErrors() throws Exception {
		String invalid = """
				{
				  "firstName": "",
				  "lastName": "Taubakul",
				  "email": "not-an-email",
				  "groupName": "DevOps-2026",
				  "course": 7
				}
				""";

		mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(invalid))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Validation failed"))
				.andExpect(jsonPath("$.errors.firstName").exists())
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.course").value("course must be between 1 and 4"));
	}

	@Test
	void duplicateEmailReturns409() throws Exception {
		// Seeded on startup; the check ignores letter case
		String duplicate = STUDENT_JSON.replace("zhanibek.taubakul.test@example.com", "TaubakulZhanibek@gmail.com");

		mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(duplicate))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").value("Student with email taubakulzhanibek@gmail.com already exists"));
	}

	@Test
	void updateStudentReturns200WithNewData() throws Exception {
		Student saved = saveStudent("update.taubakul@example.com");
		String update = """
				{
				  "firstName": "Zhanibek",
				  "lastName": "Taubakul",
				  "email": "zhanibek.updated@example.com",
				  "groupName": "DevOps-2027",
				  "course": 4
				}
				""";

		mockMvc.perform(put("/api/students/{id}", saved.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(update))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(saved.getId()))
				.andExpect(jsonPath("$.firstName").value("Zhanibek"))
				.andExpect(jsonPath("$.email").value("zhanibek.updated@example.com"))
				.andExpect(jsonPath("$.groupName").value("DevOps-2027"))
				.andExpect(jsonPath("$.course").value(4));

		mockMvc.perform(get("/api/students/{id}", saved.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("zhanibek.updated@example.com"));
	}

	@Test
	void updateStudentCanKeepOwnEmail() throws Exception {
		Student saved = saveStudent("same.taubakul@example.com");
		String update = STUDENT_JSON.replace("zhanibek.taubakul.test@example.com", "same.taubakul@example.com");

		mockMvc.perform(put("/api/students/{id}", saved.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(update))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("same.taubakul@example.com"))
				.andExpect(jsonPath("$.firstName").value("Zhanibek"));
	}

	@Test
	void updateMissingStudentReturns404() throws Exception {
		mockMvc.perform(put("/api/students/{id}", 999999)
						.contentType(MediaType.APPLICATION_JSON)
						.content(STUDENT_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Student with id 999999 not found"));
	}

	@Test
	void updateWithInvalidDataReturns400() throws Exception {
		Student saved = saveStudent("invalid.taubakul@example.com");
		String invalid = STUDENT_JSON.replace("\"course\": 3", "\"course\": 0")
				.replace("zhanibek.taubakul.test@example.com", "wrong-email");

		mockMvc.perform(put("/api/students/{id}", saved.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(invalid))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.course").value("course must be between 1 and 4"));
	}

	@Test
	void updateWithEmailOfAnotherStudentReturns409() throws Exception {
		Student saved = saveStudent("conflict.taubakul@example.com");
		// taubakulzhanibek@gmail.com belongs to the seeded student
		String update = STUDENT_JSON.replace("zhanibek.taubakul.test@example.com", "taubakulzhanibek@gmail.com");

		mockMvc.perform(put("/api/students/{id}", saved.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(update))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Student with email taubakulzhanibek@gmail.com already exists"));
	}

	@Test
	void nonNumericIdReturns400() throws Exception {
		mockMvc.perform(get("/api/students/abc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.path").value("/api/students/abc"));
	}

	@Test
	void infoContainsAuthorName() throws Exception {
		mockMvc.perform(get("/api/info"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.project").value("taubakul-zhanibek-student-api"))
				.andExpect(jsonPath("$.author").value("Zhanibek Taubakul"))
				// version must be the one from build.gradle (via build-info.properties)
				.andExpect(jsonPath("$.version").value(buildProperties.getVersion()));
	}

	@Test
	void healthEndpointIsUp() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void createdLocationPointsToNewStudent() throws Exception {
		String location = mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(STUDENT_JSON.replace("zhanibek.taubakul.test", "location.taubakul")))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/api/students/")))
				.andReturn().getResponse().getHeader("Location");

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("location.taubakul@example.com"));
	}
}
