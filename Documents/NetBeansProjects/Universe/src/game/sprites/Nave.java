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
    int velocidad = 5;
    
    double direccion = 0;
    double restanteX = 0;
    double restanteY = 0;
    double hp = 120;
    
    double relacionDistanciaXY = 1d;
    double relacionDistanciaYX = 1d;
    double damageAmplifier = 1;
    
    public Nave() {
        //super(this);
        setSprite("/Imagenes/Nave/nave.png");
        setX(200d);
        setY(200d);
    }


    @Override
    public Nave move() {
        Double Xfinal = getX() + restanteX;
        Double Yfinal = getY() + restanteY;
        if (Math.abs(restanteX) + Math.abs(restanteY) > 20) {
            direccion = Lib.calcularRotacion(getX(), getY(), Xfinal, Yfinal);
        }
       
        Double velocidadX = (double)velocidad * relacionDistanciaXY;
        Double velocidadY = (double)velocidad * relacionDistanciaYX;
        if (restanteX > 0) {
            setX(getX() - velocidadX);
            restanteX = restanteX - velocidadX;
            if (restanteX < velocidadX) {
                restanteX = 0;
            }
        } else if (restanteX < 0) {
            setX(getX() + velocidadX);
            restanteX = restanteX + velocidadX;
        }
        if (restanteY > 0) {
            setY(getY() - velocidadY);
            restanteY = restanteY - velocidadY;
            if (restanteY < velocidadY) {
                restanteY = 0;
            }
        } else if (restanteY < 0) {
            setY(getY() + velocidadY);
            restanteY = restanteY + velocidadY;
        }
        if (restanteX == 0 & restanteY == 0) {
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
        return this;
    }

    public Double getDireccion() {
        return direccion;
    }

    public Double getHp() {
        return hp;
    }

    public void setHp(Double hp) {
        hp = hp;
    }

    public void moveTo(Double x, Double y) {
        if (!Objects.equals(getX(), x) && !Objects.equals(getY(), y)) {
            restanteX = getX() - (x - (double)(getWidth() / 2));
            restanteY = getY() - (y - (double)(getHeight() / 2));
        }
        Double Xfinal = getX() + restanteX;
        Double Yfinal = getY() + restanteY;
        direccion = Lib.calcularRotacion(getX(), getY(), Xfinal, Yfinal);
        calcularRelacionXY();
    }

    public void calcularRelacionXY() {
        if (restanteX != 0 && restanteY != 0) {
            relacionDistanciaXY = 100 * Math.abs(restanteX) / (Math.abs(restanteX) + Math.abs(restanteY)) / 100;
            relacionDistanciaYX = 100 * Math.abs(restanteY) / (Math.abs(restanteX) + Math.abs(restanteY)) / 100;
        }
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
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());
        Double direccionT = (double)direccion;
        image = ImageTransform.rotacionImagen(image, direccionT);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
        return this;
    }

    public Double getRestanteX() {
        return restanteX;
    }

    public void setRestanteX(Double restanteX) {
        restanteX = restanteX;
    }

    public Double getRestanteY() {
        return restanteY;
    }

    public void setRestanteY(Double restanteY) {
        restanteY = restanteY;
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        iterator = iterator;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        velocidad = velocidad;
    }

    public Double getRelacionDistanciaXY() {
        return relacionDistanciaXY;
    }

    public void setRelacionDistanciaXY(Double relacionDistanciaXY) {
        relacionDistanciaXY = relacionDistanciaXY;
    }

    public Double getRelacionDistanciaYX() {
        return relacionDistanciaYX;
    }

    public void setRelacionDistanciaYX(Double relacionDistanciaYX) {
        relacionDistanciaYX = relacionDistanciaYX;
    }
    
    public double getDamageAmplifier() {
        return damageAmplifier;
    }

    public void setDamageAmplifier(double damageAmplifier) {
        damageAmplifier = damageAmplifier;
    }
}

