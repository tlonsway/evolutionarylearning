import java.util.*;
public class SimulateBitcoin {
    ArrayList<Double> prices;
    final int NORMALIZEPOINT = 25000;
    int ins = 24;
    int outs = 1;
    public SimulateBitcoin() {
        prices=FileDataReader.btcHour();
    }
    public int simulate(Network n) {
        //for each set of 48, 
        long totalscore=0;
        for(int i=0;i<(prices.size()/1000)-ins-outs;i++) {
            double[] netin = new double[ins];
            for(int i2=i;i2<i+ins;i2++) {
                netin[i2-i]=(prices.get(i2));
            }
            double[] netout = n.forward(netin);
            //de-normalize output of normalized data
            for(int v=0;v<netout.length;v++) {
                double dis = Math.abs(prices.get(v+i+ins)-(netout[v]));
                if (dis>=2500) {
                    totalscore-=75;
                } else if (dis<=10) {
                    totalscore+=2500;
                } else if (dis<=100) {
                    totalscore+=1000;
                } else if (dis<=250) {
                    totalscore+=500;
                } else if (dis<=500) {
                    totalscore+=100;
                } else if (dis<=1000) {
                    totalscore+=50;
                }            
                //System.out.println("distance was: " + dis + " - guess was: " + (NORMALIZEPOINT*(netout[v])) + " - correct answer was: " + (prices.get(v+i+48)));
            }
        }
        //System.out.println("network achieved score of: " + totalscore);
        return (int)totalscore;
    }
    public int simulateImproved(Network n, boolean verbose) {
        long totalscore = 0;
        int numcorrect = 0;
        int numtotal = 0;
        for(int i=0;i<prices.size()-ins-outs-1;i++) {
            double[] netin = new double[ins];
            for(int i2=i;i2<i+ins;i2++) {
                netin[i2-i]=(prices.get(i2));
            }
            double netout = n.forward(netin)[0]*100;              
            double change = prices.get(i+ins+1)-prices.get(i+ins);
            int changedir = -1; //-1 down;0 same,1 up
            if (change>0) {
                changedir = 1;
            } else if (change==0) {
                changedir = 0;
            }
            int preddir = -1; //-1 down;0 same;1 up
            if (netout>0) {
                preddir = 1;
            } else if (netout==0) {
                preddir = 0;
            }
            if (changedir == preddir) {
                numcorrect++;
                totalscore+= 1;
            }
            numtotal++;
        }     
        if (verbose) {
            System.out.println(numcorrect + "/" + numtotal + " correct predictions");
        }
        return (int)totalscore;
    }
}