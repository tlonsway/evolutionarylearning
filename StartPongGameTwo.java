import javax.swing.*;
public class StartPongGameTwo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("pong window");
        frame.setVisible(true);
        frame.setSize(600,620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PongGameTwo p = new PongGameTwo();
        while(!p.isTrained()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        frame.add(p);
        p.setVisible(true);
        p.draw();
        while(true) {
            p.update();
            p.draw();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}