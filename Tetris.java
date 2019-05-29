import javax.swing.*;
import java.awt.*;
public class Tetris extends JComponent{
    int[][] board;
    int[][][] blocks = new int[][][]{ {{1},{1},{1}} , {{1,1},{0,1},{0,1}} , {{1,1},{1,1}} , {{1,1,1},{0,1,0}} , {{1,1,0},{0,1,1}} , {{0,1,1},{1,1,0}} , {{1,1},{1,0},{1,0}}};
    int[][] fallingBlock;
    int[] fbPos;
    boolean movingRight;
    boolean movingLeft;
    double[] choices;
    int score;
    boolean isAlive;
    /*
     * {1}, {1,1},                               {1,1}
     * {1}, {0,1}, {1,1} {1,1,1} {1,1,0} {0,1,1} {1,0}
       {1}  {0,1}  {1,1} {0,1,0} {0,1,1} {1,1,0} {1,0}
     * 
     */
    public Tetris(){
        reset();
    }
    public void reset(){
        board = new int[20][50];
        fallingBlock = getShape();
        fbPos = new int[]{9,0};
        movingRight = false;
        movingLeft = false;
        //0 == right, 1 == left, 2 == rotate right, 3 = rotate left
        choices = new double[4];
        score = 0;
        isAlive = true;
    }
    public void draw(){
        super.repaint();
    }
    public void update(){
        if(choices[0] > 0.5){
            fbPos[0]++;
            if(collided()){
                fbPos[0]--;
            }
        }   
        if(choices[1] > 0.5){
            fbPos[0]--;
            if(collided()){
                fbPos[0]++;
            }
        }
        if(choices[2] > 0.5){
            rotateRight();
            if(collided()){
                rotateLeft();
            }   
        }
        if(choices[3] > 0.5){
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
            fbPos = new int[]{9,0};
            fallingBlock = getShape();
            if(collided()){
                isAlive = false;
            }
        }
        int row = checkRow();
        while(row != -1){
            score += 10;
            dropAllBlocks(row);
            row = checkRow();
        }
    }
    public double[] getInputs(){
        double[] ret = new double[31];
        ret[0] = fbPos[0]/board.length;
        ret[1] = fbPos[1]/board[0].length;
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
            ret[11+x] = highest/board[0].length;
        }
        return ret;
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
    public void rotateLeft(){
        for(int i = 0; i < 3; i++){
            rotateRight();
        }
    }
    public boolean collided(){
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                if(y+fbPos[1] >= board[0].length || fbPos[0]+x < 0|| x+fbPos[0] >= 20 || (board[x+fbPos[0]][y+fbPos[1]] == 1 && fallingBlock[x][y] == 1)){
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
        g.fillRect(0,0,480,1000);
        g.setColor(Color.BLACK);
        g.fillRect(20,0,440,1000);
        g.setColor(Color.BLUE);
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                if(board[x][y] == 1){
                    g.fillRect((x+1)*20,y*20,20,20);
                }
            }
        }
        g.setColor(Color.RED);
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                if(fallingBlock[x][y] ==  1){
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
        return blocks[(int)(Math.random()*blocks.length)];
    }
}