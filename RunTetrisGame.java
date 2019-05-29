import javax.swing.*;
import java.util.*;
public class RunTetrisGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Window");
        frame.setVisible(true);
        frame.setSize(1000,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Tetris t = new Tetris();
        frame.add(t);
        t.setVisible(true);
        int[] layers = {1012,18,18,5};
        int netspergen = 1500;
        int numgens = 500;
        boolean dispgenbest = true;
        Generation g = new Generation(netspergen,layers,.02,.2,"relu","relu");
        int bestscore = -1;
        Network bestnet = null;
        int genamt=numgens;
        for(int i=0;i<genamt;i++) {
            System.out.println("GEN: " + i + "/" + genamt);
            Network[] nets = g.getNets();
            for(Network n : nets) {
                int sum=0;
                for(int i2=0;i2<5;i2++) {
                    int[] scoreb = t.simulate(n,false,true,false,-1,i);
                    sum+=scoreb[0];
                }
                int score = (int)(sum/5);
                if (score>bestscore) {
                    bestscore=score;
                    System.out.println("new best score of " + bestscore);
                    //System.out.println("BEST NETWORK WEIGHTS:");
                    //n.printNet();
                    if (bestnet!=null) {
                        System.out.println("comparing new bestnet with previous");
                        Network.compareNets(bestnet,n);
                    }
                    bestnet=n;
                }
                n.setScore(score);
            }
            g.sortGen();
            Network genBest = g.getNets()[0];
            if (dispgenbest&&i%1==0) {
                t.simulate(genBest,true,true,true,-1,i);
            }
            if (i!=genamt-1) {
                g.newNextGen();
            }
        }
        g.sortGen();
        System.out.println("simulating final network - with a score of " + bestnet.getScore());
        bestnet.printNet();
        int sum=0;
        System.out.println("testing best network 1,000 times");
        for(int i=0;i<1000;i++) {
            sum+=t.simulate(bestnet,false,true,false,0,0)[0];
        }
        System.out.println("average bestnet score is " + (sum/1000));
        while(true) {
            int score=t.simulate(bestnet,true,true,true,20000,genamt)[0];
            System.out.println("bestnet scored: " + score);
        }
    }
}