package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginScreen extends JPanel implements ActionListener
{
    private TicTacToeClient client;
    private final javax.swing.JButton login;
    private final javax.swing.JLabel info;
     private final javax.swing.JLabel serverInfo;
    private final javax.swing.JTextField userName;
    private final javax.swing.JTextField serverName;
    
    public LoginScreen(TicTacToeClient client)
    {
        this.client = client;
        
        info = new JLabel();
        info.setText("Enter a username");
        
        serverInfo = new JLabel();
        serverInfo.setText("Enter a server name");
        
        userName = new javax.swing.JTextField();
        userName.addActionListener(this);
        
        serverName = new javax.swing.JTextField();
        serverName.addActionListener(this);
        
        login = new javax.swing.JButton();
        login.setText("Login");
        login.addActionListener(this);
        
        this.setLayout(new GridLayout(5,1));
        this.setSize(1000, 500);
        this.add(info);
        this.add(userName);
        this.add(serverInfo);
        this.add(serverName);
        this.add(login);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!userName.getText().equals("")&&!serverName.getText().equals(""))
        {
            client.setUsername(userName.getText());
            try
            {
                client.setUp(serverName.getText());
                client.setTitle("TicTacToe - "+userName.getText());
                client.updateCurrentScreen(client.getSelectionScreen());
            }
            catch(Exception ex)
            {
                client.alertUser("Something went wrong: \n"+ex);
            }
        }
        else
            client.alertUser("Fields must not be left empty: \nUse localhost for own server!");
    }
}
