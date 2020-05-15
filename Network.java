import java.util.*;
import java.io.*;
public class Network implements Comparable {
    private ArrayList<double[]> weights;
    private int[] lys;
    private double score;
    private double score2;
    private String activationfunction;
    private String outputactivationfunction;
    //layers looks like [num of ins, num in each hidden layer, num in output layer]
    public Network(int[] layers, String af, String outaf) {
        lys=layers;
        activationfunction=af;
        outputactivationfunction=outaf;
        weights = new ArrayList<double[]>();
        for(int i=0;i<layers.length-1;i++) {
            weights.add(new double[layers[i]*layers[i+1]]);
            for(int i2=0;i2<weights.get(i).length;i2++) {
                weights.get(i)[i2] = (Math.random()*2)-1;
            }
        }
    }
    public Network(int[] layers, ArrayList<double[]> ws, String[] afs) {
        lys=layers;
        activationfunction=afs[0];
        outputactivationfunction=afs[1];
        weights = new ArrayList<double[]>();
        for (double[] d : ws) {
            weights.add(d);
        }
    }
    public double[] forward(double[] inputs) {
        if (inputs.length!=lys[0]) {
            System.out.println("INVALID INPUT NUMBER");
            return null;
        }
        ArrayList<double[]> neurons = new ArrayList<double[]>();
        for(int i=0;i<lys.length;i++) {
            neurons.add(new double[lys[i]]);
        }
        for(int i=0;i<inputs.length;i++) {
            neurons.get(0)[i]=inputs[i];
        }
        int wrow=0;
        for(int i=1;i<neurons.size();i++) {
            int noffset=0;
            for(int i2=0;i2<neurons.get(i).length;i2++) {
                double sum=0;
                int num=0;
                for(int n1=0;n1<neurons.get(i-1).length;n1++) {
                    sum+=neurons.get(i-1)[n1]*weights.get(wrow)[noffset+(neurons.get(i).length*num)];
                    num++;
                }
                if (i==neurons.size()-1) {
                    if (outputactivationfunction.equals("sigmoid")) {
                        neurons.get(i)[i2]=sigmoid(sum);
                    } else if (outputactivationfunction.equals("relu")) {
                        neurons.get(i)[i2]=relu(sum);
                    } else if (outputactivationfunction.equals("hyptan")) {
                        neurons.get(i)[i2]=hyptan(sum);
                    }
                } else {
                    if (activationfunction.equals("sigmoid")) {
                        neurons.get(i)[i2]=sigmoid(sum);
                    } else if (activationfunction.equals("relu")) {
                        neurons.get(i)[i2]=relu(sum);
                    } else if (activationfunction.equals("hyptan")) {
                        neurons.get(i)[i2]=hyptan(sum);
                    }
                }
                noffset++;
            }
            wrow++;
        }
        double[] output = new double[lys[lys.length-1]];
        for(int i=0;i<neurons.get(neurons.size()-1).length;i++) {
            output[i]=neurons.get(neurons.size()-1)[i];
        }
        return output;
    }
    private static double hyptan(double in) {
        double p = Math.pow(Math.E,2*in);
        return((p-1)/(p+1));
    }
    private static double sigmoid(double in) {
        return (1)/(1+(Math.pow(Math.E,-1*in)));
    }
    private static double relu(double in) {
        if (in>0) {
            return in;
        } else {
            return (0.01)*in;
        }
    }
    public void setScore(double num) {
        score=num;
    }
    public double getScore() {
        return score;
    }
    public void setScore2(double num) {
        score2=num;
    }
    public double getScore2() {
        return score2;
    }
    public int compareTo(Object other) {
        Network o = (Network)other;
        if (o.getScore()>score) {
            return 1;
        }
        if (score>o.getScore()) {
            return -1;
        }
        return 0;
    }
    public ArrayList<double[]> getWeights() {
        return weights;
    }
    public int[] getLayers() {
        return lys;
    }
    public String[] getActivationFunctions() {
        return new String[]{activationfunction,outputactivationfunction};
    }
    public void printNet() {
        for(double[] da : weights) {
            for(double d : da) {
                System.out.print(d + " ");
            }
            System.out.println();
        }
    }
    public static Network loadNet(String fileName) {
        try { 
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            String[] lysstr = line.split(" ");
            int[] layers = new int[lysstr.length];
            for(int i=0;i<lysstr.length;i++) {
                layers[i]=Integer.parseInt(lysstr[i]);
            }
            line = br.readLine();
            String[] afsstr = line.split(" ");
            line = br.readLine();
            ArrayList<double[]> weights = new ArrayList<double[]>();
            while(line != null) {
                String[] tsplit = line.split(" ");
                double[] templist = new double[tsplit.length];
                for(int i=0;i<tsplit.length;i++) {
                    templist[i] = (Double.parseDouble(tsplit[i]));
                }
                weights.add(templist);
                line = br.readLine();
            }
            return new Network(layers,weights,afsstr);
        } catch (Exception e) {
            System.out.println("An error occured while attempting to load a neural network");
            e.printStackTrace();
            return null;
        }
    }
    public static Network loadNet(String fileName, int[] lys, String[] afs) {
        try { 
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            ArrayList<double[]> weights = new ArrayList<double[]>();
            while(line != null) {
                String[] tsplit = line.split(" ");
                double[] templist = new double[tsplit.length];
                for(int i=0;i<tsplit.length;i++) {
                    templist[i] = (Double.parseDouble(tsplit[i]));
                }
                weights.add(templist);
                line = br.readLine();
            }
            return new Network(lys,weights,afs);
        } catch (Exception e) {
            System.out.println("An error occured while attempting to load a neural network");
            e.printStackTrace();
            return null;
        }
    }
    public void saveNet(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for(int i : lys) {
                bw.write(i + " ");
            }
            bw.write("\n");
            bw.write(activationfunction + " " + outputactivationfunction);
            bw.write("\n");
            for(double[] da : weights) {
                for(double d : da) {
                    bw.write(d + " ");
                }
                bw.write("\n");
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("An error occured while attempting to save a neural network");
            e.printStackTrace();
            return;
        }
    }
    public static void compareNets(Network n1, Network n2) {
        ArrayList<double[]> n1w = n1.getWeights();
        ArrayList<double[]> n2w = n2.getWeights();
        if (n1w.size()!=n2w.size()) {
            System.out.println("FATAL ERROR: compared networks do not have the same weight sizes");
            System.exit(1);
        }
        int[] layers = n1.getLayers();
        int samenum = 0;
        int diffnum = 0;
        for(int i=0;i<layers.length-1;i++) {
            for(int i2=0;i2<layers[i]*layers[i+1];i2++) {
                if (n1.getWeights().get(i)[i2]==n2.getWeights().get(i)[i2]) {
                    samenum++;
                } else {
                    diffnum++;
                }
            }
        }
        System.out.println("Number of shared weights: " + samenum);
        System.out.println("Number of different weights: " + diffnum);
    }
}