import javax.swing.*;
import java.awt.*;
public class Tetris extends JComponent{
    int[][] board;
    int[][][] blocks = new int[][][]{ {{1},{1},{1}} , {{1,1},{0,1},{0,1}} , {{1,1},{1,1}} , {{1,1,1},{0,1,0}}};
    int[][] fallingBlock;
    int[] fbPos;
    /*
     * {1}, {1,1},      
     * {1}, {0,1}, {1,1} {1,1,1}
       {1}  {0,1}  {1,1} {0,1,0}
     * 
     */
    public Tetris(){
        reset();
    }
    public void reset(){
        board = new int[20][50];
        fallingBlock = getShape();
        fbPos = new int[]{9,0};
    }
    public void draw(){
        super.repaint();
    }
    public void update(){
        rotateRight();
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
        }
    }
    public void rotateRight(){
        int[][] newBlock = new int[fallingBlock[0].length][fallingBlock.length];
        for(int x = 0; x < fallingBlock.length;x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                newBlock[y][x] = fallingBlock[x][y];
            }
        }
        fallingBlock = newBlock;
    }
    public boolean collided(){
        for(int x = 0; x < fallingBlock.length; x++){
            for(int y = 0; y < fallingBlock[x].length; y++){
                if(y+fbPos[1] >= board[0].length || (board[x+fbPos[0]][y+fbPos[1]] == 1 && fallingBlock[x][y] == 1)){
                    return true;
                }
            }
        }
        return false;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,500,1000);
        g.setColor(Color.BLACK);
        g.fillRect(20,0,460,1000);
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
                System.out.print(fallingBlock[x][y]+" ");
            }
            System.out.println();
        }
        System.out.println("("+fbPos[0]+","+fbPos[1]+")");
        System.out.println("----------------------------");
    }
    public int[][] getShape(){
        return blocks[(int)(Math.random()*blocks.length)];
    }
}