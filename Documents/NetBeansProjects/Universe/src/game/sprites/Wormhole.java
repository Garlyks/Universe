/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sprites;

/**
 *
 * @author Garlyks
 */
public class Wormhole extends Sprite{
    private long creationTime = System.currentTimeMillis();
    private Wormhole exit;
    
    

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
}
