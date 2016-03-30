import java.awt.*;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class UIProfile extends UIInterface {
    public JLabel title, back, body;
    public JTextField nick;
    public JLabel info[];
    private final int INFONUM = 5;
    
    // TBC
    private String label[] = {"Nickname","rank","100","20","70","10"}; 
    
    public UIProfile() throws IOException {
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        initFrame();
        
        back.addMouseListener(new MouseAdapter(){
            @Override
                public void mouseClicked(MouseEvent e) {
                    nick.getText();
                    // TBC
                    
                    
                    performSegue(UIViewEnum.MENU);
                }
        });
        
        setVisible(true);
    }
    
    private void initFrame() throws IOException {
        title = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/profile.png"))));
        // profile body
        body = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/profile_body.png"))));
        
        back = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("graphics/done.png"))));
        title.setAlignmentX(CENTER_ALIGNMENT);
        body.setAlignmentX(CENTER_ALIGNMENT);
        back.setAlignmentX(CENTER_ALIGNMENT);
        
        initInfo();
        
        add(Box.createVerticalGlue());
        add(title);
        add(body);
        add(back);
        add(Box.createVerticalGlue());
        
    }
    
    
    
    private void initInfo(){
        info = new JLabel[INFONUM];
        for (int i = 0; i < INFONUM; i++){
            info[i] = new JLabel();
            info[i].setAlignmentX(CENTER_ALIGNMENT);
        }
        nick = new JTextField(30);
        nick.setMaximumSize(nick.getPreferredSize());
        nick.setBackground(new Color(0,0,0,0));
        nick.setHorizontalAlignment(JTextField.CENTER);
        nick.setBorder(null);
        nick.setEditable(true);
        
        nick.setText(label[0]);
        for (int i = 0; i < INFONUM; ++i){
            info[i].setText(label[i+1]);
        }
        
        // for diff info criteria
        nick.setFont(new Font("Arial", Font.BOLD, 40));
        info[0].setFont(new Font("Arial", Font.BOLD, 30));
        info[1].setFont(new Font("Arial", Font.BOLD, 25));
        info[2].setFont(new Font("Arial", Font.BOLD, 25));
        info[3].setFont(new Font("Arial", Font.BOLD, 25));
        info[4].setFont(new Font("Arial", Font.BOLD, 25));

        body.setLayout(new GridLayout(2,1));
        
        Box b1 = Box.createVerticalBox();
        b1.add(Box.createVerticalGlue());
        b1.add(Box.createVerticalGlue());
        b1.add(nick);
        b1.add(Box.createVerticalGlue());
        b1.add(info[0]);
        b1.add(Box.createVerticalGlue());

        Box b2 = Box.createVerticalBox();
        b2.add(Box.createVerticalGlue());
        for (int i = 1; i < INFONUM; ++i){
            Box btemp = Box.createHorizontalBox();
            btemp.add(Box.createHorizontalGlue());
            btemp.add(Box.createHorizontalGlue());
            btemp.add(info[i]);
            btemp.add(Box.createHorizontalGlue());
            b2.add(btemp);
            b2.add(Box.createVerticalGlue());
        }
        b2.add(Box.createVerticalGlue());
        b2.add(Box.createVerticalGlue());
        
        body.add(b1);
        body.add(b2);
    }
    
}
