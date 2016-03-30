import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Window extends JFrame {
    
    private UIInterface ui;
    
    public Window() throws IOException{
        setSize(550, 730);
        setLocation(50, 50);
        setMinimumSize(new Dimension(550,730));
        setMaximumSize(new Dimension(1000,900));
        setTitle("Reversi!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setIconImage(ImageIO.read(getClass().getResource("graphics/icon.png")));
        
        
        ui = new UIHello();
        getContentPane().add(ui);
        
        listenForSegue();
    }
    
    private void listenForSegue(){
        ui.addPropertyChangeListener("segueTo", (PropertyChangeEvent evt) -> {
            try {
                this.getContentPane().remove(ui);
                switch(evt.getNewValue().toString()){
                    case "HELLO": ui = new UIHello(); listenForSegue(); break;
                    case "LOGIN": ui = new UILogin(); listenForSegue(); break;
                    case "SIGNUP": ui = new UISignup(); listenForSegue(); break;
                    case "MENU": ui = new UIMenu(); listenForSegue(); break;
                    case "PROFILE": ui = new UIProfile(); listenForSegue(); break;
                    case "SETTINGS": ui = new UISettings(); listenForSegue(); break;
                    case "INSTRUCTIONS": ui = new UIInstructions(); listenForSegue(); break;
                    case "SINGLEPLAYER": ui = new UISinglePlayerController(); listenForSegue(); break;
                    case "MULTIPLAYER": ui = new UIMultiPlayer(); listenForSegue(); break;
                    default:break;
                }
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getContentPane().add(ui);
            this.revalidate();
            this.repaint();
        });
    }
    
    public static void main(String... args){
        try {
            Window w = new Window();
            w.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
