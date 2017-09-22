package game.sprites;

import java.awt.Graphics;
import java.util.Objects;
import javax.swing.ImageIcon;

public class Sprite {
    private Long refreshTime = System.currentTimeMillis();
    private Double x;
    private Double y;
    private Boolean visible = true;
    public String sprite;
    private boolean mustBeDestroy = false;

    public Sprite() {
        x = y = 0d;
    }

    public boolean isVisible() {
        return visible;
    }

    public Sprite setVisible(boolean estado) {
        visible = estado;
        return this;
    }

    public Double getX() {
        return x;
    }

    public Sprite setX(Double valor) {
        x = valor;
        return this;
    }

    public void destroy() {
        mustBeDestroy = true;
    }

    public boolean mustBeDestroy() {
        return mustBeDestroy;
    }

    public Double getY() {
        return y;
    }

    public Sprite setY(Double valor) {
        y = valor;
        return this;
    }

    public int getWidth() {
        return new ImageIcon(getClass().getResource(sprite)).getImage().getWidth(null);
    }

    public int getHeight() {
        return new ImageIcon(getClass().getResource(sprite)).getImage().getHeight(null);
    }
    public Sprite setSprite(String url) {
        sprite = url;
        
        return this;
    }

    public String getSprite() {
        return sprite;
    }

    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        refreshTime = System.currentTimeMillis();
        x = coordenadaHorizontal;
        y = coordenadaVertical;
        if (visible) {
            grafico.drawImage(new ImageIcon(getClass().getResource(sprite)).getImage(), x.intValue(), y.intValue(), null);
        }
    }

    public Sprite putSprite(Graphics grafico) {
        refreshTime = System.currentTimeMillis();
        if (visible) {
            grafico.drawImage(new ImageIcon(getClass().getResource(sprite)).getImage(), x.intValue(), y.intValue(), null);
        }
        return this;
    }

    public boolean isIn(Double x, Double y) {
        return x >= x && x <= x + getWidth() && y >= y && y <= y + getHeight();
    }

    public boolean intecerpta(Sprite sprite1) {
        boolean intercepta = false;
        boolean interceptaX = false;
        boolean interceptaY = false;
        if (sprite1.getX() <= getX() && sprite1.getX() + sprite1.getWidth() >= getX() || getX() <= sprite1.getX() && getX() + getWidth() >= sprite1.getX()) {
            interceptaX = true;
        }
        if (sprite1.getY() <= getY() && sprite1.getY() + sprite1.getHeight() >= getY() || getY() <= sprite1.getY() && getY() + getHeight() >= sprite1.getY()) {
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

    public long getLastRefresh() {
        return refreshTime;
    }

    public void setLastRefresh(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public void setMustBeDestroy(boolean mustBeDestroy) {        
        this.mustBeDestroy = mustBeDestroy;
    } 
    
    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 13 * hash + refreshTime.hashCode();
        hash = 13 * hash + (this.x != null ? this.x.hashCode() : 0);
        hash = 13 * hash + (this.y != null ? this.y.hashCode() : 0);
        hash = 13 * hash + (this.visible != null ? this.visible.hashCode() : 0);
        hash = 13 * hash + (this.y != null ? this.y.hashCode() : 0);
        hash = 13 * hash + (this.y != null ? this.y.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Sprite{" + "refreshTime=" + refreshTime + ", x=" + x + ", y=" + y + ", visible=" + visible + ", sprite=" + sprite + ", mustBeDestroy=" + mustBeDestroy + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sprite other = (Sprite) obj;
        if (this.mustBeDestroy != other.mustBeDestroy) {
            return false;
        }
        if (!Objects.equals(this.sprite, other.sprite)) {
            return false;
        }
        if (!Objects.equals(this.refreshTime, other.refreshTime)) {
            return false;
        }
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        if (!Objects.equals(this.visible, other.visible)) {
            return false;
        }
        return true;
    }

    public Long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Long refreshTime) {
        this.refreshTime = refreshTime;
    }
    
}

