package br.com.pipa.studios.scoreuser.service;

import br.com.pipa.studios.scoreuser.domain.dto.request.ScoreUserRequestDTO;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ScoreUnitTest {

    private ScoreService scoreService;

    private ScoreUserRequestDTO scoreUser;

    public static final int MAX_RECORDS = 20000;

    @Before
    public void setup() {
        scoreService = new ScoreService();
        scoreUser = new ScoreUserRequestDTO(1l, 1l);
        this.scoreService.removeAll();
    }

    @Test
    public void whenPassPoints_thenReturnSumTotalPoints() {
        Long points  = scoreService.sumTotalPoints(100l,100l);
        assertThat(points).isEqualTo(200);
    }

    @Test
    public void whenSaveNewUser_thenReturnUserPointsPosition() {
        long id = UUID.randomUUID().getMostSignificantBits();
        scoreUser.setUserId(id);
        scoreService.save(scoreUser);
        ScoreUserResponseDTO positionScore = scoreService.getPositionScoreList(scoreUser.getUserId());
        assertThat(positionScore.getPoints()).isEqualTo(1);
        assertThat(positionScore.getUserId()).isEqualTo(id);
    }

    @Test
    public void whenSaveMultipleUsers_thenReturnPositionAndPointsFoundInOrder() {
        long id = UUID.randomUUID().getMostSignificantBits();
        scoreUser.setUserId(id);
        scoreService.save(scoreUser);
        scoreService.save(scoreUser);
        scoreService.save(scoreUser);
        scoreService.save(scoreUser);
        ScoreUserResponseDTO positionScore = scoreService.getPositionScoreList(scoreUser.getUserId());
        assertThat(positionScore.getPosition()).isEqualTo(1);
        assertThat(positionScore.getPoints()).isEqualTo(4);
        assertThat(positionScore.getUserId()).isEqualTo(id);

        long id2 = UUID.randomUUID().getMostSignificantBits();

        ScoreUserRequestDTO scoreUser2 = new ScoreUserRequestDTO(id2, 2l);

        scoreService.save(scoreUser2);
        scoreService.save(scoreUser2);
        scoreService.save(scoreUser2);
        scoreService.save(scoreUser2);
        ScoreUserResponseDTO positionScore2 = scoreService.getPositionScoreList(scoreUser2.getUserId());
        assertThat(positionScore2.getPosition()).isEqualTo(1);
        assertThat(positionScore2.getPoints()).isEqualTo(8);
        assertThat(positionScore2.getUserId()).isEqualTo(id2);

        positionScore = scoreService.getPositionScoreList(scoreUser.getUserId());
        assertThat(positionScore.getPosition()).isEqualTo(2);

        long id3 = UUID.randomUUID().getMostSignificantBits();

        ScoreUserRequestDTO scoreUser3 = new ScoreUserRequestDTO(id3, Long.MAX_VALUE);

        scoreService.save(scoreUser3);
        positionScore = scoreService.getPositionScoreList(scoreUser3.getUserId());
        assertThat(positionScore.getPosition()).isEqualTo(1);
    }

    @Test
    public void whenGetPosition_thenReturnNotFoundUser() {
        long id = UUID.randomUUID().getMostSignificantBits();
        ScoreUserRequestDTO scoreUser = new ScoreUserRequestDTO(id, Long.MAX_VALUE);
        ScoreUserResponseDTO positionScore = scoreService.getPositionScoreList(scoreUser.getUserId());
        assertThat(positionScore.getPosition()).isNull();
    }

    @Test
    public void whenGetHighScore_thenReturnMax20kUsers() {
        List<ScoreUserRequestDTO> scoreUserRequestDTOList = new ArrayList<>();
        for (int i = -10; i <= MAX_RECORDS; i++){
            ScoreUserRequestDTO scoreUserLocal = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(), Long.valueOf(i));
            scoreUserRequestDTOList.add(scoreUserLocal);
        }
        scoreUserRequestDTOList
                .parallelStream()
                .forEach(p -> scoreService.save(p));

        List<ScoreUserResponseDTO> positionScore = scoreService.getHighScoreList();
        assertThat(positionScore.size()).isEqualTo(MAX_RECORDS);
    }

    @Test
    public void whenGetHighScore_thenReturnFirstHighScoreUser() {
        List<ScoreUserRequestDTO> scoreUserRequestDTOList = new ArrayList<>();
        for (int i = -10; i <= MAX_RECORDS; i++){
            ScoreUserRequestDTO scoreUserLocal = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(),
                    Long.valueOf(i));
            scoreUserRequestDTOList.add(scoreUserLocal);
        }

        ScoreUserRequestDTO scoreUserFirstPosition = new ScoreUserRequestDTO(UUID.randomUUID().getMostSignificantBits(),
                Long.MAX_VALUE);
        scoreUserRequestDTOList.add(scoreUserFirstPosition);

        scoreUserRequestDTOList
                .parallelStream()
                .forEach(p -> scoreService.save(p));

        List<ScoreUserResponseDTO> positionScore = scoreService.getHighScoreList();
        assertThat(positionScore.get(0).getUserId()).isEqualTo(scoreUserFirstPosition.getUserId());
        assertThat(positionScore.get(0).getPoints()).isEqualTo(scoreUserFirstPosition.getPoints());
        assertThat(positionScore.get(0).getPosition()).isEqualTo(1);
    }

    @Test
    public void whenGetHighScore_thenReturnNotFoundUser() {
        List<ScoreUserResponseDTO> positionScore = scoreService.getHighScoreList();
        assertThat(positionScore.size()).isEqualTo(0);
    }
}
