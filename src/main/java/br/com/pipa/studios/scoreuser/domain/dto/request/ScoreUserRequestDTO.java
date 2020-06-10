package br.com.pipa.studios.scoreuser.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ScoreUserRequestDTO {
    private Long userId;
    private Long points;
}