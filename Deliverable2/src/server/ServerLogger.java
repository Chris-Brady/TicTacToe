package server;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerLogger extends JPanel//Log panel to show incoming messages/info to the server
{
    private final JTextArea log;
    private final JScrollPane scroll;
    private final SimpleDateFormat sdf;
    private Calendar cal;
    
    public ServerLogger()
    {
        sdf = new SimpleDateFormat("HH:mm:ss:SSSS");
        System.out.println();
        log = new JTextArea();
        log.setEditable(false);
        scroll = new JScrollPane(log);
        this.setLayout(new GridLayout(1,1));
        this.add(scroll);
    }
    
    public synchronized void writeToLog(String s)//Call this to print to the log
    {
        cal = Calendar.getInstance();
        log.append(sdf.format(cal.getTime())+": "+s+"\n");
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
}
