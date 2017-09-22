package game.sprites;


public class Enemy
extends Nave {
    

    /*@Override
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
            this.setImpulsoX(0);
            this.setImpulsoY(0);
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
    }*/

  
}