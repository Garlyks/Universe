/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.sprites.Laser;
import game.sprites.Nave;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author jrodriguez
 */
public class CollisionManager {
    private static final CopyOnWriteArrayList<Laser> proyectilesPropios = new CopyOnWriteArrayList();    
    private static final CopyOnWriteArrayList<Laser> proyectilesEnemigos = new CopyOnWriteArrayList();
    
    private Nave playerShip;
    private EnemyManager enemyManager ;   
    
    private final int MAX_ENEMY_LASER = 30;
    static final double COLLISION_DAMAGE = 2L;
    
    public CollisionManager(Nave player, EnemyManager em){
        this.playerShip = player;
        this.enemyManager = em;
    }
    
    public void addProyectil(Laser l, boolean ownLaser){
        if (ownLaser){
            proyectilesPropios.add(l);
        }else{
            proyectilesEnemigos.add(l);
        }
    }
    
    
}
