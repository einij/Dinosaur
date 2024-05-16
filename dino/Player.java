public class Player {
    private String name;
    private Dinosaur dinosaur;

    private PlayerStatus status;
    protected boolean local;

    public Player(String name, Dinosaur dinosaur)
    {
        this(name, dinosaur, true);
    }

    public Player(String name, Dinosaur dinosaur, boolean local)
    {
        this.name = name;
        this.dinosaur = dinosaur;

        this.status = createPlayerStatus();
        this.local = local;
    }

    public boolean isLocal()
    {
        return local;
    }

    public PlayerStatus createPlayerStatus()
    {
        return new PlayerStatus();
    }

    public PlayerStatus getPlayerStatus()
    {
        return status;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dinosaur getDinosaur() {
        return dinosaur;
    }

    public void setDinosaur(Dinosaur dinosaur) {
        this.dinosaur = dinosaur;
    }
}
