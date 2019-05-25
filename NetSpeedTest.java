public class NetSpeedTest {
    public static void main(String[] args) {
        int[] layers = new int[]{10,10};
        Network n = new Network(layers,"sigmoid","sigmoid");
        long time1 = System.nanoTime();
        for(int i=0;i<100000;i++) {
            double[] ins = new double[layers[0]];
            for(int i2=0;i2<layers[0];i2++) {
                ins[i2]=Math.random();
            }
            n.forward(ins);
        }
        System.out.println("network took: " + (System.nanoTime()-time1));
    }
}