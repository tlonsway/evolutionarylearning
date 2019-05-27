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
            game.update();
            try{
                Thread.sleep(500);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }
}