import javax.swing.*;
public class RunGame {
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
            numgens=750;
            netspergen=30;
            dispgenbest=0;
            hidl_num=10;
            dispgraphs=1;
            graphwidth=400;
            graphheight=420;
        }
        StatisticsGraph arg,a2r,brg,b2g,bog;
        arg=a2r=brg=b2g=bog=null;
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
            
            JFrame ave2graph = new JFrame("Average specific score over generations");
            ave2graph.setSize(graphwidth,graphheight);
            ave2graph.setResizable(false);
            ave2graph.setVisible(true);
            ave2graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            a2r = new StatisticsGraph(ave2graph,graphwidth,graphheight);
            ave2graph.add(a2r);
            a2r.setVisible(true);
            a2r.addPoint(0,0);
            
            JFrame bestrgraph = new JFrame("Best raw score over generations");
            bestrgraph.setSize(graphwidth,graphheight);
            bestrgraph.setResizable(false);
            bestrgraph.setVisible(true);
            bestrgraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            brg = new StatisticsGraph(bestrgraph,graphwidth,graphheight);
            bestrgraph.add(brg);
            brg.setVisible(true);
            brg.addPoint(0,0);
            
            JFrame best2graph = new JFrame("Best specific score over generations");
            best2graph.setSize(graphwidth,graphheight);
            best2graph.setResizable(false);
            best2graph.setVisible(true);
            best2graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            b2g = new StatisticsGraph(best2graph,graphwidth,graphheight);
            best2graph.add(b2g);
            b2g.setVisible(true);
            b2g.addPoint(0,0);
            
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
        //init pong graphics
        JFrame frame = new JFrame("pong window");
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        PongGame p = new PongGame();
        //Snake p = new Snake(20,20,600,620);
        frame.add(p);
        p.setVisible(true);
        int[] layers = {3,hidl_num,1};
        Generation g = new Generation(netspergen,layers);
        int bestscore=-1;
        Network bestnet = null;
        int genamt=numgens;
        for(int i=0;i<genamt;i++) {
            Network[] nets = g.getNets();
            for(Network n : nets) {
                /*if (i==1000) {
                    p.simulate(n,true,true,false);
                }*/
                int sum=0;
                int sum2=0;
                for(int i2=0;i2<2;i2++) {
                    int[] scoreb = p.simulate(n,false,true,false);
                    sum+=scoreb[0];
                    sum2+=scoreb[1];
                }
                int score = (int)(sum/2);
                int score2 = (int)(sum2/2);
                //sg.addPoint(i,score);
                //sg.redraw();
                if (score>bestscore) {
                    bestscore=score;
                    System.out.println("new best score of " + bestscore);
                    System.out.println("BEST NETWORK WEIGHTS:");
                    n.printNet(); 
                    bestnet=n;
                    //sg.addPoint(i,bestscore);
                }
                //System.out.println("network score of " + score);
                n.setScore(score);
                n.setScore2(score2);
            }
            g.sortGen();
            Network genBest = g.getNets()[0];
            if (dispgenbest==1) {
                p.simulate(genBest,true,true,true);
            }
            //sg.addPoint(i,genBest.getScore());
            if (i%10==0) {
                if (dispgraphs==1) {
                    arg.addPoint(i,g.getGenAverageScore1());
                    a2r.addPoint(i,g.getGenAverageScore2());
                    brg.addPoint(i,g.getBestScore());
                    b2g.addPoint(i,g.getBestScore2());
                    bog.addPoint(i,bestscore);
                }
            }
            g.setNets(nets);
            if (i!=genamt-1) {
                g.nextGen();
                //g.nextGenRand();
            }
            //g.nextGenRand();
        }
        g.sortGen();
        System.out.println("simulating final network - with a score of " + bestnet.getScore());
        bestnet.printNet();
        int sum=0;
        System.out.println("testing best network 1,000 times");
        for(int i=0;i<1000;i++) {
            sum+=p.simulate(g.getNets()[0],false,true,false)[0];
        }
        System.out.println("average bestnet score is " + (sum/1000));
        while(true) {
            int score=p.simulate(bestnet,true,false,true)[0];
            System.out.println("bestnet scored: " + score);
        }
        //p.draw();
        //FrameThread ft = new FrameThread(p);
        //UpdateThread ut = new UpdateThread(p);
        //(new Thread(ft)).start();
        //(new Thread(ut)).start();
        //for(int i=0;i<100;i++) {
        //    System.out.println("SCORE: " + p.simulate());
        //}
    }
    public static Network getPongNet() {
        int numgens,netspergen,dispgenbest,hidl_num;
        numgens=netspergen=dispgenbest=hidl_num=0;
        numgens=1000;
        netspergen=40;
        dispgenbest=0;
        hidl_num=10;
        PongGame p = new PongGame();
        p.setVisible(true);
        int[] layers = {3,hidl_num,1};
        Generation g = new Generation(netspergen,layers);
        int bestscore=-1;
        Network bestnet = null;
        int genamt=numgens;
        for(int i=0;i<genamt;i++) {
            Network[] nets = g.getNets();
            for(Network n : nets) {
                int sum=0;
                int sum2=0;
                for(int i2=0;i2<2;i2++) {
                    int[] scoreb = p.simulate(n,false,true,false);
                    sum+=scoreb[0];
                    sum2+=scoreb[1];
                }
                int score = (int)(sum/2);
                int score2 = (int)(sum2/2);
                if (score>bestscore) {
                    bestscore=score;
                    bestnet=n;
                }
                n.setScore(score);
                n.setScore2(score2);
            }
            g.sortGen();
            Network genBest = g.getNets()[0];
            g.setNets(nets);
            if (i!=genamt-1) {
                g.nextGen();
            }
            if (i%10==0) {
                System.out.println(((double)i/(double)genamt)*100 + "% complete with network");
            }
        }
        return bestnet;
    }
}