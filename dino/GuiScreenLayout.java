import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GuiScreenLayout implements ScreenLayout {
    private SessionStat sessionStat;
    private ArrayList<Player> players;

    private JFrame frame;
    private JLabel statLabel;

    private HashMap<Player, GamePanel> gamePanels;
    private HashMap<Player, JLabel> gameLabels;

    public GuiScreenLayout(SessionStat sessionStat) {
        this.sessionStat = sessionStat;

        frame = new JFrame("Dinosaur Run");
        statLabel = new JLabel("Highest Score: " + sessionStat.getHighestScore());
        frame.add(statLabel, BorderLayout.NORTH);


        gamePanels = new HashMap<Player, GamePanel>();
        gameLabels = new HashMap<Player, JLabel>();

        // Set frame properties
        frame.setSize(1024, 960);
        // Close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Make the frame visible
        frame.setVisible(true);

        players = new ArrayList<Player>();
    }

    public void redraw() {
        statLabel.setText("Highest Score: " + sessionStat.getHighestScore());
        for (Player player : players) {
            gamePanels.get(player).revalidate();
            gamePanels.get(player).repaint();
            gameLabels.get(player).setText(player.getName() + " (" + player.getDinosaur().getName() + ")             " + player.getPlayerStatus().getCurrentScore() + " / " + player.getPlayerStatus().getHighestScore());
        }
    }

    @Override
    public void onboard(GamePlan gamePlan, GameStatus gameStatus, KeyListener keyListener) {
        JPanel parentGamePanel = new JPanel();
        frame.add(parentGamePanel, BorderLayout.CENTER);

        ArrayList<Player> localPlayers = new ArrayList<Player>();
        for (Player player : gameStatus.getPlayers()) {
            this.players.add(player);

            JLabel playerStatLabel = new JLabel();
            parentGamePanel.add(playerStatLabel);
            gameLabels.put(player, playerStatLabel);
            playerStatLabel.setText(player.getName() + " (" + player.getDinosaur().getName() + ")             " + player.getPlayerStatus().getCurrentScore() + " / " + player.getPlayerStatus().getHighestScore());

            GamePanel panel = new GamePanel(player, gamePlan, gameStatus);
            System.out.println("Adding Game Panel: " + panel);
            gamePanels.put(player, panel);
            parentGamePanel.add(panel);
            if (player.isLocal()) {
                localPlayers.add(player);
            }
        }
        if (localPlayers.size() > 3) {
            throw new RuntimeException("Can't support more than 3 local players");
        }

        frame.addKeyListener(keyListener);
    }
}
