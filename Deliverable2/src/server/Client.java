package server;

import server.TicTacToeServer;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client implements Runnable
{
    private Socket socket;
    private TicTacToeServer s;
    private DataInputStream in;
    private PrintStream out;
    private String line;
    private String ID;
    
    public Client(Socket socket, TicTacToeServer s)
    {
        this.socket = socket;
        this.s=s;
        try
        {
            in = new DataInputStream (socket.getInputStream());
            out = new PrintStream(socket.getOutputStream()); 
        }
        catch(Exception e) 
        {
            s.log(e.toString());
        }
    }
    
    public void run() 
    { 
        try 
        {
            in = new DataInputStream (socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            while((line = in.readLine()) != null && !line.equals("DC"))
            {   
                if(line.startsWith("NG")) //New Game
                {
                    if(s.countClients(line.substring(2))== 0)
                    {
                        this.ID = line.substring(2);
                        out.println("SVICONX");
                        out.println("SVSTARTGAME");
                    }
                    else
                    {
                        out.println("SVGAMEINUSE");
                    }
                }
                
                else if(line.startsWith("JG")) //Join Game
                {
                    if(s.countClients(line.substring(2))==1)
                    {
                        this.ID = line.substring(2);
                        out.println("SVICONO");
                        out.println("SVSTARTGAME");
                        s.sendMessageToClients("CHSERVER: GAME BEGIN!", ID);
                        s.sendMessageToClients("SVTURNX",ID);
                    }
                    else
                    {
                        out.println("SVNOGAME");
                    }
                }
                
                else if(line.startsWith("ID"))
                {
                    ID=null;
                }
                
                else
                {
                    s.sendMessageToClients(line,ID);
                }
                s.log("ID: "+ID+" Msg: "+line);
            }
            socket.close();
            in.close();
            out.close();
            s.removeClient(this);
        } 
        catch (Exception e)
        {
            s.log(e.toString());
            try
            {
                socket.close();
                in.close();
                out.close();
                s.removeClient(this);
            }
            catch(Exception ex)
            {
                s.log(ex.toString());
            }
        }
    }
    
    public String getID()
    {
        return this.ID;
    }
    
    public void sendMessage(String s)
    {
        out.println(s);
    }
}
