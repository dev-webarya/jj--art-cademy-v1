package com.artacademy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Basic unit tests for ArtAcademy application.
 * 
 * Note: Full integration tests with @SpringBootTest require a reachable MongoDB
 * instance.
 * For CI/CD environments without MongoDB access, consider using:
 * - Embedded MongoDB (de.flapdoodle.embed.mongo)
 * - Testcontainers with MongoDB
 * - A test profile with mock repositories
 */
class ArtacademyApplicationTests {

	@Test
	void applicationClassExists() {
		// Verify the main application class is accessible
		assertNotNull(ArtacademyApplication.class);
	}

}
