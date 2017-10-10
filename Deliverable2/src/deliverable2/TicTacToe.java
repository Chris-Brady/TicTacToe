package deliverable2;

import javax.swing.JFrame;


public class TicTacToe
{
    public static void main(String[] args)
    {
        
        JFrame ui = new JFrame("Tic Tac Toe");
        ui.add(new GameScreen());
        ui.pack();
        ui.setVisible(true);
        
        TicTacToeServer s = new TicTacToeServer();
        TicTacToeClient c = new TicTacToeClient();
    }
}
