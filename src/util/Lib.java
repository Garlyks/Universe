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
        Double grados;
        Double factor;
        Double relacion;
        int cuadrante;
        Double factorX = Xinicial - Xfinal;
        Double factorY = Yinicial - Yfinal;
        Double anguloRadianes = Math.atan(factorY / factorX);
        grados = Math.toDegrees(anguloRadianes);
        if (Math.abs(factorX / factorY) < 1d){
            factor = factorX / factorY;
            relacion = factorY / factorX;
            cuadrante = 1;
        } else if (Math.abs(factorY / factorX) < 1d) {
            factor = factorY / factorX;
            relacion = factorX / factorY;
            cuadrante = 2;
        }
        Boolean derecha = false;
        Boolean arriba = false;
        if (factorX > 0d) {
            derecha = true;
        }
        if (factorY < 0d) {
            arriba = true;
        }
        if (derecha && arriba) {
            grados = 360d + grados;
        }
        if (!derecha && arriba) {
            grados = 180d + grados;
        }
        if (!derecha && !arriba) {
            grados = 180d + grados;
        }
        if (grados >= 360d) {
            grados = 0d;
        }
        return grados;
    }

    public static Double getRandomHeight(Canvas canvas){
       
        return 0d + (Math.random() * canvas.getHeight());
    }
    
    public static Double getRandomWidth(Canvas canvas){
        return 0d + (Math.random() * canvas.getWidth());
    }
}

