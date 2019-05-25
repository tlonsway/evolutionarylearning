public class GAN {
    public static void main(String[] args) {
        
        //reference to files storing source images
        //feed noise into generative, take output
        //feed generative input and 5 images from source into discriminative
        
        //feed current best generative into all of a generations discriminative, create next generation of discriminative
        //attempt each generative on the newest generation of discriminative, create next generation of generative
        
        //declare generative and discriminative networks
        //generative: 2500 inputs, 2500 outputs, 3 hidden layers(1000 nodes each)
        //discriminative: 15000 inputs, 1 output, 2 hidden layers(1000 nodes each)
        //500 generative and discriminative per respective generations
        
        int generationnum = 100;
        
        
        int[] generativelayers = {2500,1000,1000,1000,2500};
        Generation generative = new Generation(500,generativelayers,.005,.4,"relu","sigmoid");
        
        int[] discriminativelayers = {15000,1000,1000,1};
        Generation discriminative = new Generation(500,discriminativelayers,.005,.4,"relu","sigmoid");
        
        Network bestgenerative = generative.getBestNet();
        Network bestdiscriminative = null;
        
        for(int gnum=0;gnum<100;gnum++) {
            //evolve discriminative, then evolve generative
            
            
            
            
            
            
        }
    }
}