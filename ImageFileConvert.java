import java.util.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;
public class ImageFileConvert {
    //this class is created to convert colored images to b&w
    public static void main(String[] args) {
        File colordb = new File("color");
        File bwdb = new File("fullcolor");
        for(File f : colordb.listFiles()) {
            if (!f.isDirectory()) {
                try {
                    BufferedImage img = ImageIO.read(f);
                    BufferedImage bw = removeColor(img);
                    File wr = new File(bwdb.getName()+"/"+f.getName().substring(0,f.getName().indexOf(".")) + ".png");
                    ImageIO.write(bw, "png", wr);
                } catch (Exception e) {
                    System.out.println("Failed to load and write image");
                    e.printStackTrace();
                }
            }
        }
    }
    public static BufferedImage removeColor(BufferedImage in) {
        //BufferedImage gr = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage gr = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = gr.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        return gr;
    }
}