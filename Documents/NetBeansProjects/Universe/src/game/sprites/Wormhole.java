/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import util.ImageTransform;
import util.Lib;

/**
 *
 * @author Garlyks
 */
public class Wormhole extends Sprite{
    private long creationTime = System.currentTimeMillis();
    private long lastUsage = System.currentTimeMillis();
    private int ROTATION_INTERVAL = 50; //interval in Milliseconds to the next grade change
    private long nextRotation = System.currentTimeMillis();
    private double actualRotation = 0;
    
    private Wormhole exit ;
    
    public Wormhole(){
        super();        
        super.setSprite("/Imagenes/wormhole/wormhole.png");
        
    }
    

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public Wormhole getExit() {
        return exit;
    }

    public void setExit(Wormhole exit) {
        this.exit = exit;
    }

    public int getRotacion() {
        return ROTATION_INTERVAL;
    }

    public void setRotacion(int ROTATION_INTERVAL) {
        this.ROTATION_INTERVAL = ROTATION_INTERVAL;
    }
    
 
    @Override
    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        //setX(x);
        //setY(y);
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());
        if(ROTATION_INTERVAL++>=360)ROTATION_INTERVAL = 0;        
        image = ImageTransform.rotacionImagen(image,ROTATION_INTERVAL);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
    }
    @Override
    public Wormhole putSprite(Graphics grafico) { 
       // System.out.println("Wormhole at X: "+getX().intValue()+" Y: "+ getY().intValue());
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());
               
        if(nextRotation<= System.currentTimeMillis() ){
            nextRotation = System.currentTimeMillis()+ROTATION_INTERVAL;
            if(actualRotation++>=360)actualRotation = 0;             
        }
        
        image = ImageTransform.rotacionImagen(image,actualRotation);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
        if(getExit()!=null){
            
            //image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getExit().getSprite())).getImage());
            //if(getExit().getRotacion()>=360)getExit().setRotacion(0);        
            //image = ImageTransform.ROTATION_INTERVALImagen(image,ROTATION_INTERVAL);
            if (isVisible()) {
                grafico.drawImage(image, getExit().getX().intValue(), getExit().getY().intValue(), null);
            }
        }
        return this;
    }

    public long getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(long lastUsage) {
        this.lastUsage = lastUsage;
    }
    
    
}
