import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private ArrayList<Player> localPlayers;
    private GamePlan gamePlan;
    private GameStatus gameStatus;
    private GameEnvironment env;
    private ScreenLayout layout;

    private static long lastMsec;

    private static int SPEEDUP_DISTANCE_TICKS = 8192;
    private static int TICKS_PER_POSITION = 10;

    static HashMap<AbstractMap.SimpleEntry<Integer, Integer>, AbstractMap.SimpleEntry<Integer, Integer>> keyMap = initKeyMap();
    static HashMap<AbstractMap.SimpleEntry<Integer, Integer>, AbstractMap.SimpleEntry<Integer, Integer>> initKeyMap()
    {
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, AbstractMap.SimpleEntry<Integer, Integer>> keyMap = 
            new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, AbstractMap.SimpleEntry<Integer, Integer>>();
        // When there's 1 local player.
        keyMap.put(pair(1, KeyEvent.VK_UP), pair(KeyEvent.VK_UP, 0));
        keyMap.put(pair(1, KeyEvent.VK_DOWN), pair(KeyEvent.VK_DOWN, 0));
        keyMap.put(pair(1, KeyEvent.VK_SPACE), pair(KeyEvent.VK_UP, 0));

        // When there're 2 local players.
        keyMap.put(pair(2, KeyEvent.VK_A), pair(KeyEvent.VK_UP, 0));
        keyMap.put(pair(2, KeyEvent.VK_SHIFT), pair(KeyEvent.VK_DOWN, 0));

        keyMap.put(pair(2, KeyEvent.VK_UP), pair(KeyEvent.VK_UP, 1));
        keyMap.put(pair(2, KeyEvent.VK_DOWN), pair(KeyEvent.VK_DOWN, 1));

        // When there're 3 local players.
        keyMap.put(pair(3, KeyEvent.VK_A), pair(KeyEvent.VK_UP, 0));
        keyMap.put(pair(3, KeyEvent.VK_SHIFT), pair(KeyEvent.VK_DOWN, 0));

        keyMap.put(pair(3, KeyEvent.VK_H), pair(KeyEvent.VK_UP, 1));
        keyMap.put(pair(3, KeyEvent.VK_B), pair(KeyEvent.VK_DOWN, 1));

        keyMap.put(pair(3, KeyEvent.VK_UP), pair(KeyEvent.VK_UP, 2));
        keyMap.put(pair(3, KeyEvent.VK_DOWN), pair(KeyEvent.VK_DOWN, 2));

        return keyMap;
    }

    class LocalPlayerKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) { }
        
        public void keyPressed(KeyEvent e) {
            AbstractMap.SimpleEntry<Integer, Integer> value = keyMap.get(pair(localPlayers.size(), e.getKeyCode()));
            if (value == null)
            {
                return;
            }
            PlayerStatus status = localPlayers.get(value.getValue()).getPlayerStatus();
            int keyReceived = 0;
            if (value.getKey() == KeyEvent.VK_UP)
            {
                keyReceived = 1;
            }
            else if (value.getKey() == KeyEvent.VK_DOWN)
            {
                keyReceived = 2;
            }
            System.out.println("Status: " + status + " key: " + keyReceived);
            status.setKeyReceived(keyReceived);
        }

        public void keyReleased(KeyEvent e) { }
    }

    private static AbstractMap.SimpleEntry<Integer, Integer> pair(int a, int b) {
        return new AbstractMap.SimpleEntry<>(a, b);
    }

    public Game(ArrayList<Player> players, GamePlan gamePlan, GameEnvironment env, ScreenLayout layout) {
        this.localPlayers = new ArrayList<>();
        for (Player player : players)
        {
            if (player.isLocal())
            {
                this.localPlayers.add(player);
            }
        }
        this.gamePlan = gamePlan;
        this.layout = layout;
        this.env = env;
        this.gameStatus = new GameStatus(players, env.getInitialSpeed(), TICKS_PER_POSITION);

        // Link I/O of players to the layout.
        this.layout.onboard(gamePlan, gameStatus, new LocalPlayerKeyListener());
    }

    private void movePlayers(GameStatus gameStatus)
    {
        for (Player player : localPlayers)
        {
            movePlayer(player, gameStatus);
        }
    }

    private void movePlayer(Player player, GameStatus gameStatus)
    {
        PlayerStatus playerStatus = player.getPlayerStatus();
        if (!playerStatus.isAlive()) {
            return;
        }

        int key = playerStatus.getKeyReceivedAndReset();

        // If the player was jumping (or forcefully going down) and just hit the ground, process the completion of the jump.
        if (playerStatus.getJumpType() != 0 && playerStatus.getCurrentHeight() <= 0)
        {
            playerStatus.setJumpType(0);
            playerStatus.setJumpBaseHeight(0);
            playerStatus.setJumpProgressTicks(0);
        }

        // If the player is on the ground or on the first jump and pressed 'up', initiate the jump.
        if ((playerStatus.getJumpType() == 0 || playerStatus.getJumpType() == 1) && key == 1)
        {
            playerStatus.setJumpBaseHeight(playerStatus.getCurrentHeight());
            playerStatus.setJumpType(playerStatus.getJumpType() + 1);
            playerStatus.setJumpProgressTicks(0);
        }

        // If the player is jumping and pressed 'down', start go down.
        if ((playerStatus.getJumpType() == 1 || playerStatus.getJumpType() == 2) && key == 2)
        {
            playerStatus.setJumpType(3);
            playerStatus.setJumpBaseHeight(0);
            // The initial velocity the player would have had to jump to reach this height.
            double v0 = Math.pow(2 * player.getDinosaur().getGravity() * playerStatus.getCurrentHeight(), 0.5);
            // Time that would have taken to reach the peak of the parabola.
            double sec = v0 / player.getDinosaur().getGravity();
            int ticks = (int) (sec * 100 / env.getTickMsec());
            playerStatus.setJumpProgressTicks(ticks);
        }

        // Increment the tick for jump duration.
        if (playerStatus.getJumpType() != 0)
        {
            playerStatus.incrementJumpProgressTicks();
        }

        // Move the player's height per physics.
        if (playerStatus.getJumpType() > 0)
        {
            playerStatus.setCurrentHeight(Math.max(playerStatus.getJumpBaseHeight() + calculateJumpHeight(player), 0));
        }
    }
    
    private int calculateJumpHeight(Player player)
    {
        Dinosaur dinosaur = player.getDinosaur();
        PlayerStatus playerStatus = player.getPlayerStatus();
        int dinosaurJumpHeight;

        if (playerStatus.getJumpType() == 1)
        {
            dinosaurJumpHeight = dinosaur.getJumpHeight();
        }
        else if (playerStatus.getJumpType() == 2)
        {
            dinosaurJumpHeight = dinosaur.getSecondJumpHeight();
        }
        else
        {
            dinosaurJumpHeight = playerStatus.getCurrentHeight();
        }

        // The time from the beginning of the jump in seconds.
        double sec = playerStatus.getJumpProgressTicks() * env.getTickMsec() / 100.0;
        // The initial velocity the player has to jump.
        double v0 = Math.pow(2 * player.getDinosaur().getGravity() * dinosaurJumpHeight, 0.5);
        // The expected height.
        int height = (int) (v0 * sec - 0.5 * player.getDinosaur().getGravity() * sec * sec);

        return height;
    }

    public boolean isOverlapping(int ax1, int ax2, int ay1, int ay2, int bx1, int bx2, int by1, int by2) {
        if (ay1 > by2 || ay2 < by1)
        {
            return false;
        }
        if (ax1 > bx2 || ax2 < bx1)
        {
            return false;
        }
        return true;
    }

    public void detectContacts(GameStatus gameStatus, GamePlan gamePlan)
    {
        for (Player player : localPlayers)
        {
            int graceMargin = (int) (player.getDinosaur().getWidth() * 0.2);

            for (int i = 0; ; i++)
            { 
                if (i > player.getDinosaur().getWidth() || !player.getPlayerStatus().isAlive())
                {
                    break;
                }
                int obstacleHeight = gamePlan.getValue(gameStatus.getCurrentPosition() + i);
                if (obstacleHeight <= 0)
                {
                    continue;
                }
                if (isOverlapping(graceMargin, player.getDinosaur().getWidth() - 2 * graceMargin,
                    player.getPlayerStatus().getCurrentHeight(), player.getPlayerStatus().getCurrentHeight() + player.getDinosaur().getHeight(),
                    i, i + 1, 0, obstacleHeight))
                {
                    System.out.println(player.getName() + " died.");
                    player.getPlayerStatus().setAlive(false);
                }
            }
        }
    }

    private String playersToString()
    {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Player player : gameStatus.getPlayers())
        {
            // Apply only to local players.
            if (player.isLocal())
            {
                if (i > 0)
                {
                    sb.append("|");
                }
                sb.append(player.getPlayerStatus().toString());
                i++;
            }
        }
        return sb.toString();
    }

    private void stringToPlayers(String buf)
    {
        if (buf == null)
        {
            return;
        }

        String[] chunks = buf.split("\\|");
        int i = 0;
        for (Player player : gameStatus.getPlayers())
        {
            // Apply only to remote players.
            if (!player.isLocal())
            {
                PlayerStatus playerStatus = player.getPlayerStatus();
                playerStatus.fromString(chunks[i]);
                i++;
            }
        }
    }

    public int sleepUntilNextTick(int tickMsec)
    {
        long currentTimeMsec = System.currentTimeMillis();
        long msecToSleep = Math.max(0, tickMsec - currentTimeMsec + lastMsec);

        try 
        {
            if (lastMsec == 0) {
                lastMsec = currentTimeMsec;
                Thread.sleep(tickMsec);
                return 1;
            }

            Thread.sleep(msecToSleep);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        long timePassed = msecToSleep + currentTimeMsec - lastMsec;
        lastMsec = currentTimeMsec;
        return Math.round(timePassed / tickMsec);
    }

    public GameStatus play() {
        for (int tick = 0; !gameStatus.isComplete(); tick++)
        {
            // Share GamePlan between the server and the client.
            gamePlan.exchangeGamePlan();

            // Progress the map.
            gameStatus.setCurrentPosition((tick * (int) (1 + 0.3 * gameStatus.getSpeed())) / gameStatus.getTicksPerStep());

            // Update the players.
            movePlayers(gameStatus);

            // Detect contact.
            detectContacts(gameStatus, gamePlan);

            // Updates scores and positions for the players.
            for (Player player : localPlayers)
            {
                if (player.getPlayerStatus().isAlive())
                {
                    player.getPlayerStatus().setCurrentScore(tick);
                    player.getPlayerStatus().setCurrentPosition(gameStatus.getCurrentPosition());
                }
            }

            // Synchronize the player statuses between the server and the client.
            stringToPlayers(Network.exchange(playersToString()));

            // Redraw the text, the maps, and the players.
            layout.redraw();

            // Speed up when it's time.
            gameStatus.incrementSpeed(gameStatus.getCurrentPosition() % SPEEDUP_DISTANCE_TICKS == 0);

            // Sleep until the next tick.
            sleepUntilNextTick(env.getTickMsec());
        }
        return gameStatus;
    }
}
