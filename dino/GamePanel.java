import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private Player player;
    private GamePlan plan;
    private GameStatus status;

    private static int SCREEN_WIDTH = 960;
    private static int SCREEN_HEIGHT = 240;
    private static int WIDTH_MARGIN = 30;
    private static int HEIGHT_MARGIN = 30;
    private static int GAME_WIDTH = SCREEN_WIDTH - 2 * WIDTH_MARGIN;
    private static int GAME_HEIGHT = SCREEN_HEIGHT - 2 * HEIGHT_MARGIN;
    private static int HORIZON_BASE_HEIGHT = 20;
    private static int PLAYER_BASE_HEIGHT = HORIZON_BASE_HEIGHT - 15;

    public GamePanel(Player player, GamePlan plan, GameStatus status)
    {
        this.player = player;
        this.plan = plan;
        this.status = status;

        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setVisible(true);
    }
    
    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void setGamePlan(GamePlan plan)
    {
        this.plan = plan;
    }

    public void setGameStatus(GameStatus status)
    {
        this.status = status;
    }

    private void paintMap(Graphics2D g2d)
    {
        if (plan == null || status == null)
        {
            return;
        }

        // Draw the horizontal line.
        int heightHorizontalLine = SCREEN_HEIGHT - HEIGHT_MARGIN - HORIZON_BASE_HEIGHT;
        g2d.drawLine(WIDTH_MARGIN, heightHorizontalLine, GAME_WIDTH + WIDTH_MARGIN, heightHorizontalLine);
        // Draw Obstacles at i, of which the height is gamePlan.getValue(i)
        int currentPosition = player.getPlayerStatus().getCurrentPosition();
        for (int i = 0; i < GAME_WIDTH; i++)
        {
            int height = plan.getValue(currentPosition + i);
            if (height <= 0)
            {
                continue;
            }
            // Find the range where the obstacles' heights remain the same to draw it as one object.
            int j = i + 1;
            while (plan.getValue(currentPosition + j) == height)
            {
                j++;
            }
            int obstacleBase = SCREEN_HEIGHT - HEIGHT_MARGIN - height;
            g2d.draw(new RoundRectangle2D.Double(WIDTH_MARGIN + i, obstacleBase, j - i, height, 0.5, 0.5));
            i = j - 1;
        }
    }

    private void paintPlayer(Graphics2D g2d)
    {
        if (player == null)
        {
            return;
        }
        int xPos = WIDTH_MARGIN;
        int yPos = SCREEN_HEIGHT - HEIGHT_MARGIN - player.getDinosaur().getHeight() - player.getPlayerStatus().getCurrentHeight();
        // Consider using getRotateInstance.
        Image image = ((GuiDinosaur) player.getDinosaur()).getImage();
        float scale = ((GuiDinosaur) player.getDinosaur()).getScale();
        AffineTransform xform = AffineTransform.getTranslateInstance(xPos, yPos);
        xform.scale(scale, scale);
        // Rotate 3 degree to animate.
        if (player.getPlayerStatus().isAlive())
        {
            if (status.getCurrentPosition() % 3 == 1)
            {
                xform.rotate(Math.PI * 3 / 360);
            }
            else if (status.getCurrentPosition() % 3 == 2)
            {
                xform.rotate(-Math.PI * 3 / 360);
            }
        }
        g2d.drawImage(image, xform, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintMap(g2d);
        paintPlayer(g2d);
        g2d.dispose();
    }
}
