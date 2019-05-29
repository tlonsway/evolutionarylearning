import javax.swing.*;
import java.awt.*;
public class tetrisTester{
    public static void main(String[] args){
        JFrame frame = new JFrame("Window");
        Tetris game = new Tetris();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1550,1050);
        frame.setVisible(true);
        while(true){
            game.draw();
            game.choices = new double[]{Math.random(),Math.random(),Math.random(),Math.random()};
            game.update();
            double[] inputs = game.getInputs();
            /*
            for(double b: inputs){
                System.out.print(b);
            }
            System.out.println();
            
            if(game.count%10 == 0){
                for(int x = 0; x < game.board.length; x++){
                    game.board[x][47] = 1;
                    game.board[x][48] = 1;
                }
                game.count++;
            }
            */
            if(!game.isAlive){
                game.reset();
            }
            try{
                Thread.sleep(50);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }
}