package br.com.pipa.studios.scoreuser.service;

import br.com.pipa.studios.scoreuser.domain.ScoreUser;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import br.com.pipa.studios.scoreuser.repository.ScoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ScoreUserService {

    public static final int MAX_RECORDS = 20000;

    private ScoreUserRepository scoreRepository;

    @Autowired
    public ScoreUserService(ScoreUserRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @CacheEvict(cacheNames = "ScoreUser", allEntries = true)
    public void save(ScoreUser scoreUser) {
        Optional<ScoreUser> existUser = this.findUser(scoreUser.getUserId());
        if(existUser.isPresent() && scoreUser.getPoints() > 0) {
            scoreUser.setPoints(this.sumTotalPoints(existUser.get().getPoints(), scoreUser.getPoints()));
        }
        scoreRepository.save(scoreUser);
    }

    public Optional<ScoreUser> findUser(Long userId) {
        return scoreRepository.findById(userId);
    }

    @Cacheable(cacheNames = "ScoreUser")
    public List<ScoreUserResponseDTO> getHighScoreList() {
        AtomicInteger counter = new AtomicInteger(0);
        return scoreRepository.findByOrderByPointsDesc()
                .stream()
                .sorted(Comparator.comparingLong(ScoreUser::getPoints).reversed())
                .limit(MAX_RECORDS)
                .map(p -> new ScoreUserResponseDTO(p.getUserId(), p.getPoints(), counter.incrementAndGet()))
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "ScoreUser")
    public Optional<ScoreUserResponseDTO> getPositionScoreList(Long userId) {
        List<ScoreUser> sortedPĺayers = scoreRepository.findByOrderByPointsDesc();
        Collections.sort(sortedPĺayers, Collections.reverseOrder());

        for (int i = 0; i < sortedPĺayers.size(); i++) {
            if(sortedPĺayers.get(i).getUserId().equals(userId)){
                return Optional.of(ScoreUserResponseDTO.builder()
                        .position(i + 1)
                        .userId(sortedPĺayers.get(i).getUserId())
                        .points(sortedPĺayers.get(i).getPoints())
                        .build());
            }
        }
        return Optional.empty();
    }

    public Long sumTotalPoints(Long actualPoints, Long totalPoints) {
        return totalPoints += actualPoints;
    }

}
