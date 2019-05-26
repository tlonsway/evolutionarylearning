import java.io.File;
import javax.imageio.*;
import java.awt.Image;
import java.util.*;
import java.awt.image.BufferedImage;
public class GanImage {
    ArrayList<BufferedImage> images;
    String directory;
    public GanImage(String imageDirectory) {
        //format of directory should be similar to "C:/imagefiles/"
        images = new ArrayList<BufferedImage>();
        directory=imageDirectory;
        File file = new File(directory);
        File[] files = file.listFiles();
        for(File f : files) {
            try {
                images.add(ImageIO.read(f));
            } catch (Exception e) {
                System.out.println("Failed to load a file");
                e.printStackTrace();
            }
        }
    }
    public BufferedImage getRandomImage() {
        return images.get((int)(Math.random()*images.size()));
    }
    public ArrayList<BufferedImage> getImages() {
        return images;
    }
    public BufferedImage getIndexedImage(int n) {
        if (n<images.size()) {
            return images.get(n);
        } else {
            System.out.println(n + " is not a valid image index");
            return null;
        }
    }
    public int getImageCount() {
        return images.size();
    }
}