package game.sprites;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Sprite {
    public Double x;
    public Double y;
    private boolean visible = true;
    public String sprite;
    private boolean mustBeDestroy = false;

    public Sprite() {
        this.x = this.y = 0d;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Sprite setVisible(boolean estado) {
        this.visible = estado;
        return this;
    }

    public Double getX() {
        return this.x;
    }

    public Sprite setX(Double valor) {
        this.x = valor;
        return this;
    }

    public void destroy() {
        this.mustBeDestroy = true;
    }

    public boolean mustBeDestroy() {
        return this.mustBeDestroy;
    }

    public Double getY() {
        return this.y;
    }

    public Sprite setY(Double valor) {
        this.y = valor;
        return this;
    }

    public int getWidth() {
        return new ImageIcon(this.getClass().getResource(this.sprite)).getImage().getWidth(null);
    }

    public int getHeight() {
        return new ImageIcon(this.getClass().getResource(this.sprite)).getImage().getHeight(null);
    }
    public Sprite setSprite(String url) {
        this.sprite = url;
        return this;
    }

    public String getSprite() {
        return this.sprite;
    }

    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        this.x = coordenadaHorizontal;
        this.y = coordenadaVertical;
        if (this.visible) {
            grafico.drawImage(new ImageIcon(this.getClass().getResource(this.sprite)).getImage(), this.x.intValue(), this.y.intValue(), null);
        }
    }

    public Sprite putSprite(Graphics grafico) {
        if (this.visible) {
            grafico.drawImage(new ImageIcon(this.getClass().getResource(this.sprite)).getImage(), this.x.intValue(), this.y.intValue(), null);
        }
        return this;
    }

    public boolean isIn(Double x, Double y) {
        return x >= this.x && x <= this.x + (double)this.getWidth() && y >= this.y && y <= this.y + (double)this.getHeight();
    }

    public boolean intecerpta(Sprite sprite1) {
        boolean intercepta = false;
        boolean interceptaX = false;
        boolean interceptaY = false;
        if (sprite1.getX() <= this.getX() && sprite1.getX() + (double)sprite1.getWidth() >= this.getX() || this.getX() <= sprite1.getX() && this.getX() + (double)this.getWidth() >= sprite1.getX()) {
            interceptaX = true;
        }
        if (sprite1.getY() <= this.getY() && sprite1.getY() + (double)sprite1.getHeight() >= this.getY() || this.getY() <= sprite1.getY() && this.getY() + (double)this.getHeight() >= sprite1.getY()) {
            interceptaY = true;
        }
        if (interceptaY && interceptaX) {
            intercepta = true;
        }
        return intercepta;
    }

    public Sprite move() {
        return this;
    }
}

