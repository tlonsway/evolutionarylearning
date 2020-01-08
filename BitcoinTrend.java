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
        
        
        int[] layers = {48,96,96,96,1};
        Generation g = new Generation(netspergen,layers,.01,.1,"relu","relu");
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
            
            
            
        }
       
        
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
}