import java.io.File;
import javax.imageio.*;
import java.awt.Image;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;
public class GANDictionary {
    ArrayList<String> words;
    String location;
    public GANDictionary(String dictionarydirectory) {
        location=dictionarydirectory;
        words = new ArrayList<String>();
        File file = new File(location);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while(line!=null) {
                words.add(line);
                line=br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double[] getRandomWord() {
        String word = words.get((int)(Math.random()*words.size()));
        while(word.length()<50) {
            word=word+"0";
        }
        double[] asciiword = new double[word.length()];
        int in=0;
        for(char c : word.toCharArray()) {
            asciiword[in]=c;
            in++;
        }
        return asciiword;
    }
    public ArrayList<String> getWords() {
        return words;
    }
    public int getWordCount() {
        return words.size();
    }
}