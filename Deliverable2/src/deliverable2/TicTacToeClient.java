package deliverable2;

import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TicTacToeClient extends JFrame
{
    private ArrayList<JPanel> panels;
    private JPanel current;
    
    private Socket link;
    
    public TicTacToeClient()
    {
        try
        {
            link = new Socket("localhost",1184);
            System.out.println("Socket created");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
