import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class ColorizerDisplay extends JPanel {
    File sample;
    Colorizer c;
    public ColorizerDisplay() {
        sample = new File("sample.png");
    }
    public ColorizerDisplay(Colorizer c) {
        this.c = c;
        sample = new File("sample.png");
    }
    public void redraw() {
        this.repaint();
    }
    public void paintComponent(Graphics g){
        if (c != null) {
            try {
                super.paintComponent(g);
                BufferedImage sampleBW = ImageIO.read(sample);
                g.drawImage(sampleBW, 0, 0, this);
                BufferedImage colorized = c.colorBW(sampleBW);
                g.drawImage(colorized,sampleBW.getWidth()+50,0,this);
            } catch (Exception e) {
                System.out.println("Failed to draw frame");
                e.printStackTrace();
                return;
            }
        }
    }
    public void setColorizer(Colorizer c) {
        this.c = c;
        this.redraw();
    }
}
