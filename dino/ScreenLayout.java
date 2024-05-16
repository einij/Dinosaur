import java.awt.event.KeyListener;

public interface ScreenLayout {
    void onboard(GamePlan gamePlan, GameStatus gameStatus, KeyListener keyListener);
    void redraw();
}
