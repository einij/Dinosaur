import java.util.ArrayList;

public class GamePlan {
    protected ArrayList<int[]> map;
    private GameEnvironment env;
    private int difficulty;
    private int mode;

    protected static int MAX_OBSTACLE_HEIGHT = 80;
    protected static int DEFAULT_MAP_GENERATION_SIZE = 512;
    protected static int INITIAL_FLAT_LENGTH = 50;
    protected static int MAP_UNIT_WIDTH = 16;

    public GamePlan(GameEnvironment env)
    {
        this(env, 0);
    }

    public GamePlan(GameEnvironment env, int mode)
    {
        this.env = env;
        this.map = new ArrayList<int[]>();
        this.difficulty = env.getDifficulty();
        this.mode = mode;

        // Prepare for usage.
        getValue(0);
    }

    public ArrayList<int[]> getNewMapParts(int currentSize)
    {
        ArrayList<int[]> results = new ArrayList<int[]>();
        for (int i = currentSize; i < map.size(); i++)
        {
            results.add(map.get(i));
        }
        return results;
    }

    public void addNewMapParts(ArrayList<int[]> newParts)
    {
        for (int[] newPart : newParts)
        {
            map.add(newPart);
        }
    }

    private int[] createBucket(int bucketIndex)
    {
        int[] bucket = new int[DEFAULT_MAP_GENERATION_SIZE];

        for (int i = 0; i < DEFAULT_MAP_GENERATION_SIZE; i++)
        {
            if (bucketIndex * DEFAULT_MAP_GENERATION_SIZE + i <= INITIAL_FLAT_LENGTH)
            {
                continue;
            }
            if (Math.pow(Math.random(), difficulty + 1) >= 0.002)
            {
                continue;
            }
            // Don't have more than 3 obstables in 8 positions to give enough spaces to land and jump again.
            int obsCount = 0;
            for (int j = i - 1; j >= i - 8; j--)
            {
                int pos = bucketIndex * DEFAULT_MAP_GENERATION_SIZE + j;
                int jBucket = pos / DEFAULT_MAP_GENERATION_SIZE;
                int jIndex = pos % DEFAULT_MAP_GENERATION_SIZE;
                if ((jBucket == bucketIndex && bucket[jIndex] > 0) || (jBucket < bucketIndex && map.get(jBucket)[jIndex] > 0))
                {
                    obsCount++;
                }
            }
            if (obsCount >= 3)
            {
                continue;
            }
            bucket[i] = (int) ((1 - Math.pow(Math.random(), difficulty + 1)) * MAX_OBSTACLE_HEIGHT);
        }

        return bucket;
    }

    public int getValue(int position)
    {
        int adjustedPos = position / MAP_UNIT_WIDTH;

        int bucket = adjustedPos / DEFAULT_MAP_GENERATION_SIZE;
        int index = adjustedPos % DEFAULT_MAP_GENERATION_SIZE;

        // Make one more bucket than necessary as buffer.
        while (map.size() - 1 <= bucket)
        {
            map.add(createBucket(bucket));
        }

        return map.get(bucket)[index];
    }

    // Called by the server.
    public void exchangeGamePlan()
    {
        if (mode == 0) {
            return;
        }

        int serverGamePlanSize = map.size();
        int clientGamePlanSize = Integer.parseInt(Network.exchange("GAME_PLAN " + serverGamePlanSize).split(" ")[1]);
        if (clientGamePlanSize == serverGamePlanSize)
        {
            return;
        }
        Network.exchange(getNewMapParts(clientGamePlanSize));
    }
}
