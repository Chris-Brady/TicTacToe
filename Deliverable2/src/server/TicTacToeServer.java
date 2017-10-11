package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;

public class TicTacToeServer extends JFrame
{
    private ServerSocket listener;
    private Socket socket;
    private static final int PORT = 4000;
    private ArrayList<Client> clients;
    private ServerLogger log;
    
    public TicTacToeServer()
    {
        log = new ServerLogger();
        this.setAlwaysOnTop(true);
        this.setSize(300,300);
        this.setVisible(true);
        this.add(log);
        
        clients = new ArrayList<Client>();
        
        try
        {
            listener = new ServerSocket(PORT);
            while(true)
            {
                socket = listener.accept();
                Client c = new Client(socket,this);
                clients.add(c);
                Thread t = new Thread(c);
                t.start();
                log.writeToLog("Client connected!");
            }
        } 
        catch (Exception e) 
        {
            log.writeToLog(e.toString());
        }
    }
    
    public synchronized void log(String s)
    {
        log.writeToLog(s);
    }
    
    public synchronized int countClients(String ID)
    {
        int count = 0;
        if(clients.size()>0)
            for(Client c: clients)
                if(c.getID()!=null)
                    if(c.getID().equals(ID))
                        count++;
        return count;
    }
    
    public synchronized void removeClient(Client c)
    {
        clients.remove(c);
        log.writeToLog("Client removed: "+clients.size()+" clients left!");
    }
    
    public synchronized void sendMessageToClients(String s, String ID)
    {
        for(Client c:clients)
            if(c.getID()!=null)
                if(c.getID().equals(ID))
                    c.sendMessage(s);
    }
}