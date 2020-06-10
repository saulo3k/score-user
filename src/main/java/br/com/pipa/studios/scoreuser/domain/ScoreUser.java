package br.com.pipa.studios.scoreuser.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@RedisHash("ScoreUser")
public class ScoreUser implements Serializable, Comparable<ScoreUser> {

    @Id
    private Long userId;
    @Indexed
    private Long points;

    @Override
    public int compareTo(ScoreUser o) {
        return Long.compare(points, o.points);
        //return points.compareTo(o.points);
    }
}
