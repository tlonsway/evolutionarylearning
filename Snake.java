import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;
import javafx.geometry.*;
public class Snake extends JComponent {
    int width;
    int height;
    int screenwidth;
    int screenheight;
    int snakex;
    int snakey;
    ArrayList<int[]> previous = new ArrayList<int[]>();
    int dir = 0;
    int[] food = new int[2];
    int score=0;
    int score2=0;
    boolean lose;
    Network n;
    public Snake(int w, int h, int sw, int sh) {
        width=w;
        height=h;
        screenwidth=sw;
        screenheight=sh;
        snakex=width/2;
        snakey=height/2;
        score=0;
        food = new int[]{(int)(Math.random()*(width+1)),(int)(Math.random()*(height+1))};
    }
    public void update() {
        if (previous.size()>0) {
            for(int i=previous.size()-1;i>0;i--) {
                previous.set(i,previous.get(i-1));
            }
            previous.set(0,new int[]{snakex,snakey});
        }
        if (food[0]==snakex&&food[1]==snakey) {
            food = new int[]{(int)(Math.random()*(width+1)),(int)(Math.random()*(height+1))};
            previous.add(new int[]{snakex,snakey});
            score++;
        }
        if (dir==0) {
            snakey--;
        } else if (dir==1) {
            snakex--;
        } else if (dir==2) {
            snakex++;
        } else if (dir==3) {
            snakey++;
        }
        //draw();
        try {
            //Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (time>5000) {
            lose=true;
        }
        //update();
    }
    public void draw() {
        this.repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(snakex*(screenwidth/width), snakey*(screenheight/height), screenwidth/width, screenheight/height);
        for(int[] cords : previous) {
            g.fillRect(cords[0]*(screenwidth/width), cords[1]*(screenheight/height), screenwidth/width, screenheight/height);
        }
        g.setColor(Color.RED);
        g.fillRect(food[0]*(screenwidth/width), food[1]*(screenheight/height), screenwidth/width, screenheight/height);
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
        score=0;
        lose=false;
    }
    public void leftPress() {
        if (dir!=2) {
            dir=1;
        }
    }
    public void rightPress() {
        if (dir!=1) {
            dir=2;
        }
    }
    public void upPress() {
        if (dir!=3) {
            dir=0;
        }
    }
    public void downPress() {
        if (dir!=0) {
            dir=3;
        }
    }
}