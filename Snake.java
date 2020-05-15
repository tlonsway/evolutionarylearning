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
    double time;
    int numfood;
    int lastdec;
    double insidediag;
    double outsidediag;
    double[] outputs;
    double[] inputs;
    int timeout;
    int gennum;
    int timesinceeat;
    public Snake(int w, int h, int sw, int sh) {
        width=w;
        height=h;
        screenwidth=sw;
        screenheight=sh;
        //snakex=width/2;
        //snakey=height/2;
        //snakex=(int)(Math.random()*(width));
        //snakey=(int)(Math.random()*(height));
        snakex=(int)(width/2);
        snakey=(int)(height/2);
        score=0; 
        score2=0;
        numfood=1;
        time=0;
        lastdec=0;
        food = new int[]{(int)(Math.random()*(width-1))+1,(int)(Math.random()*(height-1))+1};
        insidediag = Math.sqrt((w-0)*(w-0)+(h-0)*(h-0));
        outsidediag = Math.sqrt((w+2)*(w+2)+(h+2)*(h+2));
        dir=1;
        outputs = new double[4];
        gennum=0;
        timesinceeat=0;
        System.out.println("TimeSinceEat limit set to " + (16*(width/10*height/10)));
    }
    public void update() {
        if (previous.size()>0) {
            for(int i=previous.size()-1;i>0;i--) {
                previous.set(i,previous.get(i-1));
            }
            previous.set(0,new int[]{snakex,snakey});
        }
        if (food[0]==snakex&&food[1]==snakey) {
            food = new int[]{(int)(Math.random()*(width-1))+1,(int)(Math.random()*(height-1))+1};
            previous.add(new int[]{snakex,snakey});
            score+=20;
            score2+=1;
            numfood++;
            timesinceeat=0;
        }
        inputs = getInputs();
        double[] decision = n.forward(inputs);
        outputs = decision;
        //System.out.println("decision: " + decision[0]);
        //System.out.println("snakex: " + snakex + "  snakey: " + snakey + "  food[0]: " + food[0] + "  food[1]: " + food[1]);
        //System.out.println("Network decision: " + decision[0]);
        int dirchoice=0; //0 is straight, 1 is left, 2 is right, decision[0] is straight, decision[1] is left, decision[2] is right
        
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
        
        if (dir==0&&lastdec==3 || dir==3&&lastdec==0 || dir==1&&lastdec==2 || dir==2&&lastdec==1) {
            dir=lastdec;
            //score=-1000000;
            //lose=true;
        } else if (dir!=lastdec) {
            //score+=1250;
        }
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
        //int backloss=3;
        //int closegain=1;
        int backloss = 0;
        int closegain = 0;
        
        //score+=1;
        //score+=1;
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
        if (decision[0]>=.33&&decision[0]<.66) {
            //score-=1000;
        }
        //draw();
        try {
            //Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (timesinceeat>timeout) {
            //score+=100000;
            lose=true;
        } else {
            //score+=50;
        }
        if (snakex>=width || snakex<0 || snakey<0 || snakey>=height) {
            //snake has crashed into a wall
            //score-=5000000; //forces snake to make 2,000 moves to not go negative
            lose=true;
        }
        for(int[] c : previous) {
            if (c[0]==snakex && c[1]==snakey) {
                lose=true;
                //score-=10000000;
                break;
            }
        }
        //score+=2500;
        
        //if (timesinceeat>(8*(width/10*height/10))) {
        
        /*if (timesinceeat>(16*((width/10)*(height/10)))) {
            //snake is likely in a loop
            lose=true;
            //score=-999999999;
        }*/ //UNCOMMENT FOR ORIGINAL FUNCTION
        
        timesinceeat++;
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
        String[] dirnames = new String[]{"right","down","up","left"};
        g.drawString("direction: " + dirnames[lastdec],0,30);
        g.drawString("score: " + score,0,60);
        g.drawString("eats: " + score2,0,90);
        g.drawString("snake x: " + snakex,0,120);
        g.drawString("snake y: " + snakey,0,150);
        g.drawString("out1: " + outputs[0],0,180);
        g.drawString("out2: " + outputs[1],0,210);
        g.drawString("out3: " + outputs[2],0,240);
        g.drawString("out4: " + outputs[3],0,270);
        g.drawString("Generation: " + gennum,0,screenheight-40);
        g.drawString("time since eat: " + timesinceeat,0,300);
        for(int i=0;i<inputs.length;i+=1) {
            g.drawString(i + " : " + inputs[i],screenwidth-200,30+i*20);
        }
    }
    public int[] simulate(Network net, boolean draw, boolean btime, boolean delay, int t, int gnum) {
        if (timeout!=-1) {
            timeout=t; //0 if turn timer not beign used, or -1 for automatic timer
        } else {
            timeout=4*(16*(width/10*height/10));
        }
        n=net;
        gennum=gnum;
        reset();
        while(!lose) {
            update();
            if (draw) {
                draw();
                try {
                    if (delay) {
                        Thread.sleep(15);
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
        //snakex=(int)(Math.random()*(width));
        //snakey=(int)(Math.random()*(height));
        snakex=(int)(width/2);
        snakey=(int)(height/2);
        previous = new ArrayList<int[]>();
        food = new int[]{(int)(Math.random()*(width-1))+1,(int)(Math.random()*(height-1))+1};
        timesinceeat=0;
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
        double[] vals = new double[24];
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
                            vals[count*3+1] = Math.sqrt(Math.abs((x-snakex)*(x-snakex)) + Math.abs((y-snakey)*(y-snakey))/maxOutsideDis);
                        }
                        else if(vals[count*3+2] == 0 && contains(x,y)){
                            //System.out.println("body found");
                            vals[count*3+2] = Math.sqrt(Math.abs((x-snakex)*(x-snakex)) + Math.abs((y-snakey)*(y-snakey))/maxOutsideDis);
                        }
                    }
                    count++;
                }
            }
        }
        //vals[vals.length-1]=Math.random()/10;
        return vals;
    }
    public boolean contains(int x, int y) {
        boolean in=false;
        for(int[] i : previous) {
            if (i[0]==x && i[1]==y) {
                in=true;
            }
        }
        return in;
    }
}