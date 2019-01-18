import java.awt.*;
import javax.swing.*;
public class PongGame extends JComponent {
    double ballx;
    double bally;
    double paddlex;
    int ballxdir;
    int ballydir;
    int delay=2;
    boolean lose;
    int score;
    Network n;
    int lastdir;
    double time;
    int score2;
    public PongGame() {
        lose=false;
        score=0;
        score2=0;
        ballx=300;
        bally=200;
        paddlex=300;
        //ballxdir=(int)((Math.random()*11)+3)-(int)((Math.random()*22));
        //ballydir=(int)((Math.random()*11)+3)-(int)((Math.random()*22));
        ballxdir=(-10 + (int)(Math.random() * (20) + 1));
        ballydir=(-10 + (int)(Math.random() * (20) + 1));
        bally=(Math.random()*560);
        //System.out.println("ball x dir: " + ballxdir);
        //System.out.println("ball y dir: " + ballydir);
        lastdir=0;
        time=0;
    }
    public void update() {
        ballx+=ballxdir;
        bally+=ballydir;
        if (ballx<=0 || ballx>=560) {
            ballxdir=-ballxdir;
            ballydir=ballydir;
        }
        if (bally<=0 || bally>=540) {
            ballxdir=ballxdir;
            ballydir=-ballydir;
        }
        if (bally>=540 && !(ballx>=paddlex&&ballx<=paddlex+100)) {
            lose=true;
        } else if (bally>=540 && (ballx>=paddlex&&ballx<=paddlex+100)) {
            score+=10000;
            score2++;
        }
        if (Math.abs(ballx-paddlex)<20) {
            //score+=100;
        }
        double[] decision = n.forward(new double[]{ballx,bally,paddlex});
        /*if (decision[0]>.6 && lastdir!=-1) {
            score+=1;
        }
        if (decision[0]<.4 && lastdir!=1) {
            score+=1;
        }*/
        if (decision[0]>=.4 && decision[0]<.6 && lastdir==0) {
            score-=50;
        }
        lastdir=0;
        if (paddlex<=500) {
            if (decision[0]>.6) {
                paddlex+=8;
                lastdir=1;
            }
        }
        if (paddlex>=0) {
            if (decision[0]<.4) {
                paddlex-=8;
                lastdir=-1;
            }
        }
        if (paddlex<=200 && paddlex>=100) {
            //score+=1000;
        }
        if (paddlex<=50 || paddlex>=450) {
            score-=100;
        }
        if (time>850) { //time is 850 normally
            lose=true;
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
        if (lose) {
            g.drawString("YOU LOST",20,20);
        }
        g.drawString("SCORE: " + score,20,50);
        g.drawString("DIRECTION: " + lastdir,20,110);
        g.drawString("PONG HITS: " + score2,20,80);
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
        score=0;
        score2=0;
        ballx=300;
        bally=200;
        paddlex=300;
        ballxdir=(-10 + (int)(Math.random() * (20) + 1));
        ballydir=(-10 + (int)(Math.random() * (20) + 1));  
        while(Math.abs(ballxdir)<3 || Math.abs(ballydir)<3) {
            ballxdir=(-10 + (int)(Math.random() * (20) + 1));
            ballydir=(-10 + (int)(Math.random() * (20) + 1));  
        }
        bally=(Math.random()*560);
    }
}