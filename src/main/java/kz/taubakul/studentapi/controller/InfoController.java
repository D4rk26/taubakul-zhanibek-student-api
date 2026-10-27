package kz.taubakul.studentapi.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /api/info - project name, author and version.
 * The version comes from build.gradle via build-info.properties.
 */
@RestController
public class InfoController {

	static final String PROJECT = "taubakul-zhanibek-student-api";
	static final String AUTHOR = "Taubakul Zhanibek Nurlanuly";

	private final String version;

	public InfoController(ObjectProvider<BuildProperties> buildProperties) {
		BuildProperties build = buildProperties.getIfAvailable();
		this.version = (build != null) ? build.getVersion() : "unknown";
	}

	@GetMapping("/api/info")
	public Map<String, String> info() {
		Map<String, String> info = new LinkedHashMap<>();
		info.put("project", PROJECT);
		info.put("author", AUTHOR);
		info.put("version", version);
		return info;
	}
}
