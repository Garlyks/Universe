package util;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Lib {
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage)img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), 2);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    public static Double calcularRotacion(Double Xinicial, Double Yinicial, Double Xfinal, Double Yfinal) {
        //System.out.println("Impulso X "+(Yfinal-Yinicial)+" Impulso Y "+(Xfinal-Xinicial));
        Double angulo = Math.atan2(Yfinal-Yinicial , Xfinal-Xinicial);
        return (angulo > 0 ? angulo : (2*Math.PI + angulo)) * 360 / (2*Math.PI);        
    }

    public static Double getRandomHeight(Canvas canvas){
       
        return 0d + (Math.random() * canvas.getHeight());
    }
    
    public static Double getRandomWidth(Canvas canvas){
        return 0d + (Math.random() * canvas.getWidth());
    }
    
    
}

