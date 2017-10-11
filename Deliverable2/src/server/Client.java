package server;

import server.TicTacToeServer;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client implements Runnable//Instance of a client on the server
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
            while((line = in.readLine()) != null)               //Interpret messages from client
            {   
                if(line.startsWith("NG")) //Tell server to start new game
                {
                    if(s.countClients(line.substring(2))== 0)
                    {
                        this.ID = line.substring(2);
                        out.println("SVICONX");     //Whoever started the game gets X
                        out.println("SVSTARTGAME"); //Tell the client to open a gamescreen
                    }
                    else
                    {
                        out.println("SVGAMEINUSE"); //Requested game ID already exists
                    }
                }
                
                else if(line.startsWith("JG")) //Tell the server to update the ID of the client to connect him with the other client
                {
                    if(s.countClients(line.substring(2))==1)
                    {
                        this.ID = line.substring(2);
                        out.println("SVICONO"); //Joiningplayer gets O
                        out.println("SVSTARTGAME");
                        s.sendMessageToClients("CHSERVER: GAME BEGIN!", ID);//Chat message to say the game has begun
                        s.sendMessageToClients("SVTURNX",ID);   //Grant X the first turn
                    }
                    else
                    {
                        out.println("SVNOGAME");//Game does not exist with that ID
                    }
                }
                
                else if(line.startsWith("ID"))  //Clear Clients game IDs so they no longer recieve messages from each other
                {
                    ID=null;
                }
                
                else
                {
                    s.sendMessageToClients(line,ID);    //Pass along the message for the clients to interpret
                }
                s.log("ID: "+ID+" Msg: "+line); //Log the message
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
    
    public void sendMessage(String s)   //Send message to this client
    {
        out.println(s);
    }
}
