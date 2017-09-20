package game.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.swing.ImageIcon;
import util.ImageTransform;
import util.Lib;

public class Nave
extends Sprite {
    int iterator = 0;
    int velocidad = 250;
    
    double direccion = 0;
    double impulsoX = 0;
    double impulsoY = 0;
    double hp = 120;
    
    double relacionDistanciaXY = 1d;
    double relacionDistanciaYX = 1d;
    double damageAmplifier = 1;   
    
    long timeAlive;
    
    public Nave() {
        //super(this);
        super.setSprite("/Imagenes/Nave/nave.png");
        super.setX(200d);
        super.setY(200d);
        
    }


    @Override
    public Nave move() {
        //System.out.println(impulsoX);
   
        if(impulsoX>0 || impulsoY>0){
            double distanciaARecorrer = (System.currentTimeMillis()-getRefreshTime())*(getVelocidad()/1000d);
            double distanciaX = distanciaARecorrer * relacionDistanciaXY/100;
            double distanciaY = distanciaARecorrer * relacionDistanciaYX/100;
            //System.out.println("Avance: "+distanciaX);
            setX(getX()+distanciaX);
            setY(getY()+distanciaY);
            
            if(impulsoX>0) impulsoX-=Math.abs(distanciaX);
            else impulsoX+=Math.abs(distanciaX);
            
            if(impulsoY>0) impulsoY-=Math.abs(distanciaY);
            else impulsoX+=Math.abs(distanciaY);
          
        }
        //Double velocidadX = (double)velocidad * relacionDistanciaXY;
        //Double velocidadY = (double)velocidad * relacionDistanciaYX;
        
        
        //long tiempoTranscurrido = System.currentTimeMillis()-this.getRefreshTime();
        
  
        
        
        /*
        Double Xfinal = getX() + impulsoX;
        Double Yfinal = getY() + impulsoY;
        if (Math.abs(impulsoX) + Math.abs(impulsoY) > 20) {
            direccion = Lib.calcularRotacion(getX(), getY(), Xfinal, Yfinal);
        }
       
        
        if (impulsoX > 0) {
            setX(getX() - velocidadX);
            impulsoX = impulsoX - velocidadX;
            if (impulsoX < velocidadX) {
                impulsoX = 0;
            }
        } else if (impulsoX < 0) {
            setX(getX() + velocidadX);
            impulsoX = impulsoX + velocidadX;
        }
        if (impulsoY > 0) {
            setY(getY() - velocidadY);
            impulsoY = impulsoY - velocidadY;
            if (impulsoY < velocidadY) {
                impulsoY = 0;
            }
        } else if (impulsoY < 0) {
            setY(getY() + velocidadY);
            impulsoY = impulsoY + velocidadY;
        }
        if (impulsoX == 0 & impulsoY == 0) {
            ++iterator;
            if (iterator == 10) {
                setX(getX() + 1d);
            } else if (iterator == 20) {
                setY(getY() - 1d);
            } else if (iterator == 30) {
                setX(getX() - 1d);
            } else if (iterator > 40) {
                setY(getY() + 1d);
                iterator = 0;
            }
        }
        */
        return this;
    }

    public Double getDireccion() {
        return direccion;
    }

    public Double getHp() {
        return hp;
    }

    public void setHp(Double hp) {
        this.hp = hp;
    }

    public void moveTo(Double x, Double y) {
        if (!Objects.equals(getX(), x) && !Objects.equals(getY(), y)) {
            impulsoX = (x - (getWidth() / 2)) -getX()  ;
            impulsoY = (y - (getHeight() / 2)) - getY()  ;
        }
        Double Xfinal = getX() + impulsoX;
        Double Yfinal = getY() + impulsoY;
        direccion = Lib.calcularRotacion(x,y,getX(), getY());
        calcularRelacionXY();
    }

    public void calcularRelacionXY() {
        //if (impulsoX != 0 && impulsoY != 0) {
            relacionDistanciaXY = 100 * Math.abs(impulsoX) / (Math.abs(impulsoX) + Math.abs(impulsoY)) ;
            relacionDistanciaYX = 100 * Math.abs(impulsoY) / (Math.abs(impulsoX) + Math.abs(impulsoY)) ;
            if(impulsoX<0){
                relacionDistanciaXY*= -1;
            }
            if(impulsoY<0){
                relacionDistanciaYX*= -1;
            }
            System.out.println("Relacion XY: "+relacionDistanciaXY+" Relacion YX: "+relacionDistanciaYX);
        //}
        
    }

    public void rotar(Double x, Double y) {
        direccion = Lib.calcularRotacion(x, y, getX(), getY());
    }

    @Override
    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        setX(coordenadaHorizontal);
        setY(coordenadaVertical);
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());
        image = ImageTransform.rotacionImagen(image, direccion);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
    }

    @Override
    public Nave putSprite(Graphics grafico) {
        move();
        //System.out.println(getRefreshTime());
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());       
        image = ImageTransform.rotacionImagen(image, direccion);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
        
        setRefreshTime(System.currentTimeMillis());
        //System.out.println(System.currentTimeMillis());
        
        return this;
    }

    public Double getRestanteX() {
        return impulsoX;
    }

    public void setRestanteX(Double impulsoX) {
        this.impulsoX = impulsoX;
    }

    public Double getRestanteY() {
        return impulsoY;
    }

    public void setRestanteY(Double impulsoY) {
        this.impulsoY = impulsoY;
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public Double getRelacionDistanciaXY() {
        return relacionDistanciaXY;
    }

    public void setRelacionDistanciaXY(Double relacionDistanciaXY) {
        this.relacionDistanciaXY = relacionDistanciaXY;
    }

    public Double getRelacionDistanciaYX() {
        return relacionDistanciaYX;
    }

    public void setRelacionDistanciaYX(Double relacionDistanciaYX) {
        this.relacionDistanciaYX = relacionDistanciaYX;
    }
    
    public double getDamageAmplifier() {
        return damageAmplifier;
    }

    public void setDamageAmplifier(double damageAmplifier) {
        this.damageAmplifier = damageAmplifier;
    }
}

