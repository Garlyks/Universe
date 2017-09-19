/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sprites;

import java.util.ArrayList;

public class WormholeControl implements Runnable {
    
    ArrayList<Sprite> spritesDinamicos = new ArrayList();
    
    @Override
    public void run() {
        spritesDinamicos.stream().filter(wh->wh.getClass().equals(Wormhole.class)).findFirst().ifPresent(wh->{
            Wormhole whActual = (Wormhole) wh;            
            (whActual).setRotacion((whActual).getRotacion()+1);
            spritesDinamicos.forEach(sprite->{
                if(whActual.intecerpta(sprite) && !sprite.equals(whActual) && System.currentTimeMillis()-whActual.getLastUsage()>2000){
                    whActual.setLastUsage(System.currentTimeMillis());
                    sprite.setX(whActual.getExit().getX())
                          .setY(whActual.getExit().getY()); 
                }
                if(whActual.getExit().intecerpta(sprite) && !sprite.equals(whActual) && System.currentTimeMillis()-whActual.getLastUsage()>2000){
                    sprite.setX(whActual.getX())
                          .setY(whActual.getY()); 
                    whActual.setLastUsage(System.currentTimeMillis());
                }
            });            
        });
    }
    
}
