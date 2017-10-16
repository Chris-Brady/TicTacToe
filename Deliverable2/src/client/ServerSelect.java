package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServerSelect extends JPanel implements ActionListener
{
    private final TicTacToeClient client;
    private final javax.swing.JButton login;
    private final javax.swing.JLabel serverInfo;
    private final javax.swing.JTextField serverName;
    
    public ServerSelect(TicTacToeClient client)  //Simple panel to display a connect screen
    {
        this.client = client;
        
        serverInfo = new JLabel();
        serverInfo.setText("Enter a server name");
        
        serverName = new javax.swing.JTextField();
        serverName.addActionListener(this);
        
        login = new javax.swing.JButton();
        login.setText("Connect");
        login.addActionListener(this);
        
        this.setLayout(new GridLayout(3,1));
        this.add(serverInfo);
        this.add(serverName);
        this.add(login);    
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!serverName.getText().equals(""))
        {
            try
            {
                client.setUp(serverName.getText());
                client.setTitle("TicTacToe - "+serverName.getText());
                client.updateCurrentScreen(new LoginScreen(client));
            }
            catch(Exception ex)
            {
                client.alertUser("Something went wrong: \n"+ex);
            }
        }
        else
            client.alertUser("Field must not be left empty: \nUse localhost for local server!");
    }
}

