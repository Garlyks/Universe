package game.sprites;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public final class Marca
extends Sprite {
    final int INTERVAL = 250;
    long intervalUntil;
    final int ACTIVE_TIME = 5000;
    long activeUntil = 0;
    boolean change = false;
    public Marca() {
        activeUntil = System.currentTimeMillis()+ACTIVE_TIME;
        
        intervalUntil =System.currentTimeMillis()+INTERVAL;
        this.setX(0d);
        this.setY(0d);
        this.setSprite("/Imagenes/Marca/marca1b.png");
        this.setVisible(false);
    }

    public Marca(Double X, Double Y) {
        
        activeUntil = System.currentTimeMillis()+ACTIVE_TIME;
        
        intervalUntil =System.currentTimeMillis()+INTERVAL;
        this.setX(X);
        this.setY(Y);
        this.setSprite("/Imagenes/Marca/marca1b.png");
    }

    @Override
    public Marca move() {
        
        //System.out.println(intervalUntil-System.currentTimeMillis());
        //++this.iterator;
        if (intervalUntil<System.currentTimeMillis()) {
            //System.out.println(System.currentTimeMillis());
            if (change){
                this.setSprite("/Imagenes/Marca/marca2b.png");
                change = false;
            }else{
                this.setSprite("/Imagenes/Marca/marca1b.png");
                change = true;
            }
            intervalUntil =System.currentTimeMillis()+INTERVAL;
        }
        
        return this;
    }

    @Override
    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        move();
        Double x1 = coordenadaHorizontal;
        Double y1 = coordenadaVertical;
        this.setX(x1);
        this.setY(y1);
        if (this.isVisible()) {
            grafico.drawImage(new ImageIcon(this.getClass().getResource(this.getSprite())).getImage(), x1.intValue(), y1.intValue(), null);
        }
    }
    @Override
    public Marca putSprite(Graphics grafico) {
        move();
        super.putSprite(grafico);
        return this;
    }
    public void moveTo(Double x, Double y) {
        move();
        this.setX(x - (double)(this.getWidth() / 2));
        this.setY(y - (double)(this.getHeight() / 2));
        //this.iterator = 0;
        activeUntil = System.currentTimeMillis()+ACTIVE_TIME;
        intervalUntil =System.currentTimeMillis()+INTERVAL;
        
    }
}

