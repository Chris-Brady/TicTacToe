package client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class GameScreen extends JPanel implements ActionListener
{
    private TicTacToeClient client;
    private int turn;
    private boolean isTurn;
    private String icon;
    private boolean gameOver;
    
    private final JPanel board;
    private final JPanel messenger;

    private final JButton[][] squares;
    
    private final JScrollPane scroll;
    private final JTextArea messageArea;
    
    private final JPanel interactionArea;
    private final JButton enter;
    private final JTextField typeArea;
    private final JButton resign;
    private final JButton rematch;
    
    public GameScreen(TicTacToeClient client)
    {
        init();
        this.client = client;
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
        
        interactionArea.setLayout(new GridLayout(3, 2));
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
    
    private boolean isTaken(int x, int y){return !squares[x][y].getText().equals("");}
    
    public void setTurn(boolean b)
    {
        if(b)
            writeToChat("SERVER: Your turn("+icon+")");
        isTurn = b;
    }
    public void setGameOver(boolean b){gameOver = b;}
    
    public String getIcon(){return this.icon;}
    public void setIcon(String icon){this.icon = icon;}
    
    public void init()
    {
        turn = 0;
        isTurn = false;
        gameOver = false;
    }
    
    public void writeToChat(String s)
    {
        messageArea.append(s+"\n");
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
    
    public void setButton(int x, int y, String icon)
    {
        squares[x][y].setText(icon);
        turn++;
        if (turn>=4&&icon.equals(this.icon))
        {
            String state = checkForWin();
            if(!state.equals(""))
                client.serverMessage(state);
        }
    }
    
    public void resetGame(boolean clearChat)
    {
        init();
        if(clearChat)
        {
            messageArea.setText("");
        }
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                squares[i][j].setText("");
    }
    
    public void restart()
    {
        if(icon.equals("X"))
            icon = "O";
        else
        {
            icon = "X";
            setTurn(true);
        }
    }
    
    public String checkForWin()
    {
        String result = "";
        String tmpr, tmpc = "";
        String cmp = icon+icon+icon;
        if(turn == 9){result = "GSDRAW";}
        for(int i=0;i<3;i++)
        {
            tmpr=tmpc="";
            for(int j=0;j<3;j++)
            {
                tmpr+=squares[i][j].getText();
                tmpc+=squares[j][i].getText();
            }
            if(tmpr.equals(cmp)||tmpc.equals(cmp))
                result="GSWIN"+icon;
        }
        if((squares[0][0].getText()+squares[1][1].getText()+squares[2][2].getText()).equals(cmp)){result="GSWIN"+icon;}
        if((squares[0][2].getText()+squares[1][1].getText()+squares[2][0].getText()).equals(cmp)){result="GSWIN"+icon;}
        return result;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object source = e.getSource();
        if(isTurn)
            for(int i=0;i<3;i++)
                for(int j=0;j<3;j++)
                    if(source == squares[i][j])
                        if(!isTaken(i,j))
                        {
                            client.serverMessage("GM,"+i+","+j+","+icon);
                            setTurn(false);
                        }
        
        if(source==rematch&&gameOver)
        {
            client.serverMessage("RM");
        }
        
        else if(source == resign)
        {
            client.serverMessage("RS"+client.getUsername());
            resetGame(true);
            client.updateCurrentScreen(client.getSelectionScreen());
        }
        
        else if(source == enter||source == typeArea)
        {
            if(!"".equals(typeArea.getText()))
            {
                client.serverMessage("CH"+client.getUsername()+": "+typeArea.getText());
                typeArea.setText(null);
            }
        }
    }
}
