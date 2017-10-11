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
    
    public TicTacToeServer()    //Server holds all the clients
    {
        log = new ServerLogger();
        clients = new ArrayList<Client>();
        
        this.setAlwaysOnTop(true);
        this.setSize(500,300);
        this.setVisible(true);
        this.add(log);
        
        try{this.setTitle("Server - Output "+Inet4Address.getLocalHost().getHostAddress());} 
        catch (Exception e){log(e.toString());}
        
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt) 
            {
                System.exit(0);
            }
        });
        
        try
        {
            listener = new ServerSocket(PORT);
            while(true)//Accept clients, adapted from Multithreaded server example on sulis
            {
                socket = listener.accept();
                Client c = new Client(socket,this);
                clients.add(c);
                Thread t = new Thread(c);
                t.start();
                log("Client connected!");
            }
        } 
        catch (Exception e) 
        {
            log(e.toString());
        }
    }
    
    public synchronized void log(String s)//Write to server log
    {
        log.writeToLog(s);
    }
    
    public synchronized int countClients(String ID)//Count clients with matching ID
    {
        int count = 0;
        if(clients.size()>0)
            for(Client c: clients)
                if(c.getID()!=null)
                    if(c.getID().equals(ID))
                        count++;
        return count;
    }
    
    public synchronized void removeClient(Client c)//Remove a client from the list, called when clients lose connection/close window
    {
        clients.remove(c);
        log("Client removed: "+clients.size()+" clients left!");
    }
    
    public synchronized void sendMessageToClients(String s, String ID)//Send a message to all clients with matching ID
    {
        for(Client c:clients)
            if(c.getID()!=null)
                if(c.getID().equals(ID))
                    c.sendMessage(s);
    }
}