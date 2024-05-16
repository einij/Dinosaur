import java.util.ArrayList;

public class GameStatus
    {
    private ArrayList<Player> players;
    private int currentPosition;
    private int speed;
    private int ticksPerStep;

    public GameStatus(ArrayList<Player> players, int initialSpeed, int ticksPerStep)
    {
        this.players = players;
        this.currentPosition = 0;
        this.ticksPerStep = ticksPerStep;
        this.speed = Math.max(initialSpeed, 1);
    }

    // Getters and setters
    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void incrementPosition()
    {
        this.currentPosition++;
    }

    public int getCurrentPosition()
    {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition)
    {
        this.currentPosition = currentPosition;
    }

    public int getTicksPerStep()
    {
        return ticksPerStep;
    }

    public int getSpeed()
    {
        return speed;
    }

    public boolean isComplete()
    {
        for (Player player : players)
        {
            if (player.getPlayerStatus().isAlive())
            {
                return false;
            }
        }
        return true;
    }

    // Incrementally increase the scroll speed as the game progresses.
    public void incrementSpeed(boolean increment)
    {
        if (increment)
        {
            this.speed++;
        }
    }
}
