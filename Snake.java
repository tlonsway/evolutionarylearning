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
    int time;
    int numfood;
    int lastdec;
    double insidediag;
    double outsidediag;
    double[] outputs;
    double[] inputs;
    public Snake(int w, int h, int sw, int sh) {
        width=w;
        height=h;
        screenwidth=sw;
        screenheight=sh;
        //snakex=width/2;
        //snakey=height/2;
        snakex=(int)(Math.random()*(width));
        snakey=(int)(Math.random()*(height));
        score=0; 
        score2=0;
        numfood=1;
        time=0;
        lastdec=0;
        food = new int[]{(int)(Math.random()*(width)),(int)(Math.random()*(height))};
        insidediag = Math.sqrt((w-0)*(w-0)+(h-0)*(h-0));
        outsidediag = Math.sqrt((w+2)*(w+2)+(h+2)*(h+2));
        dir=1;
        outputs = new double[4];
    }
    public void update() {
        if (previous.size()>0) {
            for(int i=previous.size()-1;i>0;i--) {
                previous.set(i,previous.get(i-1));
            }
            previous.set(0,new int[]{snakex,snakey});
        }
        if (food[0]==snakex&&food[1]==snakey) {
            food = new int[]{(int)(Math.random()*(width)),(int)(Math.random()*(height))};
            previous.add(new int[]{snakex,snakey});
            score+=100000*numfood;
            score2+=1;
            numfood++;
        }
        inputs = getInputs();
        double[] decision = n.forward(inputs);
        outputs = decision;
        //System.out.println("decision: " + decision[0]);
        //System.out.println("snakex: " + snakex + "  snakey: " + snakey + "  food[0]: " + food[0] + "  food[1]: " + food[1]);
        //System.out.println("Network decision: " + decision[0]);
        int dirchoice=0; //0 is straight, 1 is left, 2 is right, decision[0] is straight, decision[1] is left, decision[2] is right

        if (decision[0]>decision[1]&&decision[0]>decision[2]) {
            dirchoice=0;
        } else if (decision[1]>decision[2]) {
            dirchoice=1;
        } else {
            dirchoice=2;
        } //dir0=right, dir1=down, dir2=up, dir3=left
        
        lastdec=dir;
        
        if (decision[0]>decision[1]&&decision[0]>decision[2]&&decision[0]>decision[3]) {
            dir=0;
        } else if (decision[1]>decision[2]&&decision[1]>decision[3]) {
            dir=1;
        } else if (decision[2]>decision[3]) {
            dir=2;
        } else {
            dir=3;
        }
        
        if (dir!=lastdec) {
            score+=2500;
        }
        
        if (dir==0&&lastdec==3 || dir==3&&lastdec==1 || dir==1&&lastdec==2 || dir==2&&lastdec==1) {
            score-=15000;
        }
        
        /*if (dirchoice==0) {
            //System.out.println("going straight");
            dir=dir; //continue straight
        }
        if (dirchoice==1) {
            if (dir==0) {
                dir=2;
            }
            if (dir==1) {
                dir=0;
            }
            if (dir==2) {
                dir=3;
            }
            if (dir==3) {
                dir=1;
            }
        } //turn left
        if (dirchoice==2) {
            if (dir==0) {
                dir=1;
            }
            if (dir==1) {
                dir=3;
            }
            if (dir==2) {
                dir=0;
            }
            if (dir==3) {
                dir=2;
            }
        } //turn right
        */
        
        
        
        int cxdir=0;
        int cydir=0;
        if (food[0]>snakex) {
            cxdir=1;
        } else if (food[0]==snakex) {
            cxdir=2;
        }
        if (food[1]>snakey) {
            cydir=1;
        } else if (food[1]==snakey) {
            cydir=2;
        }
        int backloss=5000;
        int closegain=2000;        
        /*if (dir==0) {
            snakex++;
        }
        if (dir==1) {
            snakey++;
        }
        if (dir==2) {
            snakey--;
        }
        if (dir==3) {
            snakex--;
        }*/
        if (dir==0) {
            snakex++;
            if (cxdir==1) {
                score+=closegain;
            }
            if (cxdir==0) {
                score-=backloss;
            }
        }
        if (dir==1) {
            snakey++;
            if (cydir==1) {
                score+=closegain;
            }
            if (cydir==0) {
                score-=backloss;
            }
        }
        if (dir==2) {
            snakey--;
            if (cydir==0) {
                score+=closegain;
            }
            if (cydir==1) {
                score-=backloss;
            }
        }
        if (dir==3) {
            snakex--;
            if (cxdir==0) {
                score+=closegain;
            }
            if (cxdir==1) {
                score-=backloss;
            }
        }
        /*if (decision[0]<.25) {
            dir=0;
            score+=1500;
            //System.out.println("x choice made");
        } else if (decision[0]<.5) {
            dir=1;
        } else if (decision[0]<.75) {
            dir=2;
        } else if (decision[0]<=1) {
            dir=3;
            score+=1500;
            //System.out.println("x choice made");
        }*/
        /*if (decision[2]>.5) {
            if (decision[0]>.5) {
                dir=0;
            } else {
                dir=3;
            }
        } else {
            if (decision[1]>.5) {
                dir=1;
            } else {
                dir=2;
            }
        }*/
        /*if (decision[1]<.33) {
            snakey--;
            if (lastdec==4) {
                score-=500;
            }
            lastdec=1;
            if (cydir==0) {
                score+=closegain;
            } else {
                score-=backloss;
            }
            if (cxdir==2) {
                score+=closegain;
            }
        } 
        if (decision[0]<.33) {
            snakex--;
            if (lastdec==3) {
                score-=500;
            }
            lastdec=2;
            if (cxdir==0) {
                score+=closegain;
            } else {
                score-=backloss;
            }
            if (cydir==2) {
                score+=closegain;
            }
        }
        if (decision[0]>=.66) {
            snakex++;
            if (lastdec==2) {
                score-=500;
            }
            lastdec=3;
            if (cxdir==1) {
                score+=closegain;
            } else {
                score-=backloss;
            }
            if (cydir==2) {
                score+=closegain;
            }
        }
        if (decision[1]>=.66) {
            snakey++;
            lastdec=4;
            if (lastdec==1) {
                score-=500;
            }
            if (cydir==1) {
                score+=closegain;
            } else {
                score-=backloss;
            }
            if (cxdir==2) {
                score+=closegain;
            }
        }*/
        
        score+=1000;
        
        if (decision[0]>=.33&&decision[0]<.66) {
            //score-=1000;
        }
        //draw();
        try {
            //Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (time>100) {
            //score+=100000;
            lose=true;
        } else {
            //score+=50;
        }
        if (snakex>width || snakex<0 || snakey<0 || snakey>height) {
            score-=10000;
            lose=true;
        }
        //update();
    }
    public void draw() {
        this.repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,screenwidth,screenheight);
        g.setColor(Color.GREEN);
        g.fillRect(snakex*(screenwidth/width), snakey*(screenheight/height), screenwidth/width, screenheight/height);
        for(int[] cords : previous) {
            g.fillRect(cords[0]*(screenwidth/width), cords[1]*(screenheight/height), screenwidth/width, screenheight/height);
        }
        g.setColor(Color.RED);
        g.fillRect(food[0]*(screenwidth/width), food[1]*(screenheight/height), screenwidth/width, screenheight/height);
        g.setColor(Color.WHITE);
        g.drawString("direction: " + lastdec,0,30);
        g.drawString("score: " + score,0,60);
        g.drawString("eats: " + score2,0,90);
        g.drawString("snake x: " + snakex,0,120);
        g.drawString("snake y: " + snakey,0,150);
        g.drawString("out1: " + outputs[0],0,180);
        g.drawString("out2: " + outputs[1],0,210);
        g.drawString("out3: " + outputs[2],0,240);
        g.drawString("out4: " + outputs[3],0,270);
        for(int i=0;i<inputs.length;i+=1) {
            g.drawString(i + " : " + inputs[i],400,30+i*20);
        }
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
                        Thread.sleep(25);
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
        score2=0;
        time=0;
        dir=0;
        numfood=1;
        lose=false;
        snakex=(int)(Math.random()*(width));
        snakey=(int)(Math.random()*(height));
        previous = new ArrayList<int[]>();
        food = new int[]{(int)(Math.random()*(width)),(int)(Math.random()*(height))};
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
     public double[] getInputs(){
        double[] vals = new double[26];
        int x = 0;
        int y  = 0;
        int count = 0;
        double maxInsideDis = insidediag;
        double maxOutsideDis = outsidediag;
        for(int ys = -1; ys < 2; ys ++){
            for(int xs = -1; xs < 2; xs++){
                if(!(xs == 0 && ys == 0)){
                    x = snakex;
                    y = snakey;
                    while(y >= 0 && y <= height && x >= 0 && x <= width){                        
                        x += xs;
                        y += ys;
                        if(vals[count*3] == 0 && (x == width || y  == height || x == 0 || y == 0)){
                            //System.out.println("wall found");
                            vals[count*3] = Math.sqrt(Math.abs((x-snakex)*(x-snakex)) + Math.abs((y-snakey)*(y-snakey)))/maxOutsideDis;
                        }
                        else if(vals[count*3+1] == 0 && x == food[0] && y == food[1]){
                            //System.out.println("food found");
                            vals[count*3+1] = Math.sqrt(Math.abs((x-snakex)*(x-snakex)) + Math.abs((y-snakey)*(y-snakey)))/maxInsideDis;
                        }
                        else if(vals[count*3+2] == 0 && contains(x,y)){
                            //System.out.println("body found");
                            vals[count*3+2] = Math.sqrt(Math.abs((x-snakex)*(x-snakex)) + Math.abs((y-snakey)*(y-snakey)))/maxInsideDis;
                        }
                    }
                    count++;
                }
            }
        }
        vals[24]=width;
        vals[25]=height;
        return vals;
    }
}