import java.awt.*;
import javax.swing.*;
public interface Game {
    public int[] simulate(Network net, boolean draw, boolean btime, boolean delay);
    public void reset();
}