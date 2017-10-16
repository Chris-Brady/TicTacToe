package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends JPanel implements ActionListener
{
    private final TicTacToeClient client;
    private final JButton login;
    private final JButton register;
    private final JLabel info;
    private final JLabel passwordInfo;
    private final JTextField userName;
    private final JPasswordField password;
    
    public LoginScreen(TicTacToeClient client)  //Simple panel to display a login screen
    {
        this.client = client;
        
        info = new JLabel();
        info.setText("Enter a username");
        
        passwordInfo = new JLabel();
        passwordInfo.setText("Enter your password");
        
        userName = new JTextField();
        password = new JPasswordField();
        
        login = new JButton();
        login.setText("Login");
        login.addActionListener(this);
        
        register = new JButton();
        register.setText("Register");
        register.addActionListener(this);
        
        this.setLayout(new GridLayout(6,1));
        this.add(info);
        this.add(userName);
        
        this.add(passwordInfo);
        this.add(password);
        
        this.add(login);
        this.add(register);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(!userName.getText().equals("")&&!password.getText().equals(""))
        {
            if(source == login) 
                client.serverMessage("LR/"+userName.getText()+"/"+password.getText());
            else if(source == register)
            {
                if((userName.getText()).length()<=8&&(userName.getText()).matches("[a-zA-Z1-9]+"))
                    client.serverMessage("RR/"+userName.getText()+"/"+password.getText());
                else
                    client.alertUser("Username must be 8 or less characters with no special characters!");
            }
        }
        else
            client.alertUser("Fields must not be left empty!");
    }
}
