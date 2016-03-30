import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

class UIMultiPlayer extends UIInterface {
    
    public final int gifFrames = 19;
    public final int tileLength = 8;
    public final int iconNum = 6;
    
    public ImageIcon[] btow = new ImageIcon[gifFrames];
    public ImageIcon[] wtob = new ImageIcon[gifFrames];
    public JLabel[] label = new JLabel[iconNum];
    
    public JLabel wmsg, bmsg, message, oppname, opprank;
    
    public ImageIcon black, white, black2, white2, hint;
    
    private JPanel p_left, p_right; 
    public JPanel p_board = new JPanel(new GridLayout(tileLength, tileLength,0,0));
    private ImageIcon[] icon, icon2; 
    private JLabel board, messageboard, scoreboard, opponent; 
    private ImageIcon oppIc, messageIc, scoreIc, boardIc;

    private final int UNDO = 0;
    private final int REJECT = 1;
    private final int AGREE = 2;
    private final int NEW = 3;
    private final int HOME = 5;
    private final int MUSIC = 4;
    
    

    public UIMultiPlayer() throws IOException{
        setBackground(Color.black);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        
        loadIcons();
        initFrame();
        
        addComponentListener(new ComponentListener() { 
            @Override
            public void componentResized(ComponentEvent e) {
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
    
    
    private void initFrame() throws IOException {
       // Initialize panels
        board = new JLabel();
        
        p_left = new JPanel(new GridLayout(iconNum/2,1));
        p_right = new JPanel(new GridLayout(iconNum/2,1));
        
        p_left.setBackground(Color.black);
        p_right.setBackground(Color.black);

        p_board.setBackground(new Color(0,0,0,1));
        board.setLayout(null);
        // pboard set bounds!
        board.add(p_board);
        board.setIcon(boardIc);
        board.setVisible(true);
        
        p_board.setVisible(true);
        p_board.setOpaque(false);
        
        
        // set subcomponents here
        setMessageBoard();
        setScoreBoard();
        setOpponent("name", "rank");
        setIcons();
        
        Box b_down = Box.createVerticalBox();
        b_down.add(Box.createVerticalGlue());
        b_down.add(opponent);
        b_down.add(Box.createVerticalGlue());
        b_down.add(scoreboard);
        b_down.add(Box.createVerticalGlue());
        b_down.setAlignmentX(CENTER_ALIGNMENT);
        opponent.setAlignmentX(CENTER_ALIGNMENT);
        scoreboard.setAlignmentX(CENTER_ALIGNMENT);
        
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(p_left);
        b.add(board);
        b.add(p_right);
        b.add(Box.createHorizontalGlue());
        
        add(Box.createVerticalGlue());
        add(messageboard);
        messageboard.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(b);
        add(Box.createVerticalGlue());
        this.add(b_down);
        
        add(Box.createVerticalGlue());
    }
    
    
    private void setMessageBoard() throws IOException {
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
    
    private void setOpponent(String name, String rank) throws IOException{
        oppIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/opponent_black.png")));
        
        opponent = new JLabel(oppIc);
        opponent.setAlignmentX(CENTER_ALIGNMENT);
        opponent.setLayout(new BoxLayout(opponent, BoxLayout.PAGE_AXIS));
        
        oppname = new JLabel(name);
        opprank = new JLabel(rank);
        oppname.setForeground(Color.black);
        oppname.setFont(new Font("Arial",Font.BOLD,20));
        opprank.setForeground(Color.black);
        opprank.setFont(new Font("Arial",Font.BOLD,20));
        oppname.setAlignmentX(CENTER_ALIGNMENT);
        opprank.setAlignmentX(CENTER_ALIGNMENT);
        
        opponent.add(Box.createVerticalGlue());
        opponent.add(oppname);
        opponent.add(Box.createVerticalGlue());
        opponent.add(opprank);
        opponent.add(Box.createVerticalGlue());
        
    }
    
    private void loadIcons() throws IOException {
        // Load label imageicons
        icon = new ImageIcon[iconNum];
        icon2 = new ImageIcon[iconNum];
        
        boardIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/board.png")));
        icon[UNDO] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/undo.png")));
        icon[NEW] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/new.png")));
        icon[REJECT] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/reject.png")));
        icon[AGREE] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/agree.png")));
        icon[MUSIC] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/music.png")));
        icon[HOME] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/back_small.png")));
        icon2[UNDO] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/undo2.png")));
        icon2[REJECT] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/reject2.png")));
        icon2[NEW] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/new2.png")));
        icon2[AGREE] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/agree2.png")));
        icon2[MUSIC] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/music2.png")));
        icon2[HOME] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/back_small2.png")));
        
        // Load pieces icons
        hint = new ImageIcon(ImageIO.read(getClass().getResource("graphics/lining.png")));
        //hint = resize(hint,LabelRatio.pieceSize,LabelRatio.pieceSize);
        black = new ImageIcon(ImageIO.read(getClass().getResource("graphics/black.png")));
        white = new ImageIcon(ImageIO.read(getClass().getResource("graphics/white.png")));
        //black = resize(black,LabelRatio.pieceSize,LabelRatio.pieceSize);
        //white = resize(white,LabelRatio.pieceSize,LabelRatio.pieceSize);
        black2 = new ImageIcon(ImageIO.read(getClass().getResource("graphics/black2.png")));
        white2 = new ImageIcon(ImageIO.read(getClass().getResource("graphics/white2.png")));
        //black2 = resize(black2,LabelRatio.pieceSize,LabelRatio.pieceSize);
        //white2 = resize(white2,LabelRatio.pieceSize,LabelRatio.pieceSize);
        for (int i = 0; i < gifFrames-1; i++) {
            btow[i] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/btow/btow"+i+".png")));
            wtob[i] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/btow/btow"+(gifFrames-2-i)+".png")));
        }
        btow[gifFrames-1] = white;
        wtob[gifFrames-1] = black;
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
