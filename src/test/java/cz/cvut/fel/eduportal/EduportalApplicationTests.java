package cz.cvut.fel.eduportal;

import cz.cvut.fel.eduportal.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EduportalApplicationTests {
	@Autowired
	TestRestTemplate restTemplate;


	@Test
	void testFindByIdOnEmptyTable() {
		String url = "/users/1";
		ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
