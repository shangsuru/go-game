package server.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Game {
    public Game(String player1, String player2, int time,
                int timeIncrement, int oldRatingPlayer1,
                int newRatingPlayer1, int oldRatingPlayer2,
                int newRatingPlayer2, Instant date, boolean player1Won) {
        this.player1 = player1;
        this.player2 = player2;
        this.time = time;
        this.timeIncrement = timeIncrement;
        this.oldRatingPlayer1 = oldRatingPlayer1;
        this.newRatingPlayer1 = newRatingPlayer1;
        this.oldRatingPlayer2 = oldRatingPlayer2;
        this.newRatingPlayer2 = newRatingPlayer2;
        this.date = date;
        this.player1Won = player1Won;
    }

    protected Game() {}

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String player1;
    private String player2;
    private int time;
    private int timeIncrement;
    private int oldRatingPlayer1;
    private int newRatingPlayer1;
    private int oldRatingPlayer2;
    private int newRatingPlayer2;
    private Instant date;
    private boolean player1Won;

    public Long getId() {
        return id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeIncrement() {
        return timeIncrement;
    }

    public void setTimeIncrement(int timeIncrement) {
        this.timeIncrement = timeIncrement;
    }

    public int getOldRatingPlayer1() {
        return oldRatingPlayer1;
    }

    public void setOldRatingPlayer1(int oldRatingPlayer1) {
        this.oldRatingPlayer1 = oldRatingPlayer1;
    }

    public int getNewRatingPlayer1() {
        return newRatingPlayer1;
    }

    public void setNewRatingPlayer1(int newRatingPlayer1) {
        this.newRatingPlayer1 = newRatingPlayer1;
    }

    public int getOldRatingPlayer2() {
        return oldRatingPlayer2;
    }

    public void setOldRatingPlayer2(int oldRatingPlayer2) {
        this.oldRatingPlayer2 = oldRatingPlayer2;
    }

    public int getNewRatingPlayer2() {
        return newRatingPlayer2;
    }

    public void setNewRatingPlayer2(int newRatingPlayer2) {
        this.newRatingPlayer2 = newRatingPlayer2;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public boolean isPlayer1Won() {
        return player1Won;
    }

    public void setPlayer1Won(boolean player1Won) {
        this.player1Won = player1Won;
    }
}
