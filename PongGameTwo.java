import javax.swing.*;
import java.awt.*;
public class PongGameTwo extends JComponent {
    double ballx;
    double bally;
    double paddlex;
    double paddle2x;
    int ballxdir;
    int ballydir;
    int delay=2;
    boolean lose;
    int score;
    Network n;
    int lastdir;
    double time;
    int score2;
    Network net1;
    Network net2;
    boolean trained;
    int hits;
    public PongGameTwo() {
        trained=false;
        lose=false;
        score=0;
        score2=0;
        hits=0;
        ballx=300;
        bally=200;
        paddlex=300;
        paddle2x=300;
        ballxdir=(-10 + (int)(Math.random() * (20) + 1));
        ballydir=(-10 + (int)(Math.random() * (20) + 1));
        bally=(Math.random()*560);
        lastdir=0;
        time=0;
        System.out.println("training first network");
        net1=RunGame.getPongNet();
        System.out.println("training second network");
        net2=RunGame.getPongNet();
        System.out.println("both networks trained");
        trained=true;
    }
    public void update() {
        ballx+=ballxdir;
        bally+=ballydir;
        if (ballx<=0 || ballx>=560) {
            ballxdir=-ballxdir;
            ballydir=ballydir;
        }
        if (bally<=60 || bally>=540) {
            ballxdir=ballxdir;
            ballydir=-ballydir;
            hits++;
        }
        double[] decision1 = net1.forward(new double[]{ballx,bally,paddlex});
        double[] decision2 = net2.forward(new double[]{ballx,600-bally,paddle2x});
        if (bally>=540 && !(ballx>=paddlex&&ballx<=paddlex+100)) {
            lose=true;
        }
        if (bally<=40 && !(ballx>=paddle2x&&ballx<=paddle2x+100)) {
            lose=true;
        }
        if (paddlex<=500) {
            if (decision1[0]>.6) {
                paddlex+=8;
                lastdir=1;
            }
        }
        if (paddlex>=0) {
            if (decision1[0]<.4) {
                paddlex-=8;
                lastdir=-1;
            }
        }
        if (paddle2x<=500) {
            if (decision2[0]>.6) {
                paddle2x+=8;
                lastdir=1;
            }
        }
        if (paddle2x>=0) {
            if (decision2[0]<.4) {
                paddle2x-=8;
                lastdir=-1;
            }
        }
        if (lose) {
            reset();
        }
    }
    public void draw() {
        repaint();
    }
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,600);
        g.setColor(Color.WHITE);
        g.fillRect((int)ballx,(int)bally,20,20);
        g.fillRect((int)paddlex,540,100,20);
        g.fillRect((int)paddle2x,40,100,20);
        g.drawString("TOTAL HITS: " + hits, 20, 30);
    }
    public int[] simulate() {
        reset();
        while(!lose) {
            update();
        }
        return new int[]{score,score2};
    }
    public int[] simulate(Network net, boolean draw, boolean btime, boolean delay) {
        n=net;
        reset();
        while(!lose) {
            update();
            if (draw) {
                draw();
                try {
                    if (delay) {
                        Thread.sleep(5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (btime) {
                time+=1;
            }
        }
        return new int[]{score,score2};
    }
    public void reset() {
        lose=false;
        time=0;
        hits=0;
        score=0;
        score2=0;
        ballx=300;
        bally=200;
        paddlex=300;
        paddle2x=300;
        ballxdir=(-10 + (int)(Math.random() * (20) + 1));
        ballydir=(-10 + (int)(Math.random() * (20) + 1));  
        while(Math.abs(ballxdir)<3 || Math.abs(ballydir)<3) {
            ballxdir=(-10 + (int)(Math.random() * (20) + 1));
            ballydir=(-10 + (int)(Math.random() * (20) + 1));  
        }
        bally=(Math.random()*560);
    }
    public boolean isTrained() {
        return trained;
    }
}    