package game.sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.swing.ImageIcon;
import util.ImageTransform;
import util.Lib;

public class Nave
extends Sprite {
    int iterator = 0;
    public final int VELOCIDAD = 150; //pixeles por segundo
    int velocidadActual = 150; //pixeles por segundo
    
    double direccion = 0;
    double impulsoX = 0;
    double impulsoY = 0;
    double hp = 120;
    
    double relacionDistanciaXY = 0.5;
    double relacionDistanciaYX = 0.5;
    double damageAmplifier = 1;   
    
    long timeAlive;
    long lastShotTime = 0;
    
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
    
    public static final long SHOT_RELOAD = 500;
    
    public Nave() {
        //super(this);
        super.setSprite("/Imagenes/Nave/nave.png");
        super.setX(200d);
        super.setY(200d);
        
    }


    @Override
    public Nave move() {
        //calcularRelacionXY();
        //System.out.println("impulsoX "+impulsoX+" impulsoY "+impulsoY);
        
        if(impulsoX!=0 || impulsoY!=0){
            double distanciaARecorrer = (System.currentTimeMillis()-getRefreshTime())*(velocidadActual/1000d);
            double distanciaX = distanciaARecorrer * relacionDistanciaXY/100;
            double distanciaY = distanciaARecorrer * relacionDistanciaYX/100;
            
            setX(getX()+distanciaX);
            setY(getY()+distanciaY);
            
            if(impulsoX>-0.5 && impulsoX<0.5){
                impulsoX = 0;
            }else{
                if(impulsoX>0) impulsoX-=Math.abs(distanciaX);
                else impulsoX+=Math.abs(distanciaX);
            }
            if(impulsoY>-0.5 && impulsoY<0.5){
                impulsoY = 0;
            }else{
                if(impulsoY>0) impulsoY-=Math.abs(distanciaY);
                else impulsoY+=Math.abs(distanciaY);
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
        this.hp = hp;
    }

    public void moveTo(Double x, Double y) {
        
        if (!Objects.equals(getX(), x) && !Objects.equals(getY(), y)) {
            x-=getWidth()/2;
            y-=getHeight()/2;
           
            impulsoX = x-getX();
            impulsoY = y-getY();
           
        }
        direccion = Lib.calcularRotacion(getX(), getY(),x,y);
        calcularRelacionXY();
    }

    public void calcularRelacionXY() {
            relacionDistanciaXY = 100 * Math.abs(impulsoX) / (Math.abs(impulsoX) + Math.abs(impulsoY)) ;
            relacionDistanciaYX = 100 * Math.abs(impulsoY) / (Math.abs(impulsoX) + Math.abs(impulsoY)) ;
            if(impulsoX<0){
                relacionDistanciaXY*= -1;
            }
            if(impulsoY<0){
                relacionDistanciaYX*= -1;
            }
            velocidadActual =  (int) (VELOCIDAD*100/(Math.sqrt(relacionDistanciaXY*relacionDistanciaXY+relacionDistanciaYX*relacionDistanciaYX))); 
    }

    public void rotar(Double x, Double y) {
        direccion = Lib.calcularRotacion(getX(), getY(),x, y);
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
                //super.putSprite(grafico);
            } else {
                this.destroy();
            }
        } else if (this.shieldUntil-System.currentTimeMillis()>0) {   
            String temp = this.getSprite();
            this.setSprite(temp.replace(".", "_shielding."));
            //super.putSprite(grafico);
            this.setSprite(temp);          
        } else {
            if (this.shield < this.maxShield) {
                shield += (System.currentTimeMillis()-getRefreshTime())*(shieldRegeneration/1000); 
            }
            //super.putSprite(grafico);
        }
        move();
        
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());       
        image = ImageTransform.rotacionImagen(image, direccion);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
        
        setRefreshTime(System.currentTimeMillis());
        
        return this;
    }
    
    public void receiveDamage(Double damage) {
        if (this.shield > 0d && damage > 0d) {
            double damage_absorbed;
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
                //this.VELOCIDAD = 0;
                this.isDestroing = true;
            }
        }
    }
    
    public Laser shot(Nave target){
        if(System.currentTimeMillis()-lastShotTime>SHOT_RELOAD){
            lastShotTime = (System.currentTimeMillis());
            return new Laser(this,target);
        }
        return null;
    };
    
    public Double getRestanteX() {
        return impulsoX;
    }

    public void setRestanteX(Double impulsoX) {
        this.impulsoX = impulsoX;
    }

    public Double getRestanteY() {
        return impulsoY;
    }

    public void setRestanteY(Double impulsoY) {
        this.impulsoY = impulsoY;
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public Double getRelacionDistanciaXY() {
        return relacionDistanciaXY;
    }

    public void setRelacionDistanciaXY(Double relacionDistanciaXY) {
        this.relacionDistanciaXY = relacionDistanciaXY;
    }

    public Double getRelacionDistanciaYX() {
        return relacionDistanciaYX;
    }

    public void setRelacionDistanciaYX(Double relacionDistanciaYX) {
        this.relacionDistanciaYX = relacionDistanciaYX;
    }
    
    public double getDamageAmplifier() {
        return damageAmplifier;
    }

    public void setDamageAmplifier(double damageAmplifier) {
        this.damageAmplifier = damageAmplifier;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }

    public double getImpulsoX() {
        return impulsoX;
    }

    public void setImpulsoX(double impulsoX) {
        this.impulsoX = impulsoX;
        calcularRelacionXY();
    }

    public double getImpulsoY() {
        return impulsoY;
    }

    public void setImpulsoY(double impulsoY) {
        //calcularRelacionXY();
        //rotar(relacionDistanciaXY,relacionDistanciaYX);
        this.impulsoY = impulsoY;
    }

    public double getShieldRegeneration() {
        return shieldRegeneration;
    }

    public void setShieldRegeneration(double shieldRegeneration) {
        this.shieldRegeneration = shieldRegeneration;
    }

    public int getVelocidadActual() {
        return velocidadActual;
    }

    public void setVelocidadActual(int velocidadActual) {
        this.velocidadActual = velocidadActual;
    }

    public long getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(long timeAlive) {
        this.timeAlive = timeAlive;
    }

    public double getShieldResistance() {
        return shieldResistance;
    }

    public void setShieldResistance(double shieldResistance) {
        this.shieldResistance = shieldResistance;
    }

    public double getShieldUntil() {
        return shieldUntil;
    }

    public void setShieldUntil(double shieldUntil) {
        this.shieldUntil = shieldUntil;
    }

    public int getShieldPhase() {
        return shieldPhase;
    }

    public void setShieldPhase(int shieldPhase) {
        this.shieldPhase = shieldPhase;
    }

    public double getShield() {
        return shield;
    }

    public void setShield(double shield) {
        this.shield = shield;
    }

    public boolean isDestroing() {
        return isDestroing;
    }

    public double getMaxArmor() {
        return maxArmor;
    }

    public void setMaxArmor(double maxArmor) {
        armor = maxArmor;
        this.maxArmor = maxArmor;
    }
    
    
    

    
    
}

