package br.com.pipa.studios.scoreuser.service;

import br.com.pipa.studios.scoreuser.domain.ScoreUser;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import br.com.pipa.studios.scoreuser.repository.ScoreUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ScoreUserUnitTest {

    @MockBean
    private ScoreUserRepository scoreUserRepository;

    private ScoreUserService scoreUserService;

    private ScoreUser scoreUser;

    List<ScoreUser>  scoreUserMock;

    @Before
    public void setup() {
        this.scoreUserService = new ScoreUserService(this.scoreUserRepository);

        scoreUserMock = Arrays.asList(
                new ScoreUser(1L,100L),
                new ScoreUser(2L,200L),
                new ScoreUser(3L,300L));

        scoreUser = new ScoreUser(1l, 1l);
    }

    @Test
    public void when_save_expect_berifySave() {
        scoreUserService.save(scoreUser);
        verify(scoreUserRepository).save(scoreUser);
    }

    @Test
    public void when_save_expect_verifyFindExist() {
        scoreUserService.save(scoreUser);
        verify(scoreUserRepository).findById(scoreUser.getUserId());
    }

    @Test
    public void when_exist_player_expect_sum_points() {
        Long points  = scoreUserService.sumTotalPoints(100l,100l);
        assertThat(points).isEqualTo(200);
    }

    @Test
    public void when_get_high_score_expect_order_points_by_high() {
        when(scoreUserRepository.findByOrderByPointsDesc()).thenReturn(scoreUserMock);

        List<ScoreUserResponseDTO> getHighScoreList = scoreUserService.getHighScoreList();

        assertThat(getHighScoreList.get(0).getPoints()).isEqualTo(300);
        assertThat(getHighScoreList.get(1).getPoints()).isEqualTo(200);
        assertThat(getHighScoreList.get(2).getPoints()).isEqualTo(100);

        assertThat(getHighScoreList.get(0).getUserId()).isEqualTo(3);
        assertThat(getHighScoreList.get(1).getUserId()).isEqualTo(2);
        assertThat(getHighScoreList.get(2).getUserId()).isEqualTo(1);

        assertThat(getHighScoreList.get(0).getPosition()).isEqualTo(1);
        assertThat(getHighScoreList.get(1).getPosition()).isEqualTo(2);
        assertThat(getHighScoreList.get(2).getPosition()).isEqualTo(3);
    }

    @Test
    public void when_get_position_score_expect_points_position() {
        when(scoreUserRepository.findByOrderByPointsDesc()).thenReturn(scoreUserMock);

        Optional<ScoreUserResponseDTO> scoreUserResponseDTO = scoreUserService.getPositionScoreList(1L);

        assertThat(scoreUserResponseDTO.isPresent()).isTrue();
        assertThat(scoreUserResponseDTO.get().getUserId()).isEqualTo(1);
        assertThat(scoreUserResponseDTO.get().getPoints()).isEqualTo(100);

    }
}
