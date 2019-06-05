import java.util.*;
public class Generation {
    int numnet;
    double topprop = .1; //proportion out of 1 of the number of networks to use as the "best"
    double mutaterate = .05; //proportion out of 1 of the number of the top networks to mutate
    Network[] networks;
    int[] lys;
    String activationfunction;
    String outputactivationfunction;
    public Generation(int networknum, int[] netlayers, double mrate, double tprop, String af, String oaf) {
        if (!(networknum>=20)) {
            System.out.println("FATAL ERROR: There must be at least 20 networks in a generation");
            System.exit(1);
        }
        activationfunction=af;
        outputactivationfunction=oaf;
        numnet=networknum;
        lys=netlayers;
        networks = new Network[numnet];
        for(int i=0;i<numnet;i++) {
            networks[i]=new Network(netlayers,af,oaf);
        }
        mutaterate=mrate;
        topprop=tprop;
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
    public Network getBestNet() {
        int bs = -1*Integer.MAX_VALUE;
        Network bestnet = null;
        for(Network n : networks) {
            if (n.getScore()>bs) {
                bs=(int)(n.getScore());
                bestnet=n;
            }
        }
        return bestnet;
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
            newnets.add(new Network(lys,activationfunction,outputactivationfunction));
        }   
        for(int i=0;i<numnet-50-numrandom;i++) {
            newnets.add(combine(oldnetworks[i%5],oldnetworks[(int)(Math.random()*oldnetworks.length)]));
            //newnets.add(combine(oldnetworks[i%5],oldnetworks[4-(i%5)]));
        }
        for(int i=0;i<oldnetworks.length;i++) {
            networks[i]=newnets.get(i);
        }
    }
    public void newNextGen() {
        sortGen();
        Network[] oldnets = new Network[(int)(numnet*topprop)]; //top percent of nets from previous
        ArrayList<Network> newnets = new ArrayList<Network>();
        //gather top percentage of scorers
        for(int i=0;i<oldnets.length;i++) {
            oldnets[i]=networks[i];
        }
        //add in the top scorers to the new generation
        for(Network n : oldnets) {
            newnets.add(mutate(n)); //add mutations of best networks
            newnets.add(new Network(lys,n.getWeights(),n.getActivationFunctions())); //direct copy of best networks to next generation
        }
        //genetically breed the top percentage to make a new full set of data
        int numfromprev = (int)((numnet-2*oldnets.length)/3);
        for(int i=0;i<numfromprev;i++) {
            //generate a new network
            int rand1 = (int)(Math.random()*oldnets.length);
            int rand2 = (int)(Math.random()*oldnets.length);
            newnets.add(combine(oldnets[rand1],oldnets[rand2]));
        }
        //mutations are automatically added to genetic combinations at mutation rate
        //add random new networks to add uniqueness
        for(int i=0;i<numnet-2*oldnets.length-numfromprev;i++) {
            newnets.add(new Network(lys,activationfunction,outputactivationfunction));
        }
        for(int i=0;i<networks.length;i++) {
            networks[i]=newnets.get(i);
        }
        //System.out.println("NETSIZE: " + networks.length);
        //System.out.println("GENSIZE: " + newnets.size());
    }
    public void nextGenRand() {
        ArrayList<Network> newnets = new ArrayList<Network>();
        sortGen();
        newnets.add(networks[0]);
        newnets.add(networks[1]);
        for(int i=0;i<numnet-2;i++) {
            newnets.add(new Network(lys,activationfunction,outputactivationfunction));
        }
        for(int i=0;i<networks.length;i++) {
            networks[i]=newnets.get(i);
        }
    }
    public void nextGenSemiRand() {
        sortGen();
        Network[] oldnets = new Network[(int)(numnet*topprop)]; //top percent of nets from previous
        ArrayList<Network> newnets = new ArrayList<Network>();
        //gather top percentage of scorers
        for(int i=0;i<oldnets.length;i++) {
            oldnets[i]=networks[i];
        }
        //add in the top scorers to the new generation
        for(Network n : oldnets) {
            //newnets.add(mutate(n)); //add mutations of best networks
            newnets.add(new Network(lys,n.getWeights(),n.getActivationFunctions())); //direct copy of best networks to next generation
        }
        int amt=0;
        int amtmutate = (numnet-oldnets.length)/2;
        int amtrandom = (numnet-oldnets.length)-amtmutate;
        while(amt<amtmutate) {
            for(Network n : oldnets) {
                if (amt<numnet-oldnets.length) {
                    newnets.add(mutate(n));
                    amt++;
                }
            }
        }
        for(int i=0;i<amtrandom;i++) {
            newnets.add(new Network(lys,activationfunction,outputactivationfunction));
        }
        for(int i=0;i<networks.length;i++) {
            networks[i]=newnets.get(i);
        }
        //new generation consists of previous best, and all others are mutated permutations of the previous best, no randoms or combinations used
    }
    public Network combine(Network one, Network two) {
        boolean mutate1 = false;
        if (Math.random()<mutaterate) {
            mutate1=true;
        }
        ArrayList<double[]> newnet = new ArrayList<double[]>();
        for(int i=0;i<lys.length-1;i++) {
            newnet.add(new double[lys[i]*lys[i+1]]);
        }
        for(int i=0;i<lys.length-1;i++) {
            for(int i2=0;i2<lys[i]*lys[i+1];i2++) {
                int choice = (int)(Math.random()*2);
                boolean mutate=false;
                if (Math.random()<mutaterate) {
                    mutate=true;
                }
                if (mutate1&&mutate) {
                    choice=-1;
                    double num = (Math.random()*2)-1;
                    newnet.get(i)[i2] = num;
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
        return (new Network(lys,newnet,one.getActivationFunctions()));
    }
    public Network mutate(Network n) {
        ArrayList<double[]> newnet = new ArrayList<double[]>();
        for(int i=0;i<lys.length-1;i++) {
            newnet.add(new double[lys[i]*lys[i+1]]);
        }
        for(int i=0;i<lys.length-1;i++) {
            for(int i2=0;i2<lys[i]*lys[i+1];i2++) {
                boolean mutate=false;
                if (Math.random()<mutaterate) {
                    mutate=true;
                }
                if (mutate) {
                    double num = (Math.random()*2)-1;
                    newnet.get(i)[i2] = num;
                } else {
                    newnet.get(i)[i2]=n.getWeights().get(i)[i2];
                }
            }
        }
        Network ret = new Network(lys,newnet,n.getActivationFunctions());
        /*System.out.println("before");
        n.printNet();
        System.out.println("after");
        ret.printNet();*/
        return ret;
    }
    public void setNets(Network[] n) {
        networks=n;
    }
}