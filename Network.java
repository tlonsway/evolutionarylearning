import java.util.*;
public class Network implements Comparable {
    private ArrayList<double[]> weights;
    private int[] lys;
    private double score;
    private double score2;
    //layers looks like [num of ins, num in each hidden layer, num in output layer]
    public Network(int[] layers) {
        lys=layers;
        //weights = new double[][];
        weights = new ArrayList<double[]>();
        for(int i=0;i<layers.length-1;i++) {
            weights.add(new double[layers[i]*layers[i+1]]);
            //System.out.println("adding " + (layers[i]*layers[i+1]) + " weights to weight layer " + i);
            for(int i2=0;i2<weights.get(i).length;i2++) {
                weights.get(i)[i2] = (Math.random()*2)-1;
                //System.out.println("set weight to " + weights.get(i)[i2]);
            }
        }
    }
    public Network(int[] layers, ArrayList<double[]> ws) {
        lys=layers;
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
        //set inputs
        for(int i=0;i<inputs.length;i++) {
            neurons.get(0)[i]=inputs[i];
        }
        //calculate hidden nodes on
        int wrow=0;
        for(int i=1;i<neurons.size();i++) {
            int noffset=0;
            //System.out.println("row " + (i) + ":");
            for(int i2=0;i2<neurons.get(i).length;i2++) {
                double sum=0;
                int num=0;
                for(int n1=0;n1<neurons.get(i-1).length;n1++) {
                    //System.out.println("i: " + i + " n1: " + n1 + " wrow: " + wrow + " noffset: " + noffset + " num: " + num);
                    //System.out.println("multiplying neuron number " + n1 + " in row " + (i-1) + " by weight number " + (noffset+(neurons.get(i).length*num)) + " in weight row " + wrow);
                    sum+=neurons.get(i-1)[n1]*weights.get(wrow)[noffset+(neurons.get(i).length*num)];
                    num++;
                }
                if (i==neurons.size()-1) {
                    neurons.get(i)[i2]=sigmoid(sum);
                } else {
                    neurons.get(i)[i2]=relu(sum);
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
    private static double sigmoid(double in) {
        return (1)/(1+(Math.pow(Math.E,-1*in)));
    }
    private static double relu(double in) {
        return in;
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
    public void printNet() {
        for(double[] da : weights) {
            for(double d : da) {
                System.out.print(d + " ");
            }
            System.out.println();
        }
    }
}