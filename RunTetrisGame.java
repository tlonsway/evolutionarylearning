import javax.swing.*;
import java.util.*;
public class RunTetrisGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Window");
        frame.setVisible(true);
        frame.setSize(1000,1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Tetris t = new Tetris();
        frame.add(t);
        t.setVisible(true);
        int[] layers = {31,55,55,55,5};
        int netspergen = 500;
        int numgens = 5000;
        boolean dispgenbest = true;
        int gentimeformutationchange = 10;
        double startingmutation = .05;
        double modifiedmutationrate = startingmutation;
        int mutationmoddir = 1; //-1 for decrease, 0 for static, 1 for increase
        int genssincebest = 0;
        Generation g = new Generation(netspergen,layers,startingmutation,.2,"relu","relu");
        int bestscore = -1*Integer.MAX_VALUE;
        Network bestnet = null;
        int genamt=numgens;
        
        System.out.println("EVOLUTIONARY TETRIS AI | TRISTAN LONSWAY");
        System.out.println("code available at https://github.com/Kryptikz/evolutionarylearning");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("NETWORK SETTINGS");
        System.out.println("NETSPERGEN: " + netspergen);
        System.out.println("NUMGENS: " + numgens);
        System.out.println("MUTATIONCHANGEWAIT: " + gentimeformutationchange);
        System.out.println("STARTING MUTATION RATE: " + startingmutation);
        System.out.println("STARTING MOD DIR: " + mutationmoddir);
        System.out.println("ACTIVATION FUNCTION: " + "relu/relu");
        
        for(int i=0;i<genamt;i++) {
            System.out.println("GEN: " + i + "/" + genamt);
            Network[] nets = g.getNets();
            for(Network n : nets) {
                int sum=0;
                for(int i2=0;i2<2;i2++) {
                    int[] scoreb = t.simulate(n,false,true,false,-1,i);
                    sum+=scoreb[0];
                }
                int score = (int)(sum/2);
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
                    
                    genssincebest=0;
                }
                n.setScore(score);
            }
            
            if (modifiedmutationrate>=.25) {
                //increase or decrease modified by 5% of original each time
                System.out.println("mutationrate hit a maximum at .25, now decreasing");
                mutationmoddir=-1;
            }
            if (modifiedmutationrate<=0) {
                System.out.println("mutationrate hit a minimum at 0, now increasing");
                mutationmoddir=1;
            }
            if (genssincebest>gentimeformutationchange) {
                if (mutationmoddir==1) {
                    modifiedmutationrate+=(.05*startingmutation);
                } else if (mutationmoddir==-1) {
                    modifiedmutationrate-=(.05*startingmutation);
                }
                System.out.println("MUTATION RATE MODIFIED: " + modifiedmutationrate);
                g.setMutationRate(modifiedmutationrate);
                genssincebest=0;
            }
            
            if (modifiedmutationrate>=startingmutation*5) {
                //increase or decrease modified by 5% of original each time
                System.out.println("mutationrate hit a maximum, now decreasing");
                mutationmoddir=-1;
            }
            if (modifiedmutationrate<=startingmutation*-5) {
                System.out.println("mutationrate hit a minimum, now increasing");
                mutationmoddir=1;
            }
            if (genssincebest>gentimeformutationchange) {
                
                
            }
            
            g.sortGen();
            Network genBest = g.getNets()[0];
            if (dispgenbest&&i%1==0) {
                t.simulate(genBest,true,true,true,-1,i);
            }
            if (i!=genamt-1) {
                g.newNextGen();
            }
            genssincebest++;
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