public class PlayerStat {
    private int highestScore;
    private int nGamesWon;

    public PlayerStat() {
        this.highestScore = 0;
        this.nGamesWon = 0;
    }

    // Methods to update player stats
    public void updateScore(int score) {
        if (score > this.highestScore) {
            this.highestScore = score;
        }
    }

    public void incrementGamesWon() {
        this.nGamesWon++;
    }

    // Getters
    public int getHighestScore() {
        return highestScore;
    }

    public int getNGamesWon() {
        return nGamesWon;
    }
}
