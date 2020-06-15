package br.com.pipa.studios.scoreuser.service;

import br.com.pipa.studios.scoreuser.domain.dto.request.ScoreUserRequestDTO;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    public static final int MAX_RECORDS = 20000;

    private static ConcurrentHashMap<Long,Long> conHashMap = new ConcurrentHashMap<Long,Long>();

    public void save(ScoreUserRequestDTO scoreUser) {
        if(conHashMap.containsKey(scoreUser.getUserId())){
            Long lastPoints = conHashMap.get(scoreUser.getUserId());
            Long totalPoints = this.sumTotalPoints(scoreUser.getPoints(), lastPoints);
            conHashMap.put(scoreUser.getUserId(), totalPoints);
        }else{
            conHashMap.put(scoreUser.getUserId(), scoreUser.getPoints());
        }
    }

    public Long sumTotalPoints(Long actualPoints, Long totalPoints) {
        return totalPoints + actualPoints;
    }

    public List<ScoreUserResponseDTO> getHighScoreList() {
        AtomicInteger counter = new AtomicInteger(0);
        return conHashMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(p -> new ScoreUserResponseDTO(p.getKey(), p.getValue(), counter.incrementAndGet()))
                .limit(MAX_RECORDS)
                .collect(Collectors.toList());
    }

    public ScoreUserResponseDTO getPositionScoreList(Long userId) {
        try{
            AtomicInteger counter = new AtomicInteger(0);
            return conHashMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(p -> new ScoreUserResponseDTO(p.getKey(), p.getValue(), counter.incrementAndGet()))
                    .filter(p -> p.getUserId().equals(userId))
                    .collect(Collectors.toList()).get(0);
        }catch (IndexOutOfBoundsException e){
            return new ScoreUserResponseDTO();
        }

    }

    public void removeAll() {
        this.conHashMap = new ConcurrentHashMap<>();
    }
}
