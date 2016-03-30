import java.awt.*;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class UISettings extends UIInterface {
    public JLabel title, back, body;
    private JTextField undo, hint;
    private JLabel music = new JLabel("ON (click to turn off)", SwingConstants.CENTER);
    private static boolean isMusicOn = true;
    private static int undoNum = 5, hintNum = 5;
    private static final int defaultUndo = 5, defaultHint = 5;
    private static final int maxUndo = 8, maxHint = 8;
    
    public UISettings() throws IOException {
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        initFrame();
        
        back.addMouseListener(new MouseAdapter(){
            @Override
                public void mouseClicked(MouseEvent e) {
                    try{
                        hintNum = Integer.parseUnsignedInt(hint.getText());
                        if (hintNum > maxHint)
                            hintNum = maxHint;
                    } catch(NumberFormatException ex){
                        hintNum = defaultHint;
                    }
                    try{
                        undoNum = Integer.parseUnsignedInt(undo.getText());
                        if (undoNum > maxUndo)
                            undoNum = maxUndo;
                    } catch(NumberFormatException ex){
                        undoNum = defaultUndo;
                    }
                    
                    performSegue(UIViewEnum.MENU);
                }
        });
        music.addMouseListener(new MouseAdapter(){
            @Override
                public void mouseClicked(MouseEvent e) {
                    if (isMusicOn){
                        music.setText("OFF (click to turn on)");
                        isMusicOn = false;
                    } else {
                        music.setText("ON (click to turn off)");
                        isMusicOn = true;
                    }
                }
        });
        
        
        setVisible(true);
    }
    
    private void initFrame() throws IOException {
        title = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/setting.png"))));
        // settings body
        body = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/settings_body.png"))));
        
        back = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/done.png"))));
        title.setAlignmentX(CENTER_ALIGNMENT);
        body.setAlignmentX(CENTER_ALIGNMENT);
        back.setAlignmentX(CENTER_ALIGNMENT);
        
        undo = new JTextField("5", 10);
        hint = new JTextField("5", 10);
        undo.setMaximumSize(undo.getPreferredSize());
        undo.setBackground(new Color(0,0,0,0));
        undo.setHorizontalAlignment(JTextField.CENTER);
        undo.setBorder(null);
        undo.setFont(new Font("Arial", Font.BOLD, 30));
        
        hint.setMaximumSize(undo.getPreferredSize());
        hint.setBackground(new Color(0,0,0,0));
        hint.setHorizontalAlignment(JTextField.CENTER);
        hint.setBorder(null);
        hint.setFont(new Font("Arial", Font.BOLD,30));
        
        music.setFont(new Font("Arial", Font.BOLD, 30));
        
        initComponentLayout();
        
        
        add(Box.createVerticalGlue());
        add(title);
        add(body);
        add(back);
        add(Box.createVerticalGlue());
    }
    
    private void initComponentLayout(){
        
        Box b1 = Box.createHorizontalBox();
        Box b2 = Box.createHorizontalBox();
        
        b1.add(Box.createHorizontalGlue());
        b1.add(undo);
        b2.add(Box.createHorizontalGlue());
        b2.add(hint);
        
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalGlue());
        b.add(Box.createVerticalGlue());
        b.add(Box.createVerticalGlue());
        b.add(Box.createVerticalGlue());
        b.add(b1);
        b.add(Box.createVerticalGlue());
        b.add(b2);
        b.add(Box.createVerticalGlue());
        
        body.setLayout(new GridLayout(2,1));
        body.add(b);
        body.add(music);
    }
    public static int getHint(){
        return hintNum;
    }
    public static int getUndo(){
        return undoNum;
    }
    public static boolean getIsMusicOn(){
        return isMusicOn;
    }
    
}

