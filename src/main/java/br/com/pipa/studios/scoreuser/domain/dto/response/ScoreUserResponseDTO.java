package br.com.pipa.studios.scoreuser.domain.dto.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ScoreUserResponseDTO implements Serializable {
    private Long userId;
    private Long points;
    private Integer position;
}