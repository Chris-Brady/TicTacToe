package deliverable2;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class GameScreen extends JPanel implements ActionListener
{
    
    private JPanel board;
    
    private JPanel messenger;

    private JButton[][] squares;
    
    private JScrollPane scroll;
    private JTextArea messageArea;
    
    private JPanel interactionArea;
    private JButton enter;
    private JTextField typeArea;
    private JButton resign;
    private JButton rematch;
    
    public GameScreen()
    {
        board = new JPanel();
        messenger = new JPanel();
        squares = new JButton[3][3];
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
            {
                squares[i][j] = new JButton();
                squares[i][j].addActionListener(this);
                board.add(squares[i][j]); 
            }
        
        //MESSAGEBOX AREA
        messageArea = new JTextArea();
        scroll = new JScrollPane(messageArea);
        interactionArea = new JPanel();
        typeArea = new JTextField();
        enter = new JButton();
        enter.addActionListener(this);
        resign = new JButton();
        resign.addActionListener(this);
        rematch = new JButton();
        rematch.addActionListener(this);
        
        typeArea.addActionListener(this);
        enter.setText("Enter");
        resign.setText("Resign");
        rematch.setText("Rematch");
        
        interactionArea.setLayout(new GridLayout(2, 2));
        interactionArea.add(typeArea);
        interactionArea.add(enter);
        interactionArea.add(resign);
        interactionArea.add(rematch);
        
        messageArea.setEditable(false);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        messenger.add(scroll);
        messenger.add(interactionArea);
        messenger.setLayout(new GridLayout(2, 1));
        board.setLayout(new GridLayout(3, 3));
        this.setLayout(new GridLayout(1, 2));
        this.add(board);
        this.add(messenger);
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object source = e.getSource();
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(source == squares[i][j])
                    messageArea.append("GAME: "+i+" "+j+"\n");
        if(source==rematch)
            messageArea.append("GAME: REMATCH\n");
        else if(source == resign)
            messageArea.append("GAME: RESIGN\n");
        else if(source == enter||source == typeArea)
            if(!"".equals(typeArea.getText()))
            {
                messageArea.append("PLAYER: "+typeArea.getText()+"\n");
                typeArea.setText("");   
            }
    }
}
