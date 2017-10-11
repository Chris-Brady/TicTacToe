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

public class TicTacToeClient extends JFrame
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
        setVisible(true);
        setAlwaysOnTop(true);
        
        login = new LoginScreen(this);
        selection = new SelectionScreen(this);
        game = new GameScreen(this);
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
    
    public void serverMessage(String s)
    {
        try
        {
            pw.println(s);
            pw.flush();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void setUp(String serverIP) throws Exception
    {
        link = new Socket(serverIP,4000);
        br=new BufferedReader(new InputStreamReader(link.getInputStream()));
        pw= new PrintWriter(link.getOutputStream());
        reciever = new Thread(new ServerListener(this,br));
        reciever.start();
    }
    
    
    public void updateCurrentScreen(JPanel p)
    {
        this.getContentPane().removeAll();
        this.getContentPane().add(p);
        this.pack();
        this.setSize(600,420);
    }
    
    public void alertUser(String s)
    {
        JOptionPane.showMessageDialog(this,s);
    }
    
    public void setUsername(String s){this.userName = s;}
    
    public String getUsername(){return this.userName;}
    public JPanel getGameScreen(){return this.game;}
    public JPanel getLoginScreen(){return this.login;}
    public JPanel getSelectionScreen(){return this.selection;}
}
