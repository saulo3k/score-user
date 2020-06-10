package br.com.pipa.studios.scoreuser;

import br.com.pipa.studios.scoreuser.config.TestRedisConfiguration;
import br.com.pipa.studios.scoreuser.controller.ScoreUserController;
import br.com.pipa.studios.scoreuser.domain.dto.request.ScoreUserRequestDTO;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import br.com.pipa.studios.scoreuser.repository.ScoreUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
class ScoreUserIntegrationTests {

	private  static final int MAX_RECORDS = 20000;

	@Autowired
	private ScoreUserController scoreUserController;

	private ScoreUserRequestDTO scoreUserRequestDTO;


	@BeforeEach
	void setUp() {
		scoreUserRequestDTO = new ScoreUserRequestDTO(1L, 100L);
	}

	@Test
	public void when_save_expect_created_status() {
		ResponseEntity result = scoreUserController.save(scoreUserRequestDTO);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void when_get_notfound_user_expect_empty() {
		ResponseEntity<ScoreUserResponseDTO> result = scoreUserController.getUser(-1L);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNull();
	}

	@Test
	public void when_getfound_user_expect_ok() {
		ScoreUserRequestDTO scoreUserFoundRequestDTO = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(), 1L);
		ResponseEntity resultSave = scoreUserController.save(scoreUserFoundRequestDTO);
		assertThat(resultSave.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		ResponseEntity<ScoreUserResponseDTO> result = scoreUserController.getUser(scoreUserFoundRequestDTO.getUserId());
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getUserId()).isEqualTo(scoreUserFoundRequestDTO.getUserId());
		assertThat(result.getBody().getPoints()).isEqualTo(scoreUserFoundRequestDTO.getPoints());
	}

	@Test
	public void when_getfound_user_expect_first_position_ok() {
		ScoreUserRequestDTO scoreUserFoundRequestDTO = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(), Long.MAX_VALUE);
		ResponseEntity resultSave = scoreUserController.save(scoreUserFoundRequestDTO);
		assertThat(resultSave.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		ResponseEntity<ScoreUserResponseDTO> result = scoreUserController.getUser(scoreUserFoundRequestDTO.getUserId());
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getUserId()).isEqualTo(scoreUserFoundRequestDTO.getUserId());
		assertThat(result.getBody().getPoints()).isEqualTo(scoreUserFoundRequestDTO.getPoints());
		assertThat(result.getBody().getPosition()).isEqualTo(1);
	}

	@Test
	public void when_getrecords_expect_empty_array_ok() {
		ResponseEntity<List<ScoreUserResponseDTO>> result = scoreUserController.getHighScoreList();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().size()).isEqualTo(0);
	}

	@Test
	public void when_getrecords_expect_max20k_records_array_ok() {
		for (int i = 0; i <= MAX_RECORDS; i++){
			ScoreUserRequestDTO scoreUserFoundRequestDTO = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(), Long.valueOf(i));
			ResponseEntity resultSave = scoreUserController.save(scoreUserFoundRequestDTO);
			assertThat(resultSave.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		}
		ResponseEntity<List<ScoreUserResponseDTO>> result = scoreUserController.getHighScoreList();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().size()).isEqualTo(MAX_RECORDS);
	}
}
