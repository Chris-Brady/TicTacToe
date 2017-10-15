package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client implements Runnable, Comparable//Instance of a client on the server
{
    private Socket socket;
    private TicTacToeServer s;
    private DataInputStream in;
    private PrintStream out;
    private String line;
    private String ID;
    private String userName;
    
    public Client(Socket socket, TicTacToeServer s)
    {
        this.socket = socket;
        this.s=s;
        try
        {
            in = new DataInputStream (socket.getInputStream());
            out = new PrintStream(socket.getOutputStream()); 
        }
        catch(IOException e) 
        {
            s.log(e.toString());
        }
    }
    
    @Override
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
                        sendMessage("SVICONX");     //Whoever started the game gets X
                        sendMessage("SVSTARTGAME"); //Tell the client to open a gamescreen
                    }
                    else
                    {
                        sendMessage("SVGAMEINUSE"); //Requested game ID already exists
                    }
                }
                
                else if(line.startsWith("JG")) //Tell the server to update the ID of the client to connect him with the other client
                {
                    if(s.countClients(line.substring(2))==1)
                    {
                        this.ID = line.substring(2);
                        sendMessage("SVICONO"); //Joiningplayer gets O
                        sendMessage("SVSTARTGAME");
                        s.sendMessageToClients("CHSERVER: GAME BEGIN!", ID);//Chat message to say the game has begun
                        s.sendMessageToClients("SVTURNX",ID);   //Grant X the first turn
                    }
                    else
                    {
                        sendMessage("SVNOGAME");//Game does not exist with that ID
                    }
                }
                
                else if(line.startsWith("RG"))  //Clear Clients game IDs so they no longer recieve messages from each other
                {
                    sendMessage("SVRG"+s.getClientsAsString());
                }
                
                else if(line.startsWith("ID"))  //Clear Clients game IDs so they no longer recieve messages from each other
                {
                    ID=null;
                }
                
                else if(line.startsWith("NR"))  //Name request
                {
                    this.userName = line.substring(2);
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
        catch (IOException e)
        {
            s.log(e.toString());
            try
            {
                socket.close();
                in.close();
                out.close();
                s.removeClient(this);
            }
            catch(IOException ex)
            {
                s.log(ex.toString());
            }
        }
    }
    
    public String getID()
    {
        return this.ID;
    }
    
    public synchronized void sendMessage(String s)   //Send message to this client
    {
        out.println(s);
    }

    @Override
    public int compareTo(Object o)
    {
        if(o instanceof Client)
        {
            if( !( ( (Client)o ).getID() ).equals(null) )
                return compareTo(((Client)o).getID());
        }
        return -1;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
}
