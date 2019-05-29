import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
public class GANWords {
    public static void main(String[] args) {
        
        //FORMAT OF WORDS: padded to 50 characters, value 0 is returned for unused characters
        //words are converted to ascii values,
        
        String worddir = "H:/dictionary/words.txt";
        
        GANDictionary dictionary = new GANDictionary(worddir);
        
        
        
        int generationnum = 20000;
        int actualwordsdiscriminator = 4;
        
        
        int[] generativelayers = {50,75,50,75,50};
        Generation generative = new Generation(100,generativelayers,.005,.4,"relu","relu");
        
        int[] discriminativelayers = {250,300,100,1};
        Generation discriminative = new Generation(100,discriminativelayers,.005,.4,"relu","sigmoid");
        
        Network bestgenerative = generative.getBestNet();
        Network bestdiscriminative = null;
        
        double[] randomnoise = new double[50];
        for(int rni=0;rni<randomnoise.length;rni++) {
            //randomnoise[rni]=(int)(Math.random()*255);
            randomnoise[rni]=(int)(Math.random()*75)+48;
        }
        
        
        for(int gnum=0;gnum<generationnum;gnum++) {
            //EVOLVE DISCRIMINATIVE
            
            //feed synthetic image to discriminative network
            
            // double[] randomnoise = new double[784];
            // for(int rni=0;rni<randomnoise.length;rni++) {
                // randomnoise[rni]=(int)(Math.random()*255);
            // }
            
            double[] generatedword = bestgenerative.forward(randomnoise);
            
            Network[] discriminativenetworks = discriminative.getNets();
            
            double[] discriminativeinput = new double[250];
            int tin=0;
            for(double d : generatedword) {
                if (d<=122) {
                    if (d>=48) {
                        discriminativeinput[tin]=d;
                    } else {
                        discriminativeinput[tin]=48;
                    }
                } else {
                    discriminativeinput[tin]=122;
                }
                tin++;
            }
            for(int i=0;i<actualwordsdiscriminator;i++) {
                for(double d : dictionary.getRandomWord()) {
                    discriminativeinput[tin]=d;
                    tin++;
                }
            }
            
            
            int correctfits = 0;
            
            for(Network n : discriminativenetworks) {
                int actual = 0; //image is synthetic
                double observed = (n.forward(discriminativeinput))[0];
                double score = 1-((observed-actual)*(observed-actual));
                if (observed<.5) {
                    correctfits++;
                }
                n.setScore(score);
            }
            System.out.println("Correct Synthetic Predictions: " + correctfits);
            
            discriminative.sortGen();
            discriminative.newNextGen();
            discriminativenetworks = discriminative.getNets();
            
            //use actual input for discriminatives
            
            discriminativeinput = new double[250];
            tin=0;
            for(double d : generatedword) {
                if (d<=122) {
                    if (d>=48) {
                        discriminativeinput[tin]=d;
                    } else {
                        discriminativeinput[tin]=48;
                    }
                } else {
                    discriminativeinput[tin]=122;
                }
                tin++;
            }
            for(int i=0;i<actualwordsdiscriminator;i++) {
                for(double d : dictionary.getRandomWord()) {
                    discriminativeinput[tin]=d;
                    tin++;
                }
            }
            correctfits=0;
            for(Network n : discriminativenetworks) {
                int actual = 1; //image is real
                double observed = (n.forward(discriminativeinput))[0];
                double score = 1-((observed-actual)*(observed-actual));
                if (observed>.5) {
                    correctfits++;
                }
                n.setScore(score);
            }
            System.out.println("Correct Real Predictions: " + correctfits);
            bestdiscriminative=discriminative.getBestNet();
            
            discriminative.sortGen();
            discriminative.newNextGen();
            discriminativenetworks = discriminative.getNets();
            
           
            

            //EVOLVE GENERATIVE
            
            //GENERATIVE FITNESS = highest output of bestdiscriminative
            
            Network[] generativenetworks = generative.getNets();
            
            
            // randomnoise = new double[784];
            // for(int rni=0;rni<randomnoise.length;rni++) {
                // randomnoise[rni]=(int)(Math.random()*255);
            // }
            
            
            for(Network n : generativenetworks) {
                generatedword = n.forward(randomnoise);
                discriminativeinput = new double[250];
                tin=0;
                for(double d : generatedword) {
                    if (d<=122) {
                        if (d>=48) {
                            discriminativeinput[tin]=d;
                        } else {
                            discriminativeinput[tin]=48;
                        }
                    } else {
                        discriminativeinput[tin]=122;
                    }
                    tin++;
                }    
                for(int i=0;i<actualwordsdiscriminator;i++) {
                    for(double d : dictionary.getRandomWord()) {
                        discriminativeinput[tin]=d;
                        tin++;
                    }
                }
                
                
                double discriminativeoutput = (bestdiscriminative.forward(discriminativeinput))[0];
                
                n.setScore(discriminativeoutput);
            }
            
            bestgenerative = generative.getBestNet();
            
            generative.sortGen();
            generative.newNextGen();
            
            //OUTPUT BEST GENERATIVE
            
            // randomnoise = new double[784];
            // for(int rni=0;rni<randomnoise.length;rni++) {
                // randomnoise[rni]=(int)(Math.random()*255);
            // }
            
            generatedword = bestgenerative.forward(randomnoise);
            for(double d : generatedword) {
                if (d>122) {
                    d=122;
                }
                if (d<48) {
                    d=48;
                }
                System.out.print((char)d);
            }
            System.out.println();
            //System.out.println(generatedword);
            
        }
        //while(true) {
            // double[] randomnoise = new double[784];
            // for(int rni=0;rni<randomnoise.length;rni++) {
                // randomnoise[rni]=(int)(Math.random()*255);
            // }
            
        double[] generatedword = bestgenerative.forward(randomnoise);
        for(double d : generatedword) {
            if (d>122) {
                    d=122;
            }
            if (d<48) {
                d=48;
            }
            System.out.print((char)d);
        }
        System.out.println();
        //IMAGEDISPLAY.setImage(generatedimage);
        //}
    }
}