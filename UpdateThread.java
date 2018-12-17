public class UpdateThread implements Runnable {
    PongGame pg;
    int ups = 60;
    public UpdateThread(PongGame pong) {
        pg=pong;
    }
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000/ups);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pg.update();
        }
    }
}