
package game.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.swing.ImageIcon;
import util.ImageTransform;
import util.Lib;

public final class Laser
extends Sprite {
    Double alcance = 800.0;
    Double direccion = 0.0;
    Double restanteX = 0.0;
    Double restanteY = 0.0;
    double damage = 20.0;
    int velocidad = 20;
    Double relacionDistanciaXY = 1.0;
    Double relacionDistanciaYX = 1.0;
    
   
    
    public Laser(Nave naveOrigen, Nave naveDestino) {
        damage *= naveOrigen.getDamageAmplifier();
        this.setSprite("/Imagenes/Laser/laser.png");
        double Xi = naveOrigen.getX() + (naveOrigen.getWidth() / 2);        
        this.setX(Xi);
        double Yi = naveOrigen.getY() + (naveOrigen.getHeight() / 2);
        this.setY(Yi);        
        double Xd = naveDestino.getX() + (naveDestino.getWidth() / 2);
        double Yd = naveDestino.getY() + (naveDestino.getWidth() / 2);
        
        configure(Xi,  Yi,  Xd,  Yd );        
    }
    public Laser(Nave naveOrigen, double XDestino, double YDestino) {
        damage *= naveOrigen.getDamageAmplifier();
        this.setSprite("/Imagenes/Laser/laser.png");
        double Xi = naveOrigen.getX() + (naveOrigen.getWidth() / 2);        
        this.setX(Xi);
        double Yi = naveOrigen.getY() + (naveOrigen.getHeight() / 2);
        this.setY(Yi);   
        configure(Xi,  Yi,  XDestino,  YDestino ); 
    }
    
    public void configure(double Xi, double Yi, double Xd, double Yd ){
        this.restanteX = Math.abs(Xi - Xd);
        this.restanteY = Math.abs(Yi - Yd);
        this.relacionDistanciaXY = 100.0 * this.restanteX / (this.restanteX + this.restanteY) / 100.0;
        this.relacionDistanciaYX = 100.0 * this.restanteY / (this.restanteX + this.restanteY) / 100.0;
        this.direccion = Lib.calcularRotacion(Xd, Yd, this.getX(), this.getY());
        if (this.direccion >= 0.0 && this.direccion < 90.0) {
            this.relacionDistanciaXY = 1.0 - this.relacionDistanciaYX;
        }
        if (this.direccion >= 90.0 && this.direccion < 180.0) {
            this.relacionDistanciaXY = - this.relacionDistanciaXY;
        }
        if (this.direccion >= 180.0 && this.direccion < 270.0) {
            this.relacionDistanciaXY = - this.relacionDistanciaXY;
            this.relacionDistanciaYX = - this.relacionDistanciaYX;
        }
        if (this.direccion >= 270.0 && this.direccion < 360.0) {
            this.relacionDistanciaYX = - this.relacionDistanciaYX;
        }
    }
    
 
    public Laser move() {
        if (this.alcance > 0.0) {
            this.setX(this.getX() + this.relacionDistanciaXY * (double)this.velocidad);
            this.alcance = this.alcance - Math.abs(this.relacionDistanciaXY * (double)this.velocidad);
            this.setY(this.getY() + this.relacionDistanciaYX * (double)this.velocidad);
            this.alcance = this.alcance - Math.abs(this.relacionDistanciaYX * (double)this.velocidad);
        } else {
            this.destroy();
        }
        return this;
    }

    public Double getDamage() {
        return this.damage;
    }

    public Double hit() {
        this.destroy();
        return this.damage;
    }

    @Override
    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        this.setX(coordenadaHorizontal);
        this.setY(coordenadaVertical);
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(this.getClass().getResource(this.getSprite())).getImage());
        Double direccionT = (double)this.direccion;
        image = ImageTransform.rotacionImagen(image, direccionT);
        if (this.isVisible()) {
            grafico.drawImage(image, this.getX().intValue(), this.getY().intValue(), null);
        }
    }

    @Override
    public Laser putSprite(Graphics grafico) {
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(this.getClass().getResource(this.getSprite())).getImage());
        Double direccionT = (double)this.direccion;
        image = ImageTransform.rotacionImagen(image, direccionT);
        if (this.isVisible()) {
            grafico.drawImage(image, this.getX().intValue(), this.getY().intValue(), null);
        }
        return this;
    }

    public void moveTo(Double x, Double y) {
        if (!Objects.equals(this.getX(), x) && !Objects.equals(this.getY(), y)) {
            this.restanteX = this.getX() - (x - (double)(this.getWidth() / 2));
            this.restanteY = this.getY() - (y - (double)(this.getHeight() / 2));
        }
        Double Xfinal = this.getX() + this.restanteX;
        Double Yfinal = this.getY() + this.restanteY;
        this.direccion = Lib.calcularRotacion(this.getX(), this.getY(), Xfinal, Yfinal);
    }

  
}

