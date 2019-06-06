import javax.swing.*;
import java.awt.*;
import java.util.*;
public class Tetris extends JComponent{
    int[][] board;
    int[][][] blocks = new int[][][]{ {{1},{1},{1}} , {{1,1},{0,1},{0,1}} , {{1,1},{1,1}} , {{1,1,1},{0,1,0}} , {{1,1,0},{0,1,1}} , {{0,1,1},{1,1,0}} , {{1,1},{1,0},{1,0}}};
    int[][] fallingBlock;
    int[] fbPos;
    boolean movingRight;
    boolean movingLeft;
    double[] choices;
    double score;
    boolean isAlive;
    boolean isDrawing = false;
    Color fbColor;
    ArrayList<ArrayList<Color>> colorBoard;
    Network n;
    int currentHolesOnBoard;
    double[] previousdecision;
    /*
     * {1}, {1,1},                               {1,1}
     * {1}, {0,1}, {1,1} {1,1,1} {1,1,0} {0,1,1} {1,0}
       {1}  {0,1}  {1,1} {0,1,0} {0,1,1} {1,1,0} {1,0}
     * 
     */
    public Tetris(){
        reset(false);
    }
    public void reset(boolean id){
        board = new int[20][50];
        fallingBlock = getShape();
        fbPos = new int[]{9,0};
        movingRight = false;
        movingLeft = false;
        //0 == right, 1 == left, 2 == rotate right, 3 = rotate left
        choices = new double[4];
        score = 0;
        isAlive = true;
        isDrawing = id;
        if(id){
            System.out.println("Setting Color board");
            setColorBoard();
        }
        currentHolesOnBoard = 0;
    }
    public int[] simulate(Network net, boolean draw, boolean btime, boolean delay, int t, int gnum) {
        n=net;
        reset(draw);
        while(isAlive) {
            update();
            if (draw) {
                draw();
                try {
                    if (delay) {
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new int[]{(int)score};
    }
    public void draw(){
        super.repaint();
    }
    public void update(){
        double[] decision = n.forward(getInputsLarge());
        previousdecision=decision;
        int dec=0;
        double biggest=-1*Integer.MAX_VALUE;
        if (decision[0]>biggest) {
            biggest=decision[0];
            dec=1;
        }
        if (decision[1]>biggest) {
            biggest=decision[1];
            dec=2;
        }
        if (decision[2]>biggest) {
            biggest=decision[2];
            dec=3;
        }
        if (decision[3]>biggest) {
            biggest=decision[3];
            dec=4;
        }
        if (decision[4]>biggest) {
            biggest=decision[4];
            dec=0;
        }
        if(dec==1){
            fbPos[0]++;
            if(collided()){
                fbPos[0]--;
            }
        }   
        if(dec==2){
            fbPos[0]--;
            if(collided()){
                fbPos[0]++;
            }
        }
        if(dec==3){
            rotateRight();
            if(collided()){
                rotateLeft();
            }   
        }
        if(dec==4){
            rotateLeft();
            if(collided()){
                rotateRight();
            }
        }
        
        fbPos[1]++;
        if(collided()){
            fbPos[1]--;
            for(int x = 0; x < fallingBlock.length;x++){
                for(int y = 0; y < fallingBlock[x].length; y++){
                    if(fallingBlock[x][y] == 1){
                        board[x+fbPos[0]][y+fbPos[1]] = 1;
                    }
                }
            }
            //score += (((double)fbPos[1]/(double)board[0].length)*3)-1;
            if (fbPos[1]>25) {
                score += 2*(fbPos[1]);
                if (fbPos[1]>47) {
                    score+=500;
                }
            } else {
                score -= fbPos[1];
            }
            if(isDrawing){
                addShapeToColorBoard();
            }
            fbPos = new int[]{9,0};
            fallingBlock = getShape();
            if(collided()){
                isAlive = false;
            }
        }
        int previous = currentHolesOnBoard;
        checkForHole();
        int diffholecount = currentHolesOnBoard-previous;
        if (diffholecount==1) {
            score-=1;
        } else if (diffholecount>1) {
            score-=2;
        }
        
        for(int r=0;r<board[0].length;r++) {
            int cnt = this.filledInRow(r);
            if (r>10) {
                if (cnt>19) {
                    score+=16;
                } else
                if (cnt>18) {
                    score+=8;
                } else
                if (cnt>17) {
                    score+=4;
                } else
                if (cnt>16) {
                    score+=2;
                } else
                if (cnt>10) {
                    score+=.01;
                }
            } else if (r<10) {
                if (cnt>=1) {
                   // score-=1;
                }
            }
        }
        
        
        int row = checkRow();
        
        while(row != -1){
            score += 3000;
            dropAllBlocks(row);
            row = checkRow();
        }
    }
    public int filledInRow(int r) {
        int num=0;
        for(int c=0;c<board.length;c++) {
            if (board[c][r]==1) {
                num++;
            }
        }
        return num;
    }
    public double[] getInputs(){
        double[] ret = new double[31];
        ret[0] = fbPos[0];
        ret[1] = fbPos[1];
        int count = 2;
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[0].length; y++){
                ret[count] = fallingBlock[x][y];
                count++;
            }
            while(count%3 !=  0){
                count++;
            }
        }
        for(int x = 0; x  < board.length; x++){
            int highest = board[0].length;
            for(int y = board[0].length-1; y >= 0; y--){
                if(board[x][y] == 1 && y < highest){
                    highest = y;
                }
            }
            ret[11+x] = highest;
        }
        return ret;
    }
    public double[] getInputsColumnCount() {
        double[] ret = new double[51];
        ret[0] = fbPos[0];
        ret[1] = fbPos[1];
        int count = 2;
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[0].length; y++){
                ret[count] = fallingBlock[x][y];
                count++;
            }
            while(count%3 !=  0){
                count++;
            }
        }
        for(int x = 0; x  < board.length; x++){
            int highest = board[0].length;
            for(int y = board[0].length-1; y >= 0; y--){
                if(board[x][y] == 1 && y < highest){
                    highest = y;
                }
            }
            ret[11+x] = highest;
        }
        for(int x=0;x<board.length;x++) {
            int tempnum=0;
            for(int y=0;y<board[x].length;y++) {
                tempnum+=board[x][y];
            }
            ret[11+board.length+x] = tempnum;
        }
        return ret;
    }
    private void addShapeToColorBoard(){
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[x].length;y++){
                if(fallingBlock[x][y] == 1){
                    colorBoard.get(x+fbPos[0]).set(y+fbPos[1],fbColor);
                }
            }
        }
    }
    public double[] getInputsLarge() {
        double[] ret = new double[1012];
        ret[0] = fbPos[0];
        ret[1] = fbPos[1];
        int count = 2;
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[0].length; y++){
                ret[count] = fallingBlock[x][y];
                count++;
            }
            while(count%3 !=  0){
                count++;
            }
        }
        for(int x=0;x<board.length;x++) {
            for(int y=0;y<board[0].length;y++) {
                ret[count]=board[x][y];
                count++;
            }
        }
        return ret;
    }    
    public boolean checkForHole(){
        int count = 0;
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length;y++){
                if (board[x][y]==0) {
                    boolean contin = true;
                    int minX = x-1;
                    int minY = y-1;
                    int maxX = x+1;
                    int maxY = y+1;
                    if(minX < 0)minX = 0;
                    if(minY < 0)minY = 0;
                    if(maxX >= board.length)maxX = board.length-1;
                    if(maxY >= board[x].length)maxY = board[x].length-1;
                    for(int col = minX; col <= maxX; col++){
                        for(int row = minY; row <= maxY; row++){
                            if(board[col][row] == 0 && row!=y && col!=x){
                                contin = false;
                                break;
                            }
                        }
                        if(!contin){
                            break;
                        }
                    }
                    if(contin){
                        count++;
                    }
                }
            }
        }
        //if(isDrawing){
            //System.out.println(count);
        //}
        if(count > currentHolesOnBoard){
            currentHolesOnBoard = count;
            return true;
        }
        return false;
    }
    public void dropAllBlocks(int r){
        for(int x = 0; x < board.length; x++){
            board[x][r] = 0;
        }
        /*
        for(int[] x : board){
            for(int y: x){
                System.out.print(y+" ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------");
        */
        if(r > 0){
            for(int y = r-1; y >= 0; y--){
                for(int x = 0; x < board.length; x++){
                    if(isDrawing){
                        colorBoard.get(x).set(y+1,colorBoard.get(x).get(y));
                    }
                    board[x][y+1] = board[x][y];
                }
            }
        }
        /*
        for(int[] x : board){
            for(int y: x){
                System.out.print(y+" ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------");
        */
    }
    public void rotateRight(){
        int[][] newBlock = new int[fallingBlock[0].length][fallingBlock.length];
        for(int x = 0; x < fallingBlock.length;x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                newBlock[y][x] = fallingBlock[x][y];
            }
        }
        int[][] temp = new int[newBlock.length][newBlock[0].length];
        for(int x = 0; x < newBlock.length/2; x++){
            temp[x] = newBlock[newBlock.length-1-x];
            temp[newBlock.length-1-x] = newBlock[x];
        }
        if(newBlock.length%2 == 1){
            temp[(int)(newBlock.length/2)] = newBlock[(int)(newBlock.length/2)];
        }
        fallingBlock = temp;
    }
    private void setColorBoard(){
        colorBoard = new ArrayList<ArrayList<Color>>();
        for(int x  = 0; x < board.length; x++){
            colorBoard.add(new ArrayList<Color>());
            for(int y = 0; y < board[0].length;y++){
                colorBoard.get(x).add(null);
            }
        }
    }
    public void rotateLeft(){
        for(int i = 0; i < 3; i++){
            rotateRight();
        }
    }
    public boolean collided(){
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                if(y+fbPos[1] >= board[0].length || fbPos[0]+x < 0|| x+fbPos[0] >= 20 || (board[x+fbPos[0]][y+fbPos[1]] == 1 && fallingBlock[x][y] == 1)){
                    if(fbPos[0]+x < 0|| x+fbPos[0] >= 20){
                        //score -= 2;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public int checkRow(){
        for(int y = 0; y < board[0].length; y++){
            boolean found = true;
            for(int x = 0; x < board.length; x++){
                if(board[x][y] == 0){
                    if(isDrawing){
                        colorBoard.get(x).set(y,null);
                    }
                    found = false;
                }
            }
            if(found){
                //System.out.println("Row: "+y);
                return y;
            }
        }
        
        return -1;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,420,1000);
        g.setColor(Color.BLACK);
        g.fillRect(20,0,400,1000);
        g.setColor(Color.BLUE);
        
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                if(colorBoard != null && colorBoard.get(x).get(y) != null){
                    g.setColor(colorBoard.get(x).get(y));
                }
                if(board[x][y] == 1){
                    g.fillRect((x+1)*20,y*20,20,20);
                }
            }
        }
        if(fbColor != null){
            g.setColor(fbColor);
        }
        else{
            g.setColor(Color.RED);
        }
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                if(fallingBlock[x][y] == 1){
                    g.fillRect((fbPos[0]+1+x)*20,(fbPos[1]+y)*20,20,20);
                }
                //  System.out.print(fallingBlock[x][y]+" ");
            }
            //System.out.println();
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: "+score,25,15);
        g.drawString("isAlive: "+isAlive,25,30);
        //System.out.println("("+fbPos[0]+","+fbPos[1]+")");
        //System.out.println("----------------------------");
    }
    public int[][] getShape(){
        if(isDrawing){
            fbColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
        }
        return blocks[(int)(Math.random()*blocks.length)];
    }
}