import java.util.ArrayList;

public class GuiMain {
    public static void main(String[] args)
    {
        GameEnvironment environment = new GameEnvironment(9.8f, 320, 100, 20, 1, 200);
        SessionStat sessionStat = new SessionStat();

        int mode = 0;  // 0 = Standalone, 1 = Server, 2 = Client.

        int nPlayers = 1;
        String address = null;

        for (int i = 1; i < args.length; i++)
        {
            System.out.println(args[i]);
            if (args[i].startsWith("-n"))
            {
                nPlayers = Integer.parseInt(args[i].substring(2));
            }
            else if (args[i].startsWith("-s"))
            {
                mode = 1;
            }
            else if (args[i].startsWith("-c"))
            {
                mode = 2;
                address = args[i].substring(2);
                // Connect to the server.
            }
        }

        System.out.println("mode: " + mode);
        Network.init(mode, address);

        System.out.println("Players: " + nPlayers);

        ArrayList<Player> players = new ArrayList<Player>();
        for (int i = 0; i < nPlayers; i++) {
            Dinosaur dinosaur = GuiDinosaur.getDinosaurs().get(i % GuiDinosaur.getDinosaurs().size());
            players.add(new Player("Player " + (i + 1), dinosaur));
        }

        String remoteNPlayersBuf = Network.exchange("N_PLAYERS " + nPlayers);

        if (remoteNPlayersBuf != null)
        {
            for (int i = 0; i < Integer.parseInt(remoteNPlayersBuf.split(" ")[1]); i++) {
                Dinosaur dinosaur = GuiDinosaur.getDinosaurs().get((nPlayers + i) % GuiDinosaur.getDinosaurs().size());
                players.add(new Player("Player " + (nPlayers + i + 1), dinosaur, false));
            }
        }

        GuiScreenLayout layout = new GuiScreenLayout(sessionStat);

        GamePlan gamePlan = mode == 2 ? new RemoteGamePlan(environment) : new GamePlan(environment, mode);
        Game game = new Game(players, gamePlan, environment, layout);
        GameStatus status = game.play();
        System.out.println("Game complete: " + status.isComplete());
    }
}

