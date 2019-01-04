import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class TrainBitcoin {
    public static void main(String[] args) {
        int numgens,netspergen,hidl_num;
        numgens=netspergen=hidl_num=0;
        
        if (args.length==0) {
            numgens=10000;
            netspergen=40;
            hidl_num=64;
        } else if (args.length!=3) {
            System.out.println("incorrect args supplied - format follows: program.jar numgens netspergen hidl_num");
            System.exit(0);
        } else {
            numgens=Integer.parseInt(args[0]);
            netspergen=Integer.parseInt(args[1]);
            hidl_num=Integer.parseInt(args[2]);
        }
        
        //add display of current settings
        
        String calcStartTime = printDate();
        
        System.out.println("Starting calculation at " + calcStartTime);
        System.out.println("\nCurrent network settings");
        System.out.println("numgens: " + numgens + "\nnetspergen: " + netspergen + "\nhidl_num: " + hidl_num + "\n\n");
        
        
        SimulateBitcoin p = new SimulateBitcoin();
        
        int[] layers = {48,hidl_num,hidl_num,24};
        Generation g = new Generation(netspergen,layers);
        int bestscore=-10000000;
        Network bestnet = null;
        int genamt=numgens;
        long startTime=java.lang.System.currentTimeMillis();
        BitcoinThread[] btcthreads = new BitcoinThread[netspergen];
        for(int i=0;i<netspergen;i++) {
            btcthreads[i]=new BitcoinThread();
        }
        for(int i=0;i<genamt;i++) {
            btcthreads = new BitcoinThread[netspergen];
            for(int i2=0;i2<netspergen;i2++) {
                btcthreads[i2]=new BitcoinThread();
            }
            Network[] nets = g.getNets();

            /*
            for(Network n : nets) {
                
                
                int score = p.simulate(n);
                if (score>bestscore) {
                    bestscore=score;
                    System.out.println("new best score of " + bestscore);
                    //System.out.println("BEST NETWORK WEIGHTS:");
                    //n.printNet(); 
                    bestnet=n;
                }
                n.setScore(score);
            }
            */
            
            for(int ni=0;ni<nets.length;ni++) {
                btcthreads[ni].setNet(nets[ni]);
                btcthreads[ni].start();                
            }
            for(int ni=0;ni<nets.length;ni++) {
                try {
                    btcthreads[ni].join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for(int ni=0;ni<nets.length;ni++) {
                int netscore = btcthreads[ni].getScore();
                if (netscore>bestscore) {
                    bestscore=netscore;
                    bestnet=btcthreads[ni].getNet();
                    System.out.println("new best score of " + bestscore);
                }
                nets[ni].setScore(netscore);
            }
            
            g.sortGen();
            Network genBest = g.getNets()[0];
            g.setNets(nets);
            if (i!=genamt-1) {
                g.nextGen();
            }
            if (i%(genamt/100)==0) {
                System.out.println(((double)i/(double)genamt)*100 + "% complete with network");
            }
            if (i==2) {
                long seconds = (long)(((((double)(java.lang.System.currentTimeMillis()-startTime))/3)/1000)*(double)genamt);
                int day = (int)TimeUnit.SECONDS.toDays(seconds);        
                long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
                long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
                long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
                System.out.println("Predicted computation time: " + day + " days " + hours + " hours " + minute + " minutes " + second + " seconds");
            }
        }
        g.sortGen();
        System.out.println("simulating final network - with a score of " + bestnet.getScore());
        bestnet.printNet();
        int sum=0;
        System.out.println("testing best network");
        for(int i=0;i<1;i++) {
            sum+=p.simulate(g.getNets()[0]);
        }
        System.out.println("bestnet score is " + (sum/1));
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
            System.out.println("$" + (((double)((int)((netout[i]*25000)*100)))/100));
        }
        
        System.out.println("\nCalculation started at " + calcStartTime);
        System.out.println("\nCalculation complete at " + printDate());
    }
    public static String printDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }
}