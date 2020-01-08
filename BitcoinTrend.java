import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
public class BitcoinTrend {
    public static void main(String[] args) {
        int numgens,netspergen,dispgenbest,hidl_num,dispgraphs,graphwidth,graphheight;
        numgens=netspergen=dispgenbest=hidl_num=dispgraphs=graphwidth=graphheight=0;
        if (args.length != 7 && args.length!=0) {
            System.out.println("INCORRECT ARGUMENT AMOUNT SUPPLIED.");
            System.out.println("PROPER FORMAT IS: " + "(number of gens) (number of networks per gen) (1/0 should display best each gen) (number of neurons in hidden layer) (1/0 display graphs) (width of graphs) (height of graphs)");
            System.exit(0);
        }
        if (args.length==7) {
            numgens=Integer.parseInt(args[0]);
            netspergen=Integer.parseInt(args[1]);
            dispgenbest=Integer.parseInt(args[2]);
            hidl_num=Integer.parseInt(args[3]);
            dispgraphs=Integer.parseInt(args[4]);
            graphwidth=Integer.parseInt(args[5]);
            graphheight=Integer.parseInt(args[6]);
        }
        if (args.length==0) {
            System.out.println("no arguments supplied, using default settings");
            numgens=75; //750
            netspergen=250; //30
            dispgenbest=1;
            hidl_num=18;
            dispgraphs=1;
            graphwidth=400;
            graphheight=420;
        }
        StatisticsGraph arg,brg,bog;
        arg=brg=bog=null;
        if (dispgraphs==1) {
            JFrame averawgraph = new JFrame("Average raw score over generations");
            averawgraph.setSize(graphwidth,graphheight);
            averawgraph.setResizable(false);
            averawgraph.setVisible(true);
            averawgraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            arg = new StatisticsGraph(averawgraph,graphwidth,graphheight);        
            averawgraph.add(arg);
            arg.setVisible(true);  
            arg.addPoint(0,0);   
            
            /*JFrame ave2graph = new JFrame("Average specific score over generations");
            ave2graph.setSize(graphwidth,graphheight);
            ave2graph.setResizable(false);
            ave2graph.setVisible(true);
            ave2graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            a2r = new StatisticsGraph(ave2graph,graphwidth,graphheight);
            ave2graph.add(a2r);
            a2r.setVisible(true);
            a2r.addPoint(0,0);*/
            
            JFrame bestrgraph = new JFrame("Best raw score over generations");
            bestrgraph.setSize(graphwidth,graphheight);
            bestrgraph.setResizable(false);
            bestrgraph.setVisible(true);
            bestrgraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            brg = new StatisticsGraph(bestrgraph,graphwidth,graphheight);
            bestrgraph.add(brg);
            brg.setVisible(true);
            brg.addPoint(0,0);
            
            /*JFrame best2graph = new JFrame("Best specific score over generations");
            best2graph.setSize(graphwidth,graphheight);
            best2graph.setResizable(false);
            best2graph.setVisible(true);
            best2graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            b2g = new StatisticsGraph(best2graph,graphwidth,graphheight);
            best2graph.add(b2g);
            b2g.setVisible(true);
            b2g.addPoint(0,0);*/
            
            JFrame bestograph = new JFrame("Best score overall");
            bestograph.setSize(graphwidth,graphheight);
            bestograph.setResizable(false);
            bestograph.setVisible(true);
            bestograph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            bog = new StatisticsGraph(bestograph,graphwidth,graphheight);
            bestograph.add(bog);
            bog.setVisible(true);
            bog.addPoint(0,0);
            
            StatFrameThread sft = new StatFrameThread(arg);
            (new Thread(sft)).start();
        }
        
        FileWriter httpout = null;
        PrintWriter httpprint = null;
        try {
            httpout = new FileWriter("index.html");
            httpprint = new PrintWriter(httpout,true);
        } catch (Exception e) { 
            e.printStackTrace();
        }
        
        String predictTime = "";
        String predictEndTime = "";
        
        String calcStartTime = printDate();
        
        String calcstarttimeprint = "Starting calculation at " + calcStartTime;
        System.out.println(calcstarttimeprint);
        
        String netsettingsprint = "\nCurrent network settings:\n\nnumgens: " + numgens + "\nnetspergen: " + netspergen + "\nhidl_num: " + hidl_num + "\n\n";
        System.out.println(netsettingsprint);
        
        SimulateBitcoin p = new SimulateBitcoin();
        
        
        int[] layers = {24,48,48,1};
        Generation g = new Generation(netspergen,layers,.01,.1,"sigmoid","sigmoid");
        int bestscore = Integer.MIN_VALUE;
        Network bestnet = null;
        int bestscoregen = 0;
        int genamt=numgens;
        long startTime=java.lang.System.currentTimeMillis();
        
        String percentDraw="....................................................................................................";
        String percentNum="0";
        
        for(int i=0;i<genamt;i++) {
            System.out.println("GEN: " + i + "/" + genamt);
            Network[] nets = g.getNets();
            
            
                
                
            try {
                httpout = new FileWriter("index.html");
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpprint = new PrintWriter(httpout,true);
            httpprint.println("<meta http-equiv=\"refresh\" content=\"10\">");
            httpprint.println("<html>");
            httpprint.println("<h3>START TIME: " + calcStartTime + "</h2>");
            httpprint.println("<h3>PREDICTED COMPUTE TIME: " + predictTime + "</h2>");
            httpprint.println("<p> </p>\n<p> </p>\n<p> </p>\n<p> </p>\n<p> </p>\n");
            httpprint.println("<h2>PROGRESS</h2>");
            httpprint.println("<p>[" + percentDraw + "] (" + percentNum + "%)</p>");
            httpprint.println("<p>Current Generation: " + i + "/" + genamt + "</p>");
            httpprint.println("<p> </p>\n<p> </p>\n<p> </p>\n<p> </p>\n<p> </p>\n");
            httpprint.println("<h2>CURRENT BEST</h2>");
            httpprint.println("<p>Best score: " + bestscore + "</p>");
            httpprint.println("<p>Generation of top scorer: " + bestscoregen + "</p>");
            httpprint.println("<p>Prediction of best under testarr:</p>");
            if (bestnet!=null) {
                double[] prediction = runTestArr(bestnet, layers[0]);
                for(int ih=0;ih<prediction.length;ih++) {
                    //httpprint.println("<p>Up: "+ prediction[0] + "%</p");
                    //httpprint.println("<p>Down: "+ prediction[1] + "%</p");
                    httpprint.println("<p>" + ih + ": " + prediction[ih] + "</p>");
                }
            }
            httpprint.println("</html>");
            httpprint.flush();
            for(Network n : nets) {
                int score = p.simulateImproved(n,false);
                if (score>bestscore) {
                    bestscore=score;
                    bestscoregen=i;
                    System.out.println("new best score of " + bestscore);
                    //httpprint.println("<p>new best score of " + bestscore + "</p>");
                    //httpprint.flush();
                    if (bestnet!=null) {
                        System.out.println("comparing new bestnet with previous");
                        Network.compareNets(bestnet, n);
                    }
                    bestnet = n;
                }
            }
            if (i%1==0) {
                if (dispgraphs==1) {
                    arg.addPoint(i,g.getGenAverageScore1());
                    //a2r.addPoint(i,g.getGenAverageScore2());
                    brg.addPoint(i,g.getBestScore());
                    //b2g.addPoint(i,g.getBestScore2());
                    bog.addPoint(i,bestscore);
                }
            }
            g.sortGen();
            Network genBest = g.getNets()[0];
            if (genamt>=100) {
                if (i%(genamt/100)==0) {
                    System.out.println(roundPercent(((double)i/(double)genamt)*100) + "% complete with network");
                    int perc = (int)(roundPercent(((double)i/(double)genamt)*100));
                    char[] pd = percentDraw.toCharArray();
                    pd[perc]='I';
                    percentDraw=String.valueOf(pd);
                    percentNum=""+perc;
                }
            }
            if (i==9) {
                long seconds = (long)(((((double)(java.lang.System.currentTimeMillis()-startTime))/10)/1000)*(double)genamt);
                int day = (int)TimeUnit.SECONDS.toDays(seconds);        
                long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
                long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
                long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
                predictTime = day + " days " + hours + " hours " + minute + " minutes " + second + " seconds";
                predictEndTime = "";
                System.out.println("Predicted computation time: " + day + " days " + hours + " hours " + minute + " minutes " + second + " seconds");
            }
            if (i!=genamt-1) {
                g.newNextGen();
            }
        }
        g.sortGen();
        System.out.println("simulating final network - with a score of " + bestnet.getScore());
        
    }
    private static String printDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }
    private static double roundPercent(double p) {
        p+=.00005;
        return (((double)((int)(p*100)))/100);
    }
    private static double[] runTestArr(Network n,int layer_0) {
        double[] testarr2 = new double[]{2482.09,2489.09,2483.72,2481.72,2499.98,2497.61,2498.92,2490.83,2491.4,2500,2500.97,2516.66,2517.01,2525,2504.09,2492.61,2490.08,2496.37,2476.41,2463.86,2442.99,2435.62,2440.87,2449.6,2436.07,2457.54,2461.99,2431.95,2421.55,2439.97,2412,2404.99,2407.81,2409.9,2391.87,2423.63,2424.99,2416.62,2408.25,2435.99,2425.94,2445.99,2460.01,2467.83,2459.35,2454.43,2488.43,2509.17};
        double[] testarr = new double[layer_0];
        for(int i=0;i<layer_0;i++) {
            testarr[i]=testarr2[i];
        }
        double[] netout = n.forward(testarr);
        double[] ret = new double[netout.length];
        for(int i=0;i<netout.length;i++) {
            ret[i]=netout[i];
        }
        return ret;
    }
}