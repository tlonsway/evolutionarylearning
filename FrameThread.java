public class FrameThread implements Runnable {
    PongGame pg;
    int fps = 60;
    public FrameThread(PongGame pong) {
        pg=pong;
    }
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000/fps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pg.draw();
        }
    }
}