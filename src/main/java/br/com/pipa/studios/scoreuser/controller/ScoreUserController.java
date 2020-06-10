package br.com.pipa.studios.scoreuser.controller;


import br.com.pipa.studios.scoreuser.domain.ScoreUser;
import br.com.pipa.studios.scoreuser.domain.dto.request.ScoreUserRequestDTO;
import br.com.pipa.studios.scoreuser.domain.dto.response.ScoreUserResponseDTO;
import br.com.pipa.studios.scoreuser.service.ScoreUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScoreUserController {

    @Autowired
    private ScoreUserService scoreUserService;

    @ApiOperation(value = "This method can be called several times per user and not return anything. The points should be added to the user’s current score (score = current score + new points).")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created or Updated"),
            @ApiResponse(code = 500, message = "Exception"),
    })
    @PostMapping("/score")
    public ResponseEntity<Void> save(@RequestBody ScoreUserRequestDTO scoreUserRequestDTO) {
        if(scoreUserRequestDTO.getUserId() == null || scoreUserRequestDTO.getPoints() == null){
            return ResponseEntity.badRequest().build();
        }
        ScoreUser scoreUser = ScoreUser.builder()
                .points(scoreUserRequestDTO.getPoints())
                .userId(scoreUserRequestDTO.getUserId())
                .build();
        scoreUserService.save(scoreUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieves the current position of a specific user, considering the score for all users. If a user hasn't submitted a score, the response must be empty. ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Exception"),
    })
    @GetMapping("/{userId}/position")
    public ResponseEntity<ScoreUserResponseDTO> getUser(@PathVariable Long userId) {
        if(userId == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(scoreUserService.getPositionScoreList(userId)
                .orElse(null));
    }

    @ApiOperation(value = "Retrieves the high scores list, in order, limited to the 20000 higher scores. A request for a high score list without any scores submitted shall be an empty list.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 500, message = "Exception"),
    })
    @GetMapping("/highscorelist")
    public ResponseEntity<List<ScoreUserResponseDTO>> getHighScoreList() {
        return ResponseEntity.ok(scoreUserService.getHighScoreList());
    }
}
