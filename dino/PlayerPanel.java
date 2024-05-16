import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerPanel extends JPanel
{
    private JLabel statLabel;
    private GamePanel gamePanel;

    public PlayerPanel(Player player, GamePlan plan, GameStatus status)
    {
        statLabel = new JLabel(player.getName() + "\t\t\t\t\t\t\t" +
            player.getPlayerStatus().getCurrentScore() + " / " + player.getPlayerStatus().getHighestScore());
        add(statLabel);

        gamePanel = new GamePanel(player, plan, status);
        add(gamePanel);
    }
}
