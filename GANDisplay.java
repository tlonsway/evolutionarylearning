import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;
import javafx.geometry.*;
public class GANDisplay extends JComponent {
    double[] pixels;
    int imagewidth = 28;
    int imageheight = 28;
    
    public GANDisplay() {
    }
    public void redraw() {
        super.repaint();
        this.repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int pxi = 0;
        for(int r=0;r<imageheight;r++) {
            for(int c=0;c<imagewidth;c++) {
                int gray = (int)pixels[pxi];
                if (gray>255) {
                    gray=255;
                }
                g.setColor(new Color(gray,gray,gray));
                g.fillRect(20*c, 20*r, 20, 20);
                pxi++;
            }
        }
    }
    public void setImage(double[] px) {
        pixels=px;
        this.redraw();
    }
}