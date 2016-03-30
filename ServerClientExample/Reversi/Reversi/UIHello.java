import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Box;


public class UIHello extends UIInterface {

    private JLabel logo, login, signup;
    private ImageIcon logoIc, loginIc, signupIc;
      
    public UIHello() throws IOException {
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.black);
        
        initFrame();
        initComponents();
        
        setVisible(true);
    }

    
    private void initFrame() throws IOException{
        // add logo
        logoIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/logo2.png")));
        logo = new JLabel(logoIc);
        logo.setAlignmentX(CENTER_ALIGNMENT);
        
        loginIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/login.png")));
        login = new JLabel(loginIc);
        signupIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/signup.png")));
        signup = new JLabel(signupIc);
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(login);
        b.add(signup);
        b.add(Box.createHorizontalGlue());
        
        add(Box.createVerticalGlue());
        add(logo);
        add(b);
        add(Box.createVerticalGlue());
        
    }
    
    private void initComponents(){
        login.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                performSegue(UIViewEnum.LOGIN);
            }
        });
        signup.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                performSegue(UIViewEnum.SIGNUP);
            }
        });
    }
    
}
