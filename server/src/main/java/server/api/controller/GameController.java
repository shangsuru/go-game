package server.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.dto.OwnGamesDTO;
import server.api.model.Game;
import server.api.repository.GameRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping
    public Game saveGameInfoAfterStart(@RequestBody Game game) {
        return gameRepository.save(game);
    }

    @GetMapping
    public List<Game> getGamesOfPlayers(@RequestParam String player1, @RequestParam String player2) {
        return gameRepository.findByPlayer1AndPlayer2OrderByTimestampDesc(player1, player2);
    }

    @GetMapping("/active")
    public ResponseEntity<Game> getActiveGamesOfPlayers(@RequestParam String player1, @RequestParam String player2) {
        Game mostRecentGame =  gameRepository.findFirstByPlayer1AndPlayer2OrderByTimestampDesc(player1, player2);
        if (mostRecentGame.isGameTerminated()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No active game
        }

        return new ResponseEntity<>(mostRecentGame, HttpStatus.OK);
    }

    @GetMapping("/{player}")
    public OwnGamesDTO getOwnGames(@PathVariable String player, @RequestParam int page) {
        List<Game> games = gameRepository.findByPlayer1OrPlayer2OrderByTimestampDesc(player, player);
        long wins = games.stream().filter(game -> game.isGameTerminated() &&
                                        (game.getPlayer1().equals(player) && game.isPlayer1Winner()
                                        || game.getPlayer2().equals(player) && !game.isPlayer1Winner())).count();
        long losses = games.size() - wins;

        int limit = 7;
        int skip = (page - 1) * limit; // Pagination
        return new OwnGamesDTO(games.subList(skip, Math.min(skip + limit, games.size())), wins, losses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Game> updateGameInfoAfterCompletion(@RequestBody JsonNode data, @PathVariable Long id) {
        boolean player1Won = data.get("player1Won").asBoolean();
        Optional<Game> gameOptional = gameRepository.findById(id);

        if (gameOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Game game = gameOptional.get();

        int ratingPlayer1 = game.getOldRatingPlayer1();
        int ratingPlayer2 = game.getOldRatingPlayer2();
        if (game.isRated()) {
            if (player1Won) {
                ratingPlayer1 += getRatingChange(ratingPlayer1, ratingPlayer2);
                ratingPlayer2 -= getRatingChange(ratingPlayer1, ratingPlayer2);
            } else {
                ratingPlayer1 -= getRatingChange(ratingPlayer2, ratingPlayer1);
                ratingPlayer2 += getRatingChange(ratingPlayer2, ratingPlayer1);
            }
        }

        game.setPlayer1Winner(player1Won);
        game.setNewRatingPlayer1(ratingPlayer1);
        game.setNewRatingPlayer2(ratingPlayer2);
        game.setGameTerminated(true);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private int getRatingChange(int winnerRating, int loserRating) {
        return Math.max(0, 5 + 15 * Math.min(loserRating / (winnerRating + 1), 1));
    }
}
