import java.io.*;
import java.util.*;
public class FileDataReader {
    public static ArrayList<Double> btcHour() {
        String FILENAME = "coinbase_hourly.txt";
        ArrayList<Double> ret = new ArrayList<Double>();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            line = reader.readLine();
            while(line!=null) {
                ret.add(Double.parseDouble(line));
                line=reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}