package game;

import game.sprites.Laser;
import game.sprites.Nave;
import java.awt.Canvas;
import java.util.concurrent.CopyOnWriteArrayList;
import util.Lib;

public class EnemyManager {
    private final CopyOnWriteArrayList<Nave> enemies = new CopyOnWriteArrayList<>();
    private int enemyAmount = 0;
    private long lastShot = System.currentTimeMillis();
    private boolean isCheckingMoves =false;
    private boolean isCheckingImpacts =false;
    
    private final String DEFAULT_SPRITE = "/Imagenes/Nave/splitter.png";
    private final Canvas canvas;
    
    public EnemyManager(Canvas c, int enemyAmount ){
        canvas = c;
        this.enemyAmount = enemyAmount;
    }
    
    public void add(Nave e){
        enemies.add(e);
    }
    
    public void clear(){
        enemies.clear();
    }
    
    public void checkDestroyed(){
        enemies.removeIf(enemy->(enemy.mustBeDestroy()));
    }
    
    public void restart(int enemyAmount){
        this.enemyAmount = enemyAmount;
        for (int i = 0; i<enemyAmount; i++){
            enemies.add((Nave) new Nave().setX(Lib.getRandomWidth(canvas)).setY(Lib.getRandomHeight(canvas)).setSprite(DEFAULT_SPRITE));
        }
    };
    
    //chequea los impactos de proyectiles sobre el listado
    public void checkImpacts(CopyOnWriteArrayList<Laser> proyectils){
        new Thread(() -> {
            isCheckingImpacts= true;//asegura q futuros hilos no se pisen con este
            proyectils.stream().forEach(proyectil->{
                proyectil.move();
                enemies.stream().forEach(enemy->{
                    if(enemy.intecerpta(proyectil)){
                        enemy.receiveDamage(proyectil.hit());
                    }
                });
            });
            checkDestroyed();
            if(enemies.isEmpty()){
                enemyAmount++;
                for (int i = 0; i<enemyAmount; i++){
                    enemies.add((Nave) new Nave().setX(Lib.getRandomWidth(canvas)).setY(Lib.getRandomHeight(canvas)).setSprite("/Imagenes/Nave/splitter.png"));
                }
            }
            isCheckingImpacts = false;
        }).start();       
    }
    public synchronized void checkMoves(){        
        new Thread(() -> {            
            isCheckingMoves= true;//asegura q futuros hilos no se pisen con este
            enemies.stream().forEach(enemy->{
                if(enemy.getImpulsoX()+enemy.getImpulsoY()==0){
                    enemy.moveTo(Lib.getRandomWidth(canvas), Lib.getRandomHeight(canvas));
                }
            });
            isCheckingMoves=false;
        }).start();
    }
    
    public CopyOnWriteArrayList<Nave> getEnemies() {
        checkMoves();
        return enemies;
    }
    
    
    
    
    
}
