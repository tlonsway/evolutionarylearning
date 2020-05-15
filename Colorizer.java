import java.awt.image.*;
import java.awt.*;
public class Colorizer {
    Network n;
    public Colorizer(Network n) {
        this.n = n;
    }
    public BufferedImage colorBW(BufferedImage in) {
        int newwidth = in.getWidth() + (10-in.getWidth()%10);
        int newheight = in.getHeight() + (10-in.getHeight()%10);
        BufferedImage resizebw = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizebw.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        BufferedImage wholeImage = new BufferedImage(resizebw.getWidth(), resizebw.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x=0;x<resizebw.getWidth();x+=10) {
            for(int y=0;y<resizebw.getHeight()/10;y+=10) {
                BufferedImage cellbw = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
                for(int x2=x;x2<x+10;x2++) {
                    for(int y2=y;y2<y+10;y2++) {
                        cellbw.setRGB(x2-x,y2-y,resizebw.getRGB(x2,y2));
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
                        wholeImage.setRGB(x+x2,y+y2,newcol.getRGB());
                        incr+=3;
                    }
                }
            }
        }
        return wholeImage;
    }
}