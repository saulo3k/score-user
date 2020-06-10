package br.com.pipa.studios.scoreuser.repository;

import br.com.pipa.studios.scoreuser.domain.ScoreUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreUserRepository extends CrudRepository<ScoreUser, Long> {
    List<ScoreUser> findByOrderByPointsDesc();
}
