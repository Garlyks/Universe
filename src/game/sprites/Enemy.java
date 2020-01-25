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
    double shield_resistance = 15d;
    double shield_regeneration = 0.5;
    double shield = 120d;
    boolean isDestroing = false;
    boolean isShielding = false;
    int shieldPhase = 0;
    int destroyPhase = 0;
    

    @Override
    public void putSprite(Graphics grafico) {
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
        } else if (this.isShielding) {
            if (++this.shieldPhase < 10) {
                String temp = this.getSprite();
                this.setSprite(temp.replace(".", "_shielding."));
                super.putSprite(grafico);
                this.setSprite(temp);
            } else {
                this.isShielding = false;
                this.shieldPhase = 0;
            }
        } else {
            if (this.shield < this.maxShield) {
                this.shield = this.shield * 100d / this.maxShield > 10d ? this.shield + this.shield_regeneration * (this.shield * 100d / this.maxShield) / 100d : this.shield + 0.1;
            }
            super.putSprite(grafico);
        }
    }

    public void receiveDamage(Double damage) {
        if (this.shield > 0d && damage > 0d) {
            double damage_absorbed = 0d;
            if (damage > this.shield_resistance) {
                damage_absorbed = this.shield_resistance;
                damage = damage - this.shield_resistance;
            } else {
                damage_absorbed = damage;
                damage = 0d;
            }
            this.shield = this.shield - damage_absorbed;
            if (this.shield < 0d) {
                damage = - this.shield;
                this.shield = -4d;
            }
            this.isShielding = true;
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

    public Double getMax_hp() {
        return maxHp;
    }

    public Enemy setMax_hp(Double maxHp) {
        this.maxHp = maxHp;
        return this;
    }

    public Double getMax_armor() {
        return maxArmor;
    }

    public Enemy setMax_armor(Double maxArmor) {
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

    public Double getMax_shield() {
        return maxShield;
    }

    public Enemy setMax_shield(Double maxShield) {
        this.maxShield = maxShield;
        return this;
    }

    public Double getShield_resistance() {
        return shield_resistance;
    }

    public Enemy setShield_resistance(Double shield_resistance) {
        this.shield_resistance = shield_resistance;
        return this;
    }

    public Double getShield_regeneration() {
        return shield_regeneration;
    }

    public Enemy setShield_regeneration(Double shield_regeneration) {
        this.shield_regeneration = shield_regeneration;
        return this;
    }

    public Double getShield() {
        return shield;
    }

    public Enemy setShield(Double shield) {
        this.shield = shield;
        return this;
    }

    public boolean isDestroing() {
        return isDestroing;
    }

    public Enemy setIsDestroing(boolean isDestroing) {
        this.isDestroing = isDestroing;
        return this;
    }

    public boolean isIsShielding() {
        return isShielding;
    }

    public Enemy setIsShielding(boolean isShielding) {
        this.isShielding = isShielding;
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

    public void setMaxArmor(Double maxArmor) {
        armor = maxArmor;
        this.maxArmor = maxArmor;
    }

    public void setMaxShield(Double maxShield) {
        shield = maxShield;
        this.maxShield = maxShield;
    }

    
    
    
    
    
}

