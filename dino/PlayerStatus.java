public class PlayerStatus {
    private int currentScore;
    private int highestScore;
    private int currentHeight; // Current height of the player
    private int currentPosition;
    private boolean alive; // Indicates if the player is still in the game

    private int jumpType;  // 0 = No jump, 1 = First jump, 2 = Second jump, 3 = Forced going down
    private int keyReceived;  // 0 = Nothing, 1 = Up, 2 = Down
    private int jumpBaseHeight;
    private int jumpProgressTicks; // Ticks since the jump started

    public PlayerStatus() {
        this.currentScore = 0;
        this.highestScore = 0;
        this.currentHeight = 0;
        this.currentPosition = 0;
        this.alive = true;

        this.jumpType = 0;
        this.keyReceived = 0;
        this.jumpBaseHeight = 0;
        this.jumpProgressTicks = 0;
    }

    // Getters and setters
    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
        if (this.currentScore > this.highestScore)
        {
            this.highestScore = currentScore;
        }
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public synchronized int getKeyReceivedAndReset()
    {
        int k = keyReceived;
        keyReceived = 0;
        return k;
    }

    public void setKeyReceived(int keyReceived)
    {
        this.keyReceived = keyReceived;
    }

    public int getJumpBaseHeight() {
        return jumpBaseHeight;
    }

    public void setJumpBaseHeight(int jumpBaseHeight) {
        this.jumpBaseHeight = jumpBaseHeight;
    }

    public int getJumpProgressTicks() {
        return jumpProgressTicks;
    }

    public void setJumpProgressTicks(int jumpProgressTicks) {
        this.jumpProgressTicks = jumpProgressTicks;
    }

    public void incrementJumpProgressTicks() {
        this.jumpProgressTicks++;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(int currentHeight) {
        this.currentHeight = currentHeight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String toString() {
        return "PLAYER_STATUS " + currentScore + " " + highestScore + " " + currentHeight + " " + currentPosition + " " + alive;
    }

    public void fromString(String buf)
    {
        String[] chunks = buf.split(" ");
        currentScore = Integer.parseInt(chunks[1]);
        highestScore = Integer.parseInt(chunks[2]);
        currentHeight = Integer.parseInt(chunks[3]);
        currentPosition = Integer.parseInt(chunks[4]);
        alive = Boolean.parseBoolean(chunks[5]);
    }
}
