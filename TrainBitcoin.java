public class TrainBitcoin {
    public static void main(String[] args) {
        int numgens,netspergen,dispgenbest,hidl_num;
        numgens=netspergen=dispgenbest=hidl_num=0;
        
        numgens=3000;
        netspergen=50;
        dispgenbest=0;
        hidl_num=96;
        
        SimulateBitcoin p = new SimulateBitcoin();
        
        int[] layers = {48,hidl_num,24};
        Generation g = new Generation(netspergen,layers);
        int bestscore=-1000000;
        Network bestnet = null;
        int genamt=numgens;
        for(int i=0;i<genamt;i++) {
            Network[] nets = g.getNets();
            for(Network n : nets) {
                int score = p.simulate(n);;
                if (score>bestscore) {
                    bestscore=score;
                    System.out.println("new best score of " + bestscore);
                    //System.out.println("BEST NETWORK WEIGHTS:");
                    //n.printNet(); 
                    bestnet=n;
                }
                n.setScore(score);
            }
            g.sortGen();
            Network genBest = g.getNets()[0];
            g.setNets(nets);
            if (i!=genamt-1) {
                g.nextGen();
            }
            if (i%50==0) {
                System.out.println(((double)i/(double)genamt)*100 + "% complete with network");
            }
        }
        g.sortGen();
        System.out.println("simulating final network - with a score of " + bestnet.getScore());
        bestnet.printNet();
        int sum=0;
        System.out.println("testing best network 1,000 times");
        for(int i=0;i<1000;i++) {
            sum+=p.simulate(g.getNets()[0]);
        }
        System.out.println("average bestnet score is " + (sum/1000));
        /*while(true) {
            int score=p.simulate(bestnet,true,false,true)[0];
            System.out.println("bestnet scored: " + score);
        }*/
        double[] testarr = new double[]{2482.09,2489.09,2483.72,2481.72,2499.98,2497.61,2498.92,2490.83,2491.4,2500,2500.97,2516.66,2517.01,2525,2504.09,2492.61,2490.08,2496.37,2476.41,2463.86,2442.99,2435.62,2440.87,2449.6,2436.07,2457.54,2461.99,2431.95,2421.55,2439.97,2412,2404.99,2407.81,2409.9,2391.87,2423.63,2424.99,2416.62,2408.25,2435.99,2425.94,2445.99,2460.01,2467.83,2459.35,2454.43,2488.43,2509.17};
        //expected output should include 2505.56
        for(int i=0;i<testarr.length;i++) {
            testarr[i]=testarr[i]/25000;
        }
        System.out.println("\n\n\nTest out the best network with testarr");
        System.out.println("\nprevious 48 hours:\n");
        for(int i=0;i<testarr.length;i++) {
            System.out.println("$" + testarr[i]*25000);
        }
        System.out.println("\n\nnext 24 hours prediction:\n");
        double[] netout = bestnet.forward(testarr);
        for(int i=0;i<netout.length;i++) {
            System.out.println("$" + netout[i]*25000);
        }
    }
}