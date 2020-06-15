package br.com.pipa.studios.scoreuser;

import br.com.pipa.studios.scoreuser.controller.ScoreUserController;
import br.com.pipa.studios.scoreuser.domain.dto.request.ScoreUserRequestDTO;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import br.com.pipa.studios.scoreuser.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScoreUserIntegrationTests {

	private  static final int MAX_RECORDS = 20000;

	@Autowired
	private ScoreUserController scoreUserController;

	private ScoreService scoreService;

	private ScoreUserRequestDTO scoreUserRequestDTO;


	@BeforeEach
	void setUp() {
		scoreService = new ScoreService();
		this.scoreService.removeAll();
		scoreUserRequestDTO = new ScoreUserRequestDTO(1L, 100L);
	}

	@Test
	public void whenSaveNew_thenReturnCreatedStatusOk() {
		ResponseEntity result = scoreUserController.save(scoreUserRequestDTO);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void whenGetPositionNotFound_theReturnUserEmpty() {
		ResponseEntity<ScoreUserResponseDTO> result = scoreUserController.getUser(-1L);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getUserId()).isNull();
		assertThat(result.getBody().getPosition()).isNull();
		assertThat(result.getBody().getPoints()).isNull();
	}

	@Test
	public void whenGetFound_thenReturnUserPoinsAndPosition() {
		ScoreUserRequestDTO scoreUserFoundRequestDTO = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(), 1L);
		ResponseEntity resultSave = scoreUserController.save(scoreUserFoundRequestDTO);
		assertThat(resultSave.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		ResponseEntity<ScoreUserResponseDTO> result = scoreUserController.getUser(scoreUserFoundRequestDTO.getUserId());
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getUserId()).isEqualTo(scoreUserFoundRequestDTO.getUserId());
		assertThat(result.getBody().getPoints()).isEqualTo(scoreUserFoundRequestDTO.getPoints());
	}

	@Test
	public void whenGetFoundUser_theReturnFirstPositionOk() {
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
	public void whenGetRecords_thenReturnEmptyArray() {
		ResponseEntity<List<ScoreUserResponseDTO>> result = scoreUserController.getHighScoreList();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().size()).isEqualTo(0);
	}

	@Test
	public void whenGetrecords_thenReturnMaxSizeHighScore() {
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
