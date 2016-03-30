import java.awt.*;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Box;


public class UISignup extends UIInterface {

    private JLabel body, sign, back;
    private ImageIcon textIc, signIc, backIc;
    private JTextField user, pw, nick;
    
    private final int textColumnNum = 16;
    private final Font f = new Font("Arial", Font.BOLD, 20);
    
    public UISignup() throws IOException {
        
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.black);
        
        initFrame();
        initComponents();
        
        setVisible(true);
    }

    
    private void initFrame() throws IOException{
        
        setTextFields();
        
        signIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/signup.png")));
        sign = new JLabel(signIc);
        backIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/back.png")));
        back = new JLabel(backIc);
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(sign);
        b.add(back);
        b.add(Box.createHorizontalGlue());
        
        body.setAlignmentX(CENTER_ALIGNMENT);
        
        add(Box.createVerticalGlue());
        add(body);
        add(b);
        add(Box.createVerticalGlue());
        
    }
    
    private void setTextFields() throws IOException{
        textIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/signup_body.png")));
        body = new JLabel(textIc);
        body.setAlignmentX(CENTER_ALIGNMENT);
        
        user = new JTextField("Username",textColumnNum);
        user.setFont(f);
        user.setMaximumSize(user.getPreferredSize());
        user.setBackground(new Color(0,0,0,0));
        user.setBorder(null);
        
        nick = new JTextField("Display name",textColumnNum);
        nick.setFont(f);
        nick.setMaximumSize(nick.getPreferredSize());
        nick.setBackground(new Color(0,0,0,0));
        nick.setBorder(null);
        
        pw = new JPasswordField("Password",textColumnNum);
        pw.setFont(f);
        pw.setMaximumSize(pw.getPreferredSize());
        pw.setBackground(new Color(0,0,0,0));
        pw.setBorder(null);
        
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        
        Box b1 = Box.createHorizontalBox();
        b1.add(Box.createHorizontalGlue());
        b1.add(user);
        Box b2 = Box.createHorizontalBox();
        b2.add(Box.createHorizontalGlue());
        b2.add(nick);
        Box b3 = Box.createHorizontalBox();
        b3.add(Box.createHorizontalGlue());
        b3.add(pw);
        
        body.add(Box.createVerticalGlue());        
        body.add(b1);
        body.add(Box.createVerticalGlue());
        body.add(b2);
        body.add(Box.createVerticalGlue());
        body.add(b3);
        body.add(Box.createVerticalGlue());
    }
    
    private void initComponents(){
        sign.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                user.getText();
                pw.getText();
                
                
                // TBC
            }
        });
        back.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                performSegue(UIViewEnum.HELLO);
            }
        });
    }
    
    
    
}