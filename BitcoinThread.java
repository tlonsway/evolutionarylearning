import java.util.*;
public class BitcoinThread extends Thread {
    ArrayList<Double> prices;
    //ArrayList<Double> pricesnormal;
    final int NORMALIZEPOINT = 25000;    
    Network n;
    long totalscore;
    int ins = 24;
    int outs = 1;
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
        int correct = 0;
        for(int i=0;i<(prices.size())-ins-outs;i++) {
            double[] netin = new double[ins];
            for(int i2=i;i2<i+ins;i2++) {
                netin[i2-i]=(prices.get(i2));
            }
            double[] netout = n.forward(netin);
            double expect1=0;
            double expect2=0;
            if (prices.get(i+ins)>prices.get(i+ins-1)) {
                expect1=1; //price will increase
            } else {
                expect2=1; //price will decrease
            }
            if (netout[0]>netout[1]&&expect1==1) {
                totalscore+=(int)(100*(netout[0]/netout[1]));
                //totalscore+=100;
                correct++;
                //totalscore-=(int)(25*netout[1]);
            } else if (netout[1]>netout[0]&&expect2==1) {
                totalscore+=(int)(100*(netout[1]/netout[0]));
                //totalscore+=100;
                correct++;
                //totalscore-=(int)(25*netout[0]);
            } else {
                totalscore-=1000;
            }
            //de-normalize output of normalized data
            /*for(int v=0;v<netout.length;v++) { 
                double dis = Math.abs(prices.get(v+i+ins)-(netout[v]));
                //System.out.println(dis + " - actual:" + prices.get(v+i+ins) + " guess:" + (NORMALIZEPOINT*netout[v]));
                if (dis>=2500) {
                    totalscore-=10000;
                } else if (dis<=10) {
                    totalscore+=2000;
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
            }*/
        }
        System.out.println("network got " + correct + " correct predictions, out of " + prices.size());
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