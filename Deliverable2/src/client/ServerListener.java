package client;

import java.io.BufferedReader;

public class ServerListener implements Runnable //This class listens for messages from the server as a thread
{
    private BufferedReader br;
    private GameScreen gs;
    private TicTacToeClient c;
    
    public ServerListener(TicTacToeClient c, BufferedReader br)
    {
        this.c = c;
        this.gs = c.getGameScreen();
        this.br = br;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Interpret(br.readLine());
            }
            catch(Exception e)
            {
                c.alertUser("Server Error!");
                System.exit(0);
            }
        }
    }
    
    private void Interpret(String s)    //This function interprets server messages and affects the client accordingly
    {
        System.out.println("Rec: "+s);  
        
        if(s.startsWith("GM"))//gamemove
        {
            String[] t = s.split(",");
            gs.setButton(Integer.parseInt(t[1]),Integer.parseInt(t[2]),t[3]);
            if(!t[3].equals(gs.getIcon()))
            {
                gs.setTurn(true);
            }
        }
        
        else if(s.startsWith("RM"))//rematch
        {
            gs.writeToChat("SERVER: GAME RESTART!");
            gs.resetGame(false);
            gs.restart();
        }
        
        else if(s.startsWith("RS"))//resign
        {
            gs.writeToChat(s.substring(2)+" has resigned");
            if(s.substring(2).equals(c.getUsername()))
                gs.resetGame(true);
            c.serverMessage("ID");
        }
        
        else if(s.startsWith("CH"))//chat
        {
            gs.writeToChat(s.substring(2));
        }
        
        else if(s.startsWith("GS"))//gamestate
        {
            String gameState = s.substring(2);
            gs.setTurn(false);
            if(gameState.startsWith("DRAW")){c.alertUser("Game over: Draw!");}
            else if(gameState.startsWith("WIN"))
            {
                if(gameState.substring(3).equals(gs.getIcon()))
                {
                    c.alertUser("Game Over: Win!");
                }
                else
                {
                    c.alertUser("Game Over: Loss!");
                }
                gs.writeToChat("GAME OVER");
            }
            c.alertUser("Rematch to play again, Resign to exit");
            gs.setGameOver(true);
        }
        
        else if(s.startsWith("SV"))//servermessage
        {
            String serverCommand = s.substring(2);
            if(serverCommand.startsWith("TURN"))
            {
                if(gs.getIcon().equals(serverCommand.substring(4)))
                    gs.setTurn(true);
            }
            else if(serverCommand.startsWith("ICON"))
                gs.setIcon(serverCommand.substring(4));
            else if(serverCommand.startsWith("RG"))
                c.getSelectionScreen().postUpdate(serverCommand.substring(2));
            else if(serverCommand.equals("STARTGAME"))
                c.updateCurrentScreen(c.getGameScreen());
            else if (serverCommand.equals("NOGAME"))
                c.alertUser("Game unavailable to join!");
            else if (serverCommand.equals("GAMEINUSE"))
                c.alertUser("GameID already in use!");  
        }
    }
}
