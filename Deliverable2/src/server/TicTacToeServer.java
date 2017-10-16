package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class TicTacToeServer extends JFrame
{
    private ServerSocket listener;
    private Socket socket;
    private static final int PORT = 4000;
    private ArrayList<Client> clients;
    private ServerLogger log;
    private ArrayList<String[]> users;
    private final String path = "src/server/UserBase";
    
    public TicTacToeServer()    //Server holds all the clients
    {
        log = new ServerLogger();
        clients = new ArrayList<Client>();
        users = new ArrayList<String[]>();
        
        this.setAlwaysOnTop(true);
        this.setSize(500,300);
        this.setVisible(true);
        this.add(log);
        
        try
        {
            File f = new File(path);
            Scanner in = new Scanner(f);
            while(in.hasNext())
            {
                users.add(in.nextLine().split("/"));
            }
            in.close();
        }
        catch (Exception e)
        {
            log(e.toString());
        }
        
        try{this.setTitle("Server - Output "+Inet4Address.getLocalHost().getHostAddress());} 
        catch (Exception e){log(e.toString());}
        
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt) 
            {
                dumpUserBase();
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
    
    public synchronized String getClientsAsString() //Returns current games as a string
    {
        String result = "";
        for(Client c: clients)
            if(c.getID()!=null)
                if(countClients(c.getID())==1)
                    result+="["+c.getID()+"] "+c.getUserName()+"/";
        
        return result;
    }
    
    public synchronized String getLeaderBoard() //Returns a leaderboard as a string
    {
        String result = "";
        for(String[] s : users)
            result+=s[0]+"\tW["+s[2]+"]\tL["+s[3]+"]/"; 
        return result;
    }

    public synchronized boolean nameAvailable()
    {
        return false;
    }
    
    public synchronized String[] isUser(String name, String pass)
    {
        for(String[] s: users)
            if(name.equals(s[0])&&pass.equals(s[1]))
                return s;
        return null;
    }
    
    public synchronized void updateUser(String name, String str)
    {
        for(String[] s: users)
            if(s[0].equals(name))
            {
                if(str.equals("W"))
                    s[2] = ""+(Integer.parseInt(s[2])+1);
                else
                    s[3] = ""+(Integer.parseInt(s[3])+1);
                break;
            }
    }
    
    private void dumpUserBase()
    { 
        try
        {
            PrintWriter writer= new PrintWriter(path, "UTF-8");
            for(String[]s: users)
                writer.println(s[0]+"/"+s[1]+"/"+s[2]+"/"+s[3]);
            writer.close();
        } 
        catch (Exception e)
        {
            log(e.toString());
        }
    }

    public synchronized boolean nameAvailable(String name)
    {
        for(String[] s: users)
            if(s[0].equals(name))
                return false;
        return true;
    }

    public synchronized void addNewUser(String name, String pass)
    {
        String[] u = {name,pass,"0","0"};
        users.add(u);
    }
}