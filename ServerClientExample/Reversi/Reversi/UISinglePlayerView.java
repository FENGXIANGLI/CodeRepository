import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

class UISinglePlayerView extends UIGameView {
    
    private JPanel p_down, p_left, p_right; 
    
    public static final int UNDO = 0;
    public static final int HINT = 1;
    public static final int NEW = 2;
    public static final int SAVE = 3;
    public static final int LOAD = 4;
    public static final int MUSIC = 5;
    public static final int HOME = 6;

    public UISinglePlayerView() throws IOException{
        
        // init with 7 icons on board
        super(7);
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        
        loadIcons();
        initFrame();
        
        // change layout when resized
        addComponentListener(new ComponentListener() {  // When the window is resized, layout will change
            @Override
            public void componentResized(ComponentEvent e) {
                double ratio = (double)getHeight()/(double)getWidth();
               // TBC
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }
    
    
    private void initFrame() throws IOException{
        // Initialize panels
        
        p_down = new JPanel();
        p_down.setLayout(new BoxLayout(p_down, BoxLayout.PAGE_AXIS));
        p_left = new JPanel(new GridLayout(iconNum/2 +1,1));
        p_right = new JPanel(new GridLayout(iconNum/2,1));
        
        p_left.setBackground(Color.black);
        p_right.setBackground(Color.black);
        p_down.setBackground(Color.black);
        
        
        // set components
        this.setScoreBoard();
        this.setMessageBoard();
        this.setLogo();
        this.setIcons();
        
        p_down.add(Box.createVerticalGlue());
        p_down.add(messageboard);
        p_down.add(Box.createVerticalGlue());
        p_down.add(scoreboard);
        p_down.add(Box.createVerticalGlue());
        p_down.setAlignmentX(CENTER_ALIGNMENT);
        
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(p_left);
        b.add(board);
        b.add(p_right);
        b.add(Box.createHorizontalGlue());
        
        add(Box.createVerticalGlue());
        add(logo);
        add(b);
        add(Box.createVerticalGlue());
        add(p_down);
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
    }
    
    private void setIcons(){
        for (int i = 0; i < iconNum; ++i){
            label[i] = new JLabel(icon[i]);
            if (i < iconNum/2){
                p_right.add(label[i]);
            } else {
                p_left.add(label[i]);
            }
        }
    }
    
    private void setScoreBoard() throws IOException {
        scoreIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/label.png")));
        
        scoreboard = new JLabel(scoreIc);
        scoreboard.setLayout(new BoxLayout(scoreboard, BoxLayout.LINE_AXIS));
        
        wmsg = new JLabel(String.valueOf(0),SwingConstants.LEFT);
        bmsg = new JLabel(String.valueOf(0),SwingConstants.RIGHT);
        wmsg.setForeground(Color.getHSBColor(0.08f, 0.9f, 0.2f));
        wmsg.setFont(new Font("Arial",Font.BOLD,40));
        bmsg.setForeground(Color.getHSBColor(0.09f, 0.9f, 0.2f));
        bmsg.setFont(new Font("Arial",Font.BOLD,40));
        
        scoreboard.add(Box.createHorizontalGlue());
        scoreboard.add(bmsg);
        scoreboard.add(Box.createHorizontalGlue());
        scoreboard.add(wmsg);
        scoreboard.add(Box.createHorizontalGlue());
    }
    
    private void setMessageBoard() throws IOException{
        messageIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/label3.png")));
        messageboard = new JLabel(messageIc);
        
        messageboard.setLayout(new BoxLayout(messageboard,BoxLayout.PAGE_AXIS));
       
        messageboard.add(Box.createVerticalGlue());
        messageboard.add(Box.createVerticalGlue());
        message = new JLabel("Welcome to Reversi");
        message.setAlignmentX(CENTER_ALIGNMENT);
        message.setForeground(Color.getHSBColor(0.05f, 0.9f, 0.10f));
        message.setFont(new Font("Arial",Font.BOLD,22));
        messageboard.add(message);
        messageboard.add(Box.createVerticalGlue());
    }
    
    private void setLogo() throws IOException{
        logoIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/logo.png")));
        logo = new JLabel(logoIc);
        logo.setAlignmentX(CENTER_ALIGNMENT);
    }
    
    private void loadIcons() throws IOException {
        // Load label imageicons
        
        icon[UNDO] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/undo.png")));
        icon[HINT] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/hint.png")));
        icon[NEW] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/new.png")));
        icon[SAVE] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/save.png")));
        icon[LOAD] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/load.png")));
        icon[MUSIC] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/music.png")));
        icon[HOME] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/back_small.png")));
        icon2[UNDO] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/undo2.png")));
        icon2[HINT] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/hint2.png")));
        icon2[NEW] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/new2.png")));
        icon2[SAVE] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/save2.png")));
        icon2[LOAD] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/load2.png")));
        icon2[MUSIC] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/music2.png")));
        icon2[HOME] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/back_small2.png")));
        
    }
    
    
    public void changeMsg(String text){
    	message.setText(text);
    }
    public void setScore(String score, int color){
    	switch(color) {
    	case -1: bmsg.setText(score); break;
    	case 1: wmsg.setText(score); break;
    	}
    }
    public void setPressed(int _i){
        label[_i].setIcon(icon2[_i]);
    }
    public void setReleased(int _i){
        label[_i].setIcon(icon[_i]);
    }
    
}
