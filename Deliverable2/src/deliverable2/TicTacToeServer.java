package deliverable2;

import java.io.IOException;
import java.net.ServerSocket;

public class TicTacToeServer
{
    ServerSocket socket;
    public TicTacToeServer()
    {
        try
        {
            socket = new ServerSocket(1184);
        } 
        catch (IOException e)
        {
            System.out.println(e);
        }
        
    }
}
