import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Box;


public class UILogin extends UIInterface {

    private JLabel text, login, back;
    private ImageIcon textIc, loginIc, backIc;
    private JTextField user, pw;
    
    private final int textColumnNum = 18;
    private final Font f = new Font("Arial", Font.BOLD, 20);
    
    public UILogin() throws IOException {
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.black);
        
        initFrame();
        initComponents();
        
        setVisible(true);
    }

    
    private void initFrame() throws IOException{
        
        textIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/login_body.png")));
        text = new JLabel(textIc);
        text.setAlignmentX(CENTER_ALIGNMENT);
        
        setTextField();
        

        loginIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/login.png")));
        login = new JLabel(loginIc);
        backIc = new ImageIcon(ImageIO.read(getClass().getResource("graphics/back.png")));
        back = new JLabel(backIc);
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(login);
        b.add(back);
        b.add(Box.createHorizontalGlue());
        
        text.setAlignmentX(CENTER_ALIGNMENT);
        
        add(Box.createVerticalGlue());
        add(text);
        add(b);
        add(Box.createVerticalGlue());
        
    }
    
    private void setTextField(){
        user = new JTextField("Username",textColumnNum);
        user.setFont(f);
        user.setMaximumSize(user.getPreferredSize());
        user.setBackground(new Color(0,0,0,0));
        user.setBorder(null);
        
        pw = new JPasswordField("Password",textColumnNum);
        pw.setFont(f);
        pw.setMaximumSize(pw.getPreferredSize());
        pw.setBackground(new Color(0,0,0,0));
        pw.setBorder(null);
        
        text.setLayout(new BoxLayout(text, BoxLayout.PAGE_AXIS));
        
        Box b1 = Box.createHorizontalBox();
        b1.add(Box.createHorizontalGlue());
        b1.add(user);
        Box b2 = Box.createHorizontalBox();
        b2.add(Box.createHorizontalGlue());
        b2.add(pw);
        
        text.add(Box.createVerticalGlue());        
        text.add(b1);
        text.add(Box.createVerticalGlue());
        text.add(b2);
        text.add(Box.createVerticalGlue());
    }
    
    private void initComponents(){
        login.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                user.getText();
                pw.getText();
                //TBC
                
                
                performSegue(UIViewEnum.MENU);
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
