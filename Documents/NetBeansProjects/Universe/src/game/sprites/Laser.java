
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
    int velocidad = 300;
    Double relacionDistanciaXY = 1.0;
    Double relacionDistanciaYX = 1.0;
    
    double impulsoX = 0;
    double impulsoY = 0;
    
    long lifeTime = 1500;// lifetime en ms
    
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
        configure(Xi, Yi,  XDestino,  YDestino ); 
    }
    
    private Laser configure(double Xi, double Yi, double Xd, double Yd ){       
        direccion = Lib.calcularRotacion(Xi, Yi,Xd,Yd);
        //System.out.println(direccion);
        Xd-=getWidth()/2;
        Yd-=getHeight()/2;

        impulsoX = Xd-Xi;
        impulsoY = Yd-Yi;
        
        relacionDistanciaXY = 100 * Math.abs(impulsoX) / (Math.abs(impulsoX) + Math.abs(impulsoY)) ;
        relacionDistanciaYX = 100 * Math.abs(impulsoY) / (Math.abs(impulsoX) + Math.abs(impulsoY)) ;
        if(impulsoX<0){
            relacionDistanciaXY*= -1;
        }
        if(impulsoY<0){
            relacionDistanciaYX*= -1;
        }
        return this;
    }    
 
    @Override
    public Laser move() {
        lifeTime -= System.currentTimeMillis()-getRefreshTime();
        if(lifeTime<=0){
            destroy();
            return this;
        }
        if(impulsoX!=0 || impulsoY!=0){
            double distanciaARecorrer = (System.currentTimeMillis()-getRefreshTime())*(velocidad/1000d);
            double distanciaX = distanciaARecorrer * relacionDistanciaXY/100;
            double distanciaY = distanciaARecorrer * relacionDistanciaYX/100;
            
            setX(getX()+distanciaX);
            setY(getY()+distanciaY);
            
            /*if(impulsoX>-0.5 && impulsoX<0.5){
                impulsoX = 0;
            }else{
                if(impulsoX>0) impulsoX-=Math.abs(distanciaX);
                else impulsoX+=Math.abs(distanciaX);
            }
            if(impulsoY>-0.5 && impulsoY<0.5){
                impulsoY = 0;
            }else{
                if(impulsoY>0) impulsoY-=Math.abs(distanciaY);
                else impulsoY+=Math.abs(distanciaY);
            }*/
        }else{
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
        image = ImageTransform.rotacionImagen(image, direccion);
        if (this.isVisible()) {
            grafico.drawImage(image, this.getX().intValue(), this.getY().intValue(), null);
        }
    }

    @Override
    public Laser putSprite(Graphics grafico) {
        move();
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(this.getClass().getResource(this.getSprite())).getImage());
        
        image = ImageTransform.rotacionImagen(image, direccion);
        if (this.isVisible()) {
            grafico.drawImage(image, this.getX().intValue(), this.getY().intValue(), null);
        }
        setRefreshTime(System.currentTimeMillis());
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

