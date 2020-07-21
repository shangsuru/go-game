package server.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
