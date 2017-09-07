/*
 * Decompiled with CFR 0_122.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Enemy
extends Nave {
    Double max_hp = 120d;
    Double max_armor = 120d;
    Double armor = 120d;
    Double max_shield = 120d;
    Double shield_resistance = 10d;
    Double shield_regeneration = 0.1;
    Double shield = 120d;
    boolean isDestroing = false;
    boolean isShielding = false;
    int shieldPhase = 0;
    int destroyPhase = 0;

    @Override
    public void putSprite(Graphics grafico) {
        if (this.isVisible() && !this.isDestroing) {
            grafico.setColor(Color.green);
            Double porcentajeHp = this.hp * 100d / this.max_hp;
            grafico.fillRect(this.getX().intValue(), this.getY().intValue() + this.getHeight() + 11, new Double((double)this.getWidth() * porcentajeHp / 100d).intValue(), 2);
            Double porcentajeArmor = this.armor * 100d / this.max_armor;
            grafico.setColor(Color.yellow);
            grafico.fillRect(this.getX().intValue(), this.getY().intValue() + this.getHeight() + 8, new Double((double)this.getWidth() * porcentajeArmor / 100d).intValue(), 2);
            Double porcentajeShield = this.shield * 100d / this.max_shield;
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
            if (this.shield < this.max_shield) {
                this.shield = this.shield * 100d / this.max_shield > 10d ? this.shield + this.shield_regeneration * (this.shield * 100d / this.max_shield) / 100d : this.shield + 0.1;
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
}

