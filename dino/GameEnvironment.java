public class GameEnvironment {
    private float gravity;
    private int screenWidth;
    private int screenHeight;
    private int tickMsec;
    private int initialSpeed;
    private int difficulty;

    public GameEnvironment(float gravity, int screenWidth, int screenHeight, int tickMsec, int difficulty, int speed)
    {
        this.gravity = gravity;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.tickMsec = tickMsec;
        this.difficulty = difficulty;
        this.initialSpeed = speed;
    }

    // Getters and setters
    public float getGravity()
    {
        return gravity;
    }

    public void setGravity(float gravity)
    {
        this.gravity = gravity;
    }

    public int getScreenWidth()
    {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth)
    {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight()
    {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight)
    {
        this.screenHeight = screenHeight;
    }

    public int getTickMsec()
    {
        return tickMsec;
    }

    public void setTickMsec(int tickMsec)
    {
        this.tickMsec = tickMsec;
    }
    
    public int getInitialSpeed()
    {
        return initialSpeed;
    }

    public void setInitialSpeed(int initialSpeed)
    {
        this.initialSpeed = initialSpeed;
    }

    public int getDifficulty()
    {
        return difficulty;
    }
}
