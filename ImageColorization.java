import javax.swing.*;
import java.util.*;
public class ImageColorization {
    public static void main(String[] args) {
        //colorize b&w images
        JFrame frame = new JFrame("Colorizer Window");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ColorizerDisplay display = new ColorizerDisplay();
        frame.add(display);
        display.setVisible(true);
        
        String bwdbname = "bw";
        String colordbname = "fullcolor";
        
        TrainImages train = new TrainImages(bwdbname,colordbname);
        
        int[] lys = {100,100,100,300};
        int numgens = 500;
        int netspergen = 100;
        Generation g = new Generation(netspergen,lys,.005,.3,"sigmoid","sigmoid");
        double bestscore=-1;
        Network bestnet = null;
        
        for(int i=0;i<numgens;i++) {
            System.out.println("GEN: " + i + "/" + numgens);
            Network[] nets = g.getNets();
            for(Network n : nets) {
                double score = train.getNetScore(n);
                if (score>bestscore) {
                    bestscore=score;
                    System.out.println("new best score of " + bestscore);
                    if (bestnet!=null) {
                        System.out.println("comparing new bestnet with previous");
                        Network.compareNets(bestnet, n);
                    }
                    bestnet=n;
                }
                n.setScore(score);
            }
            System.out.println("***");
            g.sortGen();
            Network genBest = g.getNets()[0];
            genBest.saveNet("net_v0.1_" + i + ".cln");
            display.setColorizer(new Colorizer(genBest));
            if (i!=numgens-1) {
                g.newNextGen();
            }
        }
        
        
    }   
}