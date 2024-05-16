public class GameStat {
    private int winner; // Index or ID of the winning player
    private int[] scores; // Array of scores, one for each player

    public GameStat(int winner, int[] scores) {
        this.winner = winner;
        this.scores = scores;
    }

    // Getters
    public int getWinner() {
        return winner;
    }

    public int[] getScores() {
        return scores;
    }
}
