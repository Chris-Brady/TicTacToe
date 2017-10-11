package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;
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
        this.setSize(500,300);
        this.setVisible(true);
        this.add(log);
        
        try{this.setTitle("Server - Output "+Inet4Address.getLocalHost().getHostAddress());} 
        catch (Exception e){log.writeToLog(e.toString());}
        
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt) 
            {
                System.exit(0);
            }
        });
        
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