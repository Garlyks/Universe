package game.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.swing.ImageIcon;
import util.ImageTransform;
import util.Lib;

public final class Splitter
extends Sprite {
    Double direccion = 0d;
    Double restanteX = 0d;
    Double restanteY = 0d;
    Double health = 120d;
    int iterator = 0;
    int velocidad = 5;
    Double relacionDistanciaXY = 1d;
    Double relacionDistanciaYX = 1d;

    public Splitter() {
        this.setSprite("/Imagenes/Splitter/Splitter.png");
        this.setX(400d);
        this.setY(400d);
    }

    @Override
    public void move() {
        Double Xfinal = this.getX() + this.restanteX;
        Double Yfinal = this.getY() + this.restanteY;
        if (Math.abs(this.restanteX) + Math.abs(this.restanteY) > 20d) {
            this.direccion = Lib.calcularRotacion(this.getX(), this.getY(), Xfinal, Yfinal);
        }
       
        Double velocidadX = (double)this.velocidad * this.relacionDistanciaXY;
        Double velocidadY = (double)this.velocidad * this.relacionDistanciaYX;
        if (this.restanteX > 0d) {
            this.setX(this.getX() - velocidadX);
            this.restanteX = this.restanteX - velocidadX;
            if (this.restanteX < velocidadX) {
                this.restanteX = 0d;
            }
        } else if (this.restanteX < 0d) {
            this.setX(this.getX() + velocidadX);
            this.restanteX = this.restanteX + velocidadX;
        }
        if (this.restanteY > 0d) {
            this.setY(this.getY() - velocidadY);
            this.restanteY = this.restanteY - velocidadY;
            if (this.restanteY < velocidadY) {
                this.restanteY = 0d;
            }
        } else if (this.restanteY < 0d) {
            this.setY(this.getY() + velocidadY);
            this.restanteY = this.restanteY + velocidadY;
        }
        if (this.restanteX == 0d & this.restanteY == 0d) {
            ++this.iterator;
            if (this.iterator == 10) {
                this.setX(this.getX() + 1d);
            } else if (this.iterator == 20) {
                this.setY(this.getY() - 1d);
            } else if (this.iterator == 30) {
                this.setX(this.getX() - 1d);
            } else if (this.iterator > 40) {
                this.setY(this.getY() + 1d);
                this.iterator = 0;
            }
        }
    }

    public Double getDireccion() {
        return this.direccion;
    }

    public Double getHealth() {
        return this.health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public void moveTo(Double x, Double y) {
        if (!Objects.equals(this.getX(), x) && !Objects.equals(this.getY(), y)) {
            this.restanteX = this.getX() - (x - (double)(this.getWidth() / 2));
            this.restanteY = this.getY() - (y - (double)(this.getHeight() / 2));
        }
        Double Xfinal = this.getX() + this.restanteX;
        Double Yfinal = this.getY() + this.restanteY;
        this.direccion = Lib.calcularRotacion(this.getX(), this.getY(), Xfinal, Yfinal);
        this.calcularRelacionXY();
    }

    public void calcularRelacionXY() {
        if (this.restanteX != 0d && this.restanteY != 0d) {
            this.relacionDistanciaXY = 100d * Math.abs(this.restanteX) / (Math.abs(this.restanteX) + Math.abs(this.restanteY)) / 100d;
            this.relacionDistanciaYX = 100d * Math.abs(this.restanteY) / (Math.abs(this.restanteX) + Math.abs(this.restanteY)) / 100d;
        }
    }

    public void rotar(Double x, Double y) {
        this.direccion = Lib.calcularRotacion(x, y, this.getX(), this.getY());
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
    public void putSprite(Graphics grafico) {
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(this.getClass().getResource(this.getSprite())).getImage());
        Double direccionT = (double)this.direccion;
        image = ImageTransform.rotacionImagen(image, direccionT);
        if (this.isVisible()) {
            grafico.drawImage(image, this.getX().intValue(), this.getY().intValue(), null);
        }
    }
}

