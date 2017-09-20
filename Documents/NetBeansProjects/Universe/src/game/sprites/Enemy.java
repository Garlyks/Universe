package game.sprites;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Enemy
extends Nave {
    double maxHp = 120d;
    double maxArmor = 120d;
    double armor = 120d;
    double maxShield = 120d;
    double shieldResistance = 15d;
    double shieldRegeneration = 0.5;
    double shield = 120d;
    boolean isDestroing = false;
    double shieldUntil = 0;
    int shieldPhase = 0;
    int destroyPhase = 0;
    private long lastShotTime;
    double minShotInterval=500;

    

    @Override
    public Enemy putSprite(Graphics grafico) {
        if (this.isVisible() && !this.isDestroing) {
            grafico.setColor(Color.green);
            Double porcentajeHp = this.hp * 100d / this.maxHp;
            grafico.fillRect(this.getX().intValue(), this.getY().intValue() + this.getHeight() + 11, new Double((double)this.getWidth() * porcentajeHp / 100d).intValue(), 2);
            Double porcentajeArmor = this.armor * 100d / this.maxArmor;
            grafico.setColor(Color.yellow);
            grafico.fillRect(this.getX().intValue(), this.getY().intValue() + this.getHeight() + 8, new Double((double)this.getWidth() * porcentajeArmor / 100d).intValue(), 2);
            Double porcentajeShield = this.shield * 100d / this.maxShield;
            grafico.setColor(new Color(150, 150, 240));
            grafico.fillRect(this.getX().intValue(), this.getY().intValue() + this.getHeight() + 5, new Double((double)this.getWidth() * porcentajeShield / 100d).intValue(), 2);
        }
        if (this.isDestroing) {
            if (++this.destroyPhase <= 90) {
                String parte = this.destroyPhase < 10 ? "0" + this.destroyPhase : "" + this.destroyPhase;
                this.setSprite("/Imagenes/Explosion/explosion1_00" + parte + ".png");
                super.putSprite(grafico);
            } else {
                this.destroy();
            }
        } else if (this.shieldUntil-System.currentTimeMillis()>0) {   
            String temp = this.getSprite();
            this.setSprite(temp.replace(".", "_shielding."));
            super.putSprite(grafico);
            this.setSprite(temp);          
        } else {
            if (this.shield < this.maxShield) {
                shield += (System.currentTimeMillis()-getRefreshTime())*(shieldRegeneration/1000);               
            }
            super.putSprite(grafico);
        }
        return this;
    }

    public void receiveDamage(Double damage) {
        if (this.shield > 0d && damage > 0d) {
            double damage_absorbed = 0d;
            if (damage > this.shieldResistance) {
                damage_absorbed = this.shieldResistance;
                damage = damage - this.shieldResistance;
            } else {
                damage_absorbed = damage;
                damage = 0d;
            }
            this.shield = this.shield - damage_absorbed;
            if (this.shield < 0d) {
                damage = - this.shield;
                this.shield = -4d;
            }
            this.shieldUntil = System.currentTimeMillis()+200;
        }
        if (this.armor > 0d && damage > 0d) {
            if (this.armor > damage) {
                this.armor = this.armor - damage;
                damage = 0d;
            } else {
                this.armor = this.armor - damage;
                damage = - this.armor;
            }
        }
        if (this.hp > 0d && damage > 0d) {
            if (this.hp > damage) {
                this.hp = this.hp - damage;
                damage = 0d;
            } else {
                int anchoExplosion = new ImageIcon(this.getClass().getResource("/Imagenes/Explosion/explosion1_0001.png")).getImage().getWidth(null);
                this.setX(this.getX() + new Double(this.getWidth() - anchoExplosion) / 2d);
                int altoExplosion = new ImageIcon(this.getClass().getResource("/Imagenes/Explosion/explosion1_0001.png")).getImage().getHeight(null);
                this.setY(this.getY() + new Double(this.getHeight() - altoExplosion) / 2d);
                this.velocidad = 0;
                this.isDestroing = true;
            }
        }
    }

    public Double getMaxHp() {
        return maxHp;
    }

    public Enemy setMaxHp(Double maxHp) {
        this.maxHp = maxHp;
        return this;
    }

    public Double getMaxArmor() {
        return maxArmor;
    }

    public Enemy setMaxArmor(Double maxArmor) {
        this.maxArmor = maxArmor;
        return this;
    }

    public Double getArmor() {
        return armor;
    }

    public Enemy setArmor(Double armor) {
        this.armor = armor;
        return this;
    }

    public Double getMaxShield() {
        return maxShield;
    }

    public Enemy setMaxShield(Double maxShield) {
        this.maxShield = maxShield;
        return this;
    }

    public Double getShieldResistance() {
        return shieldResistance;
    }

    public Enemy setShieldResistance(Double shieldResistance) {
        this.shieldResistance = shieldResistance;
        return this;
    }

    public Double getShieldRegeneration() {
        return shieldRegeneration;
    }

    public Enemy setShieldRegeneration(Double shieldRegeneration) {
        this.shieldRegeneration = shieldRegeneration;
        return this;
    }

    public Double getShield() {
        return shield;
    }

    public Enemy setShield(Double shield) {
        if(shield<getMaxShield())  
            this.shield = shield;
        else
            this.shield = getMaxShield();
        return this;
    }

    public boolean isDestroing() {
        return isDestroing;
    }

    public Enemy setIsDestroing(boolean isDestroing) {
        this.isDestroing = isDestroing;
        return this;
    }

    public double isIsShielding() {
        return shieldUntil;
    }

    public Enemy setIsShielding(double shieldUntil) {
        this.shieldUntil = shieldUntil;
        return this;
    }

    public int getShieldPhase() {
        return shieldPhase;
    }

    public Enemy setShieldPhase(int shieldPhase) {
        this.shieldPhase = shieldPhase;
        return this;
    }

    public int getDestroyPhase() {
        return destroyPhase;
    }

    public Enemy setDestroyPhase(int destroyPhase) {
        this.destroyPhase = destroyPhase;
        return this;
    }
   
    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }
    
    public double getMinShotInterval() {
        return minShotInterval;
    }

    public void setMinShotInterval(double minShotInterval) {
        this.minShotInterval = minShotInterval;
    }
    
    
    
    
    
}

