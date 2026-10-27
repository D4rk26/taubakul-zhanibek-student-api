package kz.taubakul.studentapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

/**
 * Common JSON body for all error responses.
 * "errors" (field -> message) is present only for validation errors.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path,
		Map<String, String> errors) {
}
