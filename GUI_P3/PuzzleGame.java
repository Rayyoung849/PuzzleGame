import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class PuzzleGame extends JFrame {
    JPanel panel;
    private final int NUMROWS = 4, NUMCOLS = 3;
    private final int WIDTH = 800;
    private int HEIGHT = 800;
    private List<FancyButton> buttons;
    private List<FancyButton> buttonSolution;
    private BufferedImage image;
    private BufferedImage resizedImage;

    public PuzzleGame() {
        super("Puzzle Game"); // setTitle("Puzzle Game")

        panel = new JPanel(); // create a new instance of JPanel
        panel.setLayout(new GridLayout(NUMROWS, NUMCOLS));
        add(panel);

        try {
            image = loadImage();

            // resize the image to fit our given Width
            int sourceWidth = image.getWidth();
            int sourceHeight = image.getHeight();
            // resize the height so height/width = sourceHeight/sourceWidth
            HEIGHT = (int) ((double) sourceHeight / sourceWidth * WIDTH);
            resizedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            var graphics = resizedImage.createGraphics();
            graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
            graphics.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error loading this title", JOptionPane.ERROR_MESSAGE);
        }

        buttons = new ArrayList<FancyButton>();
        // instantiate buttons and add to them to the list
        for (int i = 0; i < NUMCOLS * NUMROWS; i++) {

            int row = i / NUMCOLS, col = i % NUMCOLS;

            Image imageSlice = createImage(new FilteredImageSource(resizedImage.getSource(),
            new CropImageFilter(col*WIDTH/NUMCOLS, row*HEIGHT/NUMROWS, WIDTH/NUMCOLS, HEIGHT/NUMROWS)));

            FancyButton btn = new FancyButton(); // instantiate our button

            if (i == NUMCOLS * NUMROWS - 1) { // the last button
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
            }
            else {
                btn.setIcon(new ImageIcon(imageSlice));
            }


            buttons.add(btn); // add to our button list
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            btn.addActionListener(e -> MyClickEventHandler(e));
        }

        buttonSolution = List.copyOf(buttons);
        Collections.shuffle(buttons); // shuffle the buttons

        for(var btn: buttons){
            panel.add(btn); // add to our panel
        }

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void MyClickEventHandler(ActionEvent e) {
        FancyButton btnClicked = (FancyButton)e.getSource(); // what button was clicked
        int i = buttons.indexOf(btnClicked); // get the buttons index

        // where is our button at in the grid - which row and column 
        int row = i / NUMCOLS;
        int col = i % NUMCOLS;

        int iempty = -1;
        for(int j =0; j < buttons.size(); j++){
            if(buttons.get(j).getIcon() == null){ // this tells us that we've found the empty button
                iempty = j;
                break;
            }
        }

        // where is our empty button at in the grid - which row and column 
        int rowEmpty = iempty / NUMCOLS;
        int colEmpty = iempty % NUMCOLS;

        // check if the clicked button is adjacent (same row + adj colm, or same colm and adjacent row)
        if((row == rowEmpty && Math.abs(col - colEmpty) == 1) || // || is the or operator
         (col == colEmpty && Math.abs(row - rowEmpty) == 1)){
            Collections.swap(buttons, i, iempty);
            updateButton();
         }
         //check for solution
    if(buttonSolution.equals(buttons)){
        JOptionPane.showMessageDialog(null, "Well done!");
    }

    }

    

    public void updateButton(){
        panel.removeAll();
        // readd all buttons to the panel
        for(var btn: buttons){
            panel.add(btn);
        }

        //reload the panel
        panel.validate();
    }
    
    private BufferedImage loadImage() throws IOException {
        return ImageIO.read(new File("Planet1.jpg"));
    }
}
