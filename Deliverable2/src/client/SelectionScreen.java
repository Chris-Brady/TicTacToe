package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SelectionScreen extends JPanel implements ActionListener   //This screen asks the user whether theyd like to create or join a game
{
    private final JLabel info;
    private final JTextField gameID;
    private final JButton join;
    private final JButton create;
    private TicTacToeClient c;
    
    public SelectionScreen(TicTacToeClient c)
    {
        this.c = c;
        info = new JLabel();
        info.setText("Please enter a game ID");
        gameID = new JTextField();
        join = new JButton();
        join.setText("Join");
        join.addActionListener(this);
        
        
        create = new JButton();
        create.setText("Create");
        create.addActionListener(this);
        
        
        this.setLayout(new GridLayout(2,2));
        this.add(info);
        this.add(gameID);
        this.add(join);
        this.add(create);
    }

    @Override
    public void actionPerformed(ActionEvent e)//ActionListener for buttons
    {
       Object source = e.getSource();
       if(source==join)
       {
            if(!gameID.getText().equals(""))
                c.serverMessage("JG"+gameID.getText());
       }
       else if(source==create)
       {
            if(!gameID.getText().equals(""))
                c.serverMessage("NG"+gameID.getText());
       }
    }
}
