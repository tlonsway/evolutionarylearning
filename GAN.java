import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
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
        
        JFrame frame = new JFrame("Generative Adversarial Display");
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GANDisplay IMAGEDISPLAY = new GANDisplay();
        
        frame.add(IMAGEDISPLAY);
        IMAGEDISPLAY.setVisible(true);
        
        String imagedir = "H:/mnist_png/mnist_png/testing/0/";
        
        GanImage IMAGES = new GanImage(imagedir);
        
        
        
        int generationnum = 100;
        int actualimagesdiscriminator = 1;
        
        
        int[] generativelayers = {784,150,150,784};
        Generation generative = new Generation(100,generativelayers,.005,.4,"relu","relu");
        
        int[] discriminativelayers = {1568,500,100,1};
        Generation discriminative = new Generation(100,discriminativelayers,.005,.4,"relu","sigmoid");
        
        Network bestgenerative = generative.getBestNet();
        Network bestdiscriminative = null;
        
        for(int gnum=0;gnum<generationnum;gnum++) {
            //EVOLVE DISCRIMINATIVE
            
            //feed synthetic image to discriminative network
            
            double[] randomnoise = new double[784];
            for(int rni=0;rni<randomnoise.length;rni++) {
                randomnoise[rni]=(int)(Math.random()*255);
            }
            
            double[] generatedimage = bestgenerative.forward(randomnoise);
            
            Network[] discriminativenetworks = discriminative.getNets();
            
            double[] discriminativeinput = new double[1568];
            BufferedImage[] discriminativeinputimages= new BufferedImage[actualimagesdiscriminator];
            for(int i=0;i<discriminativeinputimages.length;i++) {
                discriminativeinputimages[i]=IMAGES.getRandomImage();
            }
            
            int gii=0;
            
            for(double d: generatedimage) {
                discriminativeinput[gii]=(int)d;
                gii++;
            }
            for(BufferedImage b : discriminativeinputimages) {
                int[] values = new int[b.getHeight()*b.getWidth()];
                int vin=0;
                for(int r=0;r<b.getHeight();r++) {
                    for(int c=0;c<b.getWidth();c++) {
                        int rgb = b.getRGB(c,r);
                        int rgbr = (rgb >> 16) & 0xFF;
                        int rgbg = (rgb >> 8) & 0xFF;
                        int rbgb = (rgb & 0xFF);
                        values[vin]=(rgbr+rgbg+rbgb)/3;
                        vin++;
                    }
                }
                //System.out.println("Pixels length: " + values.length);
                for(int i : values) {
                    discriminativeinput[gii]=i;   
                    gii++;
                }
            }
            
            for(Network n : discriminativenetworks) {
                int actual = 0; //image is synthetic
                double observed = (n.forward(discriminativeinput))[0];
                double score = 1-((observed-actual)*(observed-actual));
                n.setScore(score);
            }
            
            discriminative.sortGen();
            discriminative.newNextGen();
            discriminativenetworks = discriminative.getNets();
            
            //use actual input for discriminatives
            
            discriminativeinput = new double[1568];
            discriminativeinputimages = new BufferedImage[actualimagesdiscriminator+1];
            for(int i=0;i<discriminativeinputimages.length;i++) {
                discriminativeinputimages[i]=IMAGES.getRandomImage();
            }
            gii=0;
            for(BufferedImage b : discriminativeinputimages) {
                int[] values = new int[b.getHeight()*b.getWidth()];
                int vin=0;
                for(int r=0;r<b.getHeight();r++) {
                    for(int c=0;c<b.getWidth();c++) {
                        int rgb = b.getRGB(c,r);
                        int rgbr = (rgb >> 16) & 0xFF;
                        int rgbg = (rgb >> 8) & 0xFF;
                        int rbgb = (rgb & 0xFF);
                        values[vin]=(rgbr+rgbg+rbgb)/3;
                        //b.getRGB(c,r); //order of rows vs. columns
                        vin++;
                    }
                }
                for(int i : values) {
                    discriminativeinput[gii]=i;   
                    gii++;
                }
            }
            
            for(Network n : discriminativenetworks) {
                int actual = 1; //image is real
                double observed = (n.forward(discriminativeinput))[0];
                double score = 1-((observed-actual)*(observed-actual));
                n.setScore(score);
            }
            
            bestdiscriminative=discriminative.getBestNet();
            
            discriminative.sortGen();
            discriminative.newNextGen();
            discriminativenetworks = discriminative.getNets();
            
           
            

            //EVOLVE GENERATIVE
            
            //GENERATIVE FITNESS = highest output of bestdiscriminative
            
            Network[] generativenetworks = generative.getNets();
            
            
            randomnoise = new double[784];
            for(int rni=0;rni<randomnoise.length;rni++) {
                randomnoise[rni]=(int)(Math.random()*255);
            }
            
            
            for(Network n : generativenetworks) {
                generatedimage = n.forward(randomnoise);
                
                discriminativeinput = new double[1568];
                discriminativeinputimages= new BufferedImage[actualimagesdiscriminator];
                for(int i=0;i<discriminativeinputimages.length;i++) {
                    discriminativeinputimages[i]=IMAGES.getRandomImage();
                }
                
                gii=0;
                
                for(double d: generatedimage) {
                    discriminativeinput[gii]=(int)d;
                    gii++;
                }
                for(BufferedImage b : discriminativeinputimages) {
                    int[] values = new int[b.getHeight()*b.getWidth()];
                    int vin=0;
                    for(int r=0;r<b.getHeight();r++) {
                        for(int c=0;c<b.getWidth();c++) {
                            int rgb = b.getRGB(c,r);
                            int rgbr = (rgb >> 16) & 0xFF;
                            int rgbg = (rgb >> 8) & 0xFF;
                            int rbgb = (rgb & 0xFF);
                            values[vin]=(rgbr+rgbg+rbgb)/3;
                            vin++;
                        }
                    }
                    //System.out.println("Pixels length: " + values.length);
                    for(int i : values) {
                        discriminativeinput[gii]=i;   
                        gii++;
                    }
                }
                
                
                double discriminativeoutput = (bestdiscriminative.forward(discriminativeinput))[0];
                
                n.setScore(discriminativeoutput);
            }
            
            bestgenerative = generative.getBestNet();
            
            generative.sortGen();
            generative.newNextGen();
            
            //OUTPUT BEST GENERATIVE
            
            randomnoise = new double[784];
            for(int rni=0;rni<randomnoise.length;rni++) {
                randomnoise[rni]=(int)(Math.random()*255);
            }
            
            generatedimage = bestgenerative.forward(randomnoise);
            IMAGEDISPLAY.setImage(generatedimage);
            
        }
    }
}