package kz.taubakul.studentapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for creating (and later updating) a student.
 * Validation rules are checked by @Valid in the controller.
 */
public record StudentRequest(
		@NotBlank(message = "firstName is required")
		String firstName,

		@NotBlank(message = "lastName is required")
		String lastName,

		@NotBlank(message = "email is required")
		@Email(message = "email must be a valid email address")
		String email,

		@NotBlank(message = "groupName is required")
		String groupName,

		@NotNull(message = "course is required")
		@Min(value = 1, message = "course must be between 1 and 4")
		@Max(value = 4, message = "course must be between 1 and 4")
		Integer course) {
}
