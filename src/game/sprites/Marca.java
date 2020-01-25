package game.sprites;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public final class Marca
extends Sprite {
    int iterator = 0;
    int tiempoActivo = 100;

    public Marca() {
        this.setX(0d);
        this.setY(0d);
        this.setSprite("/Imagenes/Marca/marca1b.png");
        this.setVisible(false);
    }

    public Marca(Double X, Double Y) {
        this.setX(X);
        this.setY(Y);
        this.setSprite("/Imagenes/Marca/marca1b.png");
    }

    @Override
    public void move() {
        ++this.iterator;
        if (this.iterator % 13 == 0) {
            this.setSprite("/Imagenes/Marca/marca2b.png");
        }
        if (this.iterator % 27 == 0) {
            this.setSprite("/Imagenes/Marca/marca1b.png");
        }
        if (this.iterator > this.tiempoActivo) {
            this.setVisible(false);
            this.iterator = 0;
        }
    }

    @Override
    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        Double x1 = coordenadaHorizontal;
        Double y1 = coordenadaVertical;
        this.setX(x1);
        this.setY(y1);
        if (this.isVisible()) {
            grafico.drawImage(new ImageIcon(this.getClass().getResource(this.getSprite())).getImage(), x1.intValue(), y1.intValue(), null);
        }
    }

    public void moveTo(Double x, Double y) {
        this.setX(x - (double)(this.getWidth() / 2));
        this.setY(y - (double)(this.getHeight() / 2));
        this.iterator = 0;
    }
}

