public class Main {
    public static void main(String[] args) {
        Network n = new Network(new int[]{2,5,2},"sigmoid","sigmoid");
        
        
        double[] output = n.forward(new double[]{.5,.5});
        System.out.println("output 1: " + output[0]);
        System.out.println("output 2: " + output[1]);
    }
}