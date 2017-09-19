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
    private int rotacion = 0;
    
    private Wormhole exit ;
    
    public Wormhole(){
        super();        
        setSprite("/Imagenes/wormhole/wormhole.png");
        
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
        return rotacion;
    }

    public void setRotacion(int rotacion) {
        this.rotacion = rotacion;
    }
    
 
    @Override
    public void putSprite(Graphics grafico, Double coordenadaHorizontal, Double coordenadaVertical) {
        setX(x);
        setY(y);
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());
        if(rotacion++>=360)rotacion = 0;        
        image = ImageTransform.rotacionImagen(image,rotacion);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
    }
    @Override
    public Wormhole putSprite(Graphics grafico) {        
        BufferedImage image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getSprite())).getImage());
        if(rotacion++>=360)rotacion = 0;        
        image = ImageTransform.rotacionImagen(image,rotacion);
        if (isVisible()) {
            grafico.drawImage(image, getX().intValue(), getY().intValue(), null);
        }
        if(getExit()!=null){
            
            //image = Lib.toBufferedImage(new ImageIcon(getClass().getResource(getExit().getSprite())).getImage());
            //if(getExit().getRotacion()>=360)getExit().setRotacion(0);        
            //image = ImageTransform.rotacionImagen(image,rotacion);
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
