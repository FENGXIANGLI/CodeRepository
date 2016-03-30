import javax.swing.JPanel;

public class UIInterface extends JPanel {
    
    public void performSegue(UIViewEnum v){
        this.firePropertyChange("segueTo", UIViewEnum.NULL, v);
    }

}
