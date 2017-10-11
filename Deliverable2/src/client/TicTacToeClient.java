package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeClient extends JFrame //This class is the main Client class, it sends messages to the server and stores user info
{  
    private String userName;    
    private final JPanel game;
    private final JPanel login;
    private final JPanel selection;
    private Socket link;
    private BufferedReader br;
    private PrintWriter pw;
    private Thread reciever;
    
    public TicTacToeClient()
    { 
        login = new LoginScreen(this);
        selection = new SelectionScreen(this);
        game = new GameScreen(this);
        
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setTitle("TicTacToe");
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt) 
            {
                serverMessage("RS"+userName);
                System.exit(0);
            }
        });
        updateCurrentScreen(login);      
    }
    
    public void serverMessage(String s)//Sends a message to the server
    {
        try
        {
            pw.println(s);
            pw.flush();
        }
        catch(Exception e)
        {
            alertUser("Error messaging server!");
            System.exit(0);
        }
    }
    
    public void setUp(String serverIP) throws Exception//Set up the socket
    {
        link = new Socket(serverIP,4000);
        br=new BufferedReader(new InputStreamReader(link.getInputStream()));
        pw= new PrintWriter(link.getOutputStream());
        reciever = new Thread(new ServerListener(this,br));
        reciever.start();
    }
    
    
    public void updateCurrentScreen(JPanel p)//Change the current screen being displayed
    {
        this.getContentPane().removeAll();
        this.getContentPane().add(p);
        this.pack();
        this.setSize(600,420);
    }
    
    public void alertUser(String s)//JOptionPane for messages to the user
    {
        JOptionPane.showMessageDialog(this,s);
    }
    
    /*Basic Getters and Setters*/
    public void setUsername(String s){this.userName = s;}

    public String getUsername(){return this.userName;}
    public JPanel getGameScreen(){return this.game;}
    public JPanel getLoginScreen(){return this.login;}
    public JPanel getSelectionScreen(){return this.selection;}
}
