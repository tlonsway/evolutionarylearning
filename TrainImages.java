import java.util.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;
public class TrainImages {
    Network n;
    File bwdb;
    File colordb;
    public TrainImages(String bwdbname, String colordbname) {
        bwdb = new File(bwdbname);
        colordb = new File(colordbname);
    }
    public TrainImages(Network n, String bwdbname, String colordbname) {
        this.n = n;
        bwdb = new File(bwdbname);
        colordb = new File(colordbname);
    }
    public double getNetScore(Network n) {
        this.n = n;
        return compute();
    }
    public double compute() {
        double totalscore = 0;
        for(File f : bwdb.listFiles()) {
            if (!f.isDirectory()) {
                try {
                    BufferedImage bw = ImageIO.read(f);
                    BufferedImage col = ImageIO.read(new File(colordb.getName() + "/" + f.getName().substring(0,f.getName().indexOf(".")) + ".png"));
                    int newwidth = bw.getWidth() + (10-bw.getWidth()%10);
                    int newheight = bw.getHeight() + (10-bw.getHeight()%10);
                    BufferedImage resizebw = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_ARGB);
                    BufferedImage resizecol = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = resizebw.createGraphics();
                    Graphics2D g2 = resizecol.createGraphics();
                    g.drawImage(bw, 0, 0, null);
                    g2.drawImage(col, 0, 0, null);
                    g.dispose();
                    g2.dispose();
                    for(int x=0;x<resizebw.getWidth();x+=10) {
                        for(int y=0;y<resizebw.getHeight()/10;y+=10) {
                            BufferedImage cellbw = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
                            BufferedImage cellcol = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
                            for(int x2=x;x2<x+10;x2++) {
                                for(int y2=y;y2<y+10;y2++) {
                                    cellbw.setRGB(x2-x,y2-y,resizebw.getRGB(x2,y2));
                                    cellcol.setRGB(x2-x,y2-y,resizecol.getRGB(x2,y2));
                                }   
                            }
                            double[] inputs = new double[100];
                            int incr = 0;
                            for(int x2=0;x2<10;x2++) {
                                for(int y2=0;y2<10;y2++) {
                                    inputs[incr] = (double)(new Color(cellbw.getRGB(x2,y2)).getRed())/256;
                                    incr++;
                                }
                            }
                            double[] result = n.forward(inputs);
                            BufferedImage generated = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
                            incr = 0;
                            for(int x2=0;x2<10;x2++) {
                                for(int y2=0;y2<10;y2++) {
                                    int red = (int)(256.0*result[incr]);
                                    int green = (int)(256.0*result[incr+1]);
                                    int blue = (int)(256.0*result[incr+2]);
                                    Color newcol = new Color(red,green,blue);
                                    generated.setRGB(x2,y2,newcol.getRGB());
                                    incr+=3;
                                }
                            }
                            double imgdiff = imageDiff(cellcol, generated);
                            totalscore+=imgdiff;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load and asses files for score computation");
                    e.printStackTrace();
                    return 0;
                }
            }
        }
        return Long.MAX_VALUE-totalscore;
    }
    public static double imageDiff(BufferedImage i1, BufferedImage i2) {
        try {
            if (i1.getWidth() != i2.getWidth() || i1.getHeight() != i2.getHeight()) {
                System.out.println("Compared images have a different width or height, exiting comparison");
                return -1;
            }
            for(int x=0;x<i1.getWidth();x++) {
                for(int y=0;y<i1.getHeight();y++) {
                    Color c1 = new Color(i1.getRGB(x,y));
                    Color c2 = new Color(i2.getRGB(x,y));
                    int rdiff = c1.getRed()-c2.getRed();
                    int gdiff = c1.getGreen()-c2.getGreen();
                    int bdiff = c1.getBlue()-c2.getBlue();
                    int tdiff = rdiff + gdiff + bdiff;
                    return tdiff;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to examine differences between two images");
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
}