import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class UIInstructions extends UIInterface {
    public JLabel title, back, ins;
    
    public UIInstructions() throws IOException {
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        initFrame();
        
        back.addMouseListener(new MouseAdapter(){
            @Override
                public void mouseClicked(MouseEvent e) {
                    performSegue(UIViewEnum.MENU);
                }
        });
        setVisible(true);
    }
    
    private void initFrame() throws IOException {
        title = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/ins.png"))));
        ins = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/ins_body.png"))));
        back = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/back.png"))));
        title.setAlignmentX(CENTER_ALIGNMENT);
        ins.setAlignmentX(CENTER_ALIGNMENT);
        back.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(title);
        add(ins);
        add(back);
        add(Box.createVerticalGlue());
    }
    
    
}
