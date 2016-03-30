import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class UIGameView extends JPanel {
    
    public final int gifFrames = 19;
    public final int tileLength = 8;
    private final int boardBorderSize = 15;
    private final int boardSize;
    private final int pieceSize = 45;
    
    public ImageIcon[] btow = new ImageIcon[gifFrames];
    public ImageIcon[] wtob = new ImageIcon[gifFrames];
    
    public ImageIcon black, white, black2, white2, hint;
    
    public JPanel p_board = new JPanel(new GridLayout(tileLength, tileLength,0,0));
    
    protected JLabel board,wmsg, bmsg, message, messageboard, scoreboard, logo; 
    protected ImageIcon logoIc, messageIc, scoreIc, boardIc;

    protected final int iconNum;
    public JLabel[] label;
    protected ImageIcon[] icon, icon2; 
    
    public UIGameView(int icons) throws IOException{
        iconNum = icons;
        label = new JLabel[iconNum];
        
        icon = new ImageIcon[iconNum];
        icon2 = new ImageIcon[iconNum];
        
        boardIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/board.png")));
        boardSize = boardIc.getIconHeight() - 2*boardBorderSize;
        board = new JLabel(boardIc);
        board.setLayout(new BoxLayout(board, BoxLayout.PAGE_AXIS));
        
        p_board.setVisible(true);
        p_board.setMaximumSize(new Dimension(boardSize, boardSize));
        p_board.setOpaque(false);
        p_board.setBackground(new Color(0,0,0,1));
        p_board.setAlignmentX(CENTER_ALIGNMENT);
        
        board.add(Box.createVerticalGlue());
        board.add(p_board);
        board.add(Box.createVerticalGlue());
        board.setVisible(true);

        
        
        // Load pieces icons
        hint = new ImageIcon(ImageIO.read(getClass().getResource("graphics/lining.png")));
        hint = resize(hint,pieceSize,pieceSize);
        black = new ImageIcon(ImageIO.read(getClass().getResource("graphics/black.png")));
        white = new ImageIcon(ImageIO.read(getClass().getResource("graphics/white.png")));
        black = resize(black,pieceSize,pieceSize);
        white = resize(white,pieceSize,pieceSize);
        black2 = new ImageIcon(ImageIO.read(getClass().getResource("graphics/black2.png")));
        white2 = new ImageIcon(ImageIO.read(getClass().getResource("graphics/white2.png")));
        black2 = resize(black2,pieceSize,pieceSize);
        white2 = resize(white2,pieceSize,pieceSize);
        for (int i = 0; i < gifFrames-1; i++) {
            btow[i] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/btow/btow"+i+".png")));
            wtob[i] = new ImageIcon(ImageIO.read(getClass().getResource("graphics/btow/btow"+(gifFrames-2-i)+".png")));
        }
        btow[gifFrames-1] = white;
        wtob[gifFrames-1] = black;
    }
    
     private ImageIcon resize(ImageIcon img, int wscale, int hscale){    // Resizing images
        return new ImageIcon(img.getImage().getScaledInstance(wscale, hscale, Image.SCALE_SMOOTH));
    }
}
