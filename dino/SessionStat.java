import java.util.ArrayList;
import java.util.List;

public class SessionStat {
    private int highestScore;
    private List<GameStat> gameStats;
    private List<PlayerStat> playerStats;

    public SessionStat() {
        this.highestScore = 0;
        this.gameStats = new ArrayList<>();
        this.playerStats = new ArrayList<>();
    }

    // Update the highest score in the session
    public void updateHighestScore(int score) {
        if (score > this.highestScore) {
            this.highestScore = score;
        }
    }

    // Add a GameStat to the session
    public void addGameStat(GameStat gameStat) {
        this.gameStats.add(gameStat);
        for (int score : gameStat.getScores()) {
            updateHighestScore(score);
        }
    }

    // Add PlayerStat to the session
    public void addPlayerStat(PlayerStat playerStat) {
        this.playerStats.add(playerStat);
    }

    // Getters
    public int getHighestScore() {
        return highestScore;
    }

    public List<GameStat> getGameStats() {
        return gameStats;
    }

    public List<PlayerStat> getPlayerStats() {
        return playerStats;
    }
}
