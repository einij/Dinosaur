import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Network
{
    public static int PORT = 12024;
    public static int mode;  // 0 = Standalone, 1 = Server, 2 = Client

    static ObjectInputStream in;
    static ObjectOutputStream out;
    static Socket s;
    static ServerSocket server;

    public static void init(int mode, String ipAddr)
    {
        Network.mode = mode;
        if (mode == 1)
        {
            initServer();
        }
        else if (mode == 2)
        {
            initClient(ipAddr);
        }
    }

    public static void initServer()
    {
        try
        {
            server = new ServerSocket(PORT);
            System.out.println("1");
            s = server.accept();
            System.out.println("2");
            in = new ObjectInputStream(s.getInputStream());
            System.out.println("3");
            out = new ObjectOutputStream(s.getOutputStream());
            System.out.println("4");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void initClient(String ipAddress)
    {
            System.out.println("1");
        try
        {
            s = new Socket(ipAddress, PORT);
            System.out.println("2");
            out = new ObjectOutputStream(s.getOutputStream());
            System.out.println("3");
            in = new ObjectInputStream(s.getInputStream());
            System.out.println("4");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Object exchange(Object dataToSend)
    {
        try
        {
            if (mode == 1)
            {
                out.writeObject(dataToSend);
                out.flush();

                return in.readObject();
            }
            else if (mode == 2)
            {
                Object dataReceived = in.readObject();

                out.writeObject(dataToSend);
                out.flush();

                return dataReceived;
            }
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } 
        return null;
    }

    public static String exchange(String dataToSend)
    {
        return (String) exchange((Object) dataToSend);
    }
}