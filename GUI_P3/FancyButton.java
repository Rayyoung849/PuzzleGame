import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class FancyButton extends JButton {
    public FancyButton() {

         addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
         });{

         }
    }
}
