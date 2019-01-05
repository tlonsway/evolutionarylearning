import java.util.*;
public class BitcoinThread extends Thread {
    ArrayList<Double> prices;
    //ArrayList<Double> pricesnormal;
    final int NORMALIZEPOINT = 25000;    
    Network n;
    long totalscore;
    int ins = 24;
    int outs = 2;
    public BitcoinThread(Network net) {
        prices=FileDataReader.btcHour();
        n=net;
    }
    public BitcoinThread() {
        prices=FileDataReader.btcHour();
    }
    public BitcoinThread(ArrayList<Double> normalprices) {
        prices=normalprices;
    }
    public void run() {
        //for each set of 48, 
        totalscore=0;        
        for(int i=0;i<(prices.size()/1)-ins-outs;i++) {
            double[] netin = new double[ins];
            for(int i2=i;i2<i+ins;i2++) {
                netin[i2-i]=(prices.get(i2))/NORMALIZEPOINT;
            }
            double[] netout = n.forward(netin);
            //de-normalize output of normalized data
            for(int v=0;v<netout.length;v++) { 
                double dis = Math.abs(prices.get(v+i+ins)-(NORMALIZEPOINT*netout[v]));
                if (dis>=2500) {
                    totalscore-=75;
                } else if (dis<=10) {
                    totalscore+=1000;
                } else if (dis<=100) {
                    totalscore+=100;
                } else if (dis<=250) {
                    totalscore+=50;
                } else if (dis<=500) {
                    totalscore+=65;
                } else if (dis<=1000) {
                    totalscore+=5;
                }            
                //System.out.println("distance was: " + dis + " - guess was: " + (NORMALIZEPOINT*(netout[v])) + " - correct answer was: " + (prices.get(v+i+48)));
            }
        }
        //System.out.println("network achieved score of: " + totalscore);
    }
    public int getScore() {
        return (int)totalscore;
    }
    public void setNet(Network net) {
        n=net;
    }
    public Network getNet() {
        return n;
    }
}