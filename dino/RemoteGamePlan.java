import java.util.ArrayList;

public class RemoteGamePlan extends GamePlan {

    public RemoteGamePlan(GameEnvironment env) {
        super(env);
    }

    public int getValue(int position)
    {
        int adjustedPos = position / MAP_UNIT_WIDTH;

        int bucket = adjustedPos / DEFAULT_MAP_GENERATION_SIZE;
        int index = adjustedPos % DEFAULT_MAP_GENERATION_SIZE;

        // Make one more bucket than necessary as buffer.
        if (map.size() > bucket)
        {
            return map.get(bucket)[index];
        }
        // If somehow no map data has been received, return a flat surface.
        return 0;
    }

    // Called by the client.
    public void exchangeGamePlan()
    {

        int clientGamePlanSize = map.size();
        int serverGamePlanSize = Integer.parseInt(Network.exchange("GAME_PLAN " + clientGamePlanSize).split(" ")[1]);
        if (clientGamePlanSize == serverGamePlanSize)
        {
            return;
        }
        addNewMapParts((ArrayList<int[]>) Network.exchange((Object) "GAME_PLAN"));
    }
}
