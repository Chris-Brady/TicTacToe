package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SelectionScreen extends JPanel implements ActionListener   //This screen asks the user whether theyd like to create or join a game
{
    private final JLabel info;
    private final JTextField gameID;
    private final JButton join;
    private final JButton create;
    
    private final JTextArea matches;
    private final JScrollPane scroll;
    private final JButton refresh;
    private final JButton leader;
    
    private final JPanel interaction;
    
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
        
        matches = new JTextArea();
        matches.setEditable(false);
        
        refresh = new JButton();
        refresh.setText("Refresh Games");
        refresh.addActionListener(this);
        
        leader = new JButton();
        leader.setText("Show Leaderboard");
        leader.addActionListener(this);
        
        
        scroll = new JScrollPane(matches);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        create = new JButton();
        create.setText("Create");
        create.addActionListener(this);
        
        interaction = new JPanel();
        interaction.setLayout(new GridLayout(6,1));
        interaction.add(info);
        interaction.add(gameID);
        interaction.add(join);
        interaction.add(create);
        interaction.add(refresh);
        interaction.add(leader);
        
        this.setLayout(new GridLayout(1,2));
        this.add(scroll);
        this.add(interaction);
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
            if(!gameID.getText().equals("")&&gameID.getText().matches("[a-zA-Z1-9]+"))
            {
                c.serverMessage("NG"+gameID.getText());
            }
            else
                c.alertUser("Invalid name! \nCannot contain special characters!");
       }
       
       else if(source==refresh)
       {
            c.serverMessage("RG");
       }
       
       else if(source==leader)
       {
            c.serverMessage("LB");
       }
    }
    
    public void postUpdate(String results)
    {
        String u = "";
        String[] x=results.split("/");
        for(String s:x)
            u+=s+"\n";
        matches.setText(u);
    }
    
}
