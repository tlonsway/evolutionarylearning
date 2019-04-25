import java.util.*;
public class Generation {
    int numnet;
    Network[] networks;
    int[] lys;
    public Generation(int networknum, int[] netlayers) {
        numnet=networknum;
        lys=netlayers;
        networks = new Network[numnet];
        for(int i=0;i<numnet;i++) {
            networks[i]=new Network(netlayers);
        }
    }
    public void sortGen() {
        ArrayList<Network> nets = new ArrayList<Network>();
        for(Network n : networks) {
            nets.add(n);
        }
        Collections.sort(nets);
        for(int i=0;i<networks.length;i++) {
            networks[i]=nets.get(i);
        }
    }
    public Network[] getNets() {
        return networks;
    }
    public int getBestScore() {
        int bs = 0;
        for (Network n : networks) {
            if (n.getScore()>bs) {
                bs=(int)n.getScore();
            }
        }
        return bs;
    }
    public int getBestScore2() {
        int bs = 0;
        for (Network n : networks) {
            if (n.getScore2()>bs) {
                bs=(int)n.getScore2();
            }
        }
        return bs;
    }
    public int getGenAverageScore1() {
        int sum=0;
        for(Network n : networks) {
            sum+=n.getScore();
        }
        return (int)(sum/networks.length);
    }
    public int getGenAverageScore2() {
        int sum=0;
        for(Network n : networks) {
            sum+=n.getScore2();
        }
        return (int)(sum/networks.length);
    }
    public void nextGen() {
        sortGen();
        //System.out.println("BEST GEN SCORE: " + networks[0].getScore());
        Network[] oldnetworks=networks;
        ArrayList<Network> newnets = new ArrayList<Network>();
        int numrandom = 150;
        for(int i=0;i<50;i++) {
            newnets.add(oldnetworks[i]);
        }
        for(int i=0;i<numrandom;i++) {
            newnets.add(new Network(lys));
        }   
        for(int i=0;i<numnet-50-numrandom;i++) {
            newnets.add(combine(oldnetworks[i%5],oldnetworks[(int)(Math.random()*oldnetworks.length)]));
            //newnets.add(combine(oldnetworks[i%5],oldnetworks[4-(i%5)]));
        }
        for(int i=0;i<oldnetworks.length;i++) {
            networks[i]=newnets.get(i);
        }
    }
    public void nextGenRand() {
        ArrayList<Network> newnets = new ArrayList<Network>();
        sortGen();
        newnets.add(networks[0]);
        newnets.add(networks[1]);
        for(int i=0;i<numnet-2;i++) {
            newnets.add(new Network(lys));
        }
        for(int i=0;i<networks.length;i++) {
            networks[i]=newnets.get(i);
        }
    }
    public Network combine(Network one, Network two) {
        ArrayList<double[]> newnet = new ArrayList<double[]>();
        for(int i=0;i<lys.length-1;i++) {
            newnet.add(new double[lys[i]*lys[i+1]]);
        }
        for(int i=0;i<lys.length-1;i++) {
            for(int i2=0;i2<lys[i]*lys[i+1];i2++) {
                int choice = (int)(Math.random()*2);
                int mutation = (int)(Math.random()*2000);
                boolean mutate=false;
                if (mutation<1) {
                    mutate=true;
                    choice=-1;
                }
                if (mutate) {
                    newnet.get(i)[i2]=Math.random();
                }
                //System.out.println("i: " + i);
                //System.out.println("i2: " + i2);
                //System.out.println("choice: " + choice);
                if(choice==0) {
                    newnet.get(i)[i2]=one.getWeights().get(i)[i2];
                } else if (choice==1) {
                    newnet.get(i)[i2]=two.getWeights().get(i)[i2];
                }
            }
        }
        return (new Network(lys,newnet));
    }
    public void setNets(Network[] n) {
        networks=n;
    }
}