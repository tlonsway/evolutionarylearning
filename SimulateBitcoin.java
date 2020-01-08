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
    public int simulateImproved(Network n) {
        long totalscore = 0;
        for(int i=0;i<prices.size()-ins-outs;i++) {
            double[] netin = new double[ins];
            for(int i2=i;i2<i+ins;i2++) {
                netin[i2-i]=(prices.get(i2));
            }
            double[] netout = n.forward(netin);
            for(int v=0;v<netout.length;v++) {
                double dis = prices.get(v+i+ins);
                
            }
            
        }     
        return (int)totalscore;
    }
}