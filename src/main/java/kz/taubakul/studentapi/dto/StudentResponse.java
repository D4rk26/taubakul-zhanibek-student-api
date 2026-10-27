package kz.taubakul.studentapi.dto;

import kz.taubakul.studentapi.entity.Student;

/**
 * Student data returned by the API.
 */
public record StudentResponse(
		Long id,
		String firstName,
		String lastName,
		String email,
		String groupName,
		Integer course) {

	public static StudentResponse from(Student student) {
		return new StudentResponse(
				student.getId(),
				student.getFirstName(),
				student.getLastName(),
				student.getEmail(),
				student.getGroupName(),
				student.getCourse());
	}
}
