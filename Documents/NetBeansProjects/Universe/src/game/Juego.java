package game;

import game.sprites.Laser;
import game.sprites.Sprite;
import game.sprites.Enemy;
import game.sprites.Marca;
import game.sprites.Wormhole;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import util.Lib;

public final class Juego extends Canvas {
    private static int fps = 0;
    private static int fpsActual = 0;
    private static long fpsTime = 0;
    private final CopyOnWriteArrayList<Sprite> spritesStaticos = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Sprite> spritesDinamicos = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Sprite> proyectilesPropios = new CopyOnWriteArrayList();
    //private final ArrayList<Sprite> proyectilesPropios = new ArrayList();
    private final CopyOnWriteArrayList<Sprite> proyectilesEnemigos = new CopyOnWriteArrayList();
    private Frame ventana;
    private final Sprite planeta = new Sprite();
    private final Sprite fondo = new Sprite();
    private final Enemy myShip = new Enemy();
    private Marca marca;
    BufferedImage pantalla;
    int refreshTime = 25;
    private final int maxEnemyLasers = 30;
    static long tiempo = System.currentTimeMillis();
    
    JPanel controles;
    private int dificult = 0;
    private boolean clear = false;
    
    static final double COLLISION_DAMAGE = 2L;
    
    public Juego(){
        //cheats
       // myShip.setMaxArmor(5000d);
        //myShip.setDamageAmplifier(25);
        
        //configurar y setear cantidad enemigos
        configureScreen();   
        whInit();
        configureGame(); 
        //spritesDinamicos.add(new Sprite().setSprite("/Imagenes/Marca/marca2b.png").setX(500d).setY(500d));
        threadGame.start();
        threadDibuja.start();
    }    
    
    public void configureScreen(){
        ventana = new JFrame();
        ventana.setLayout(new BorderLayout());
       // controles = new JPanel();
        //controles.add(new JButton("OK"));
        //ventana.add((Component)controles, "South");
        ventana.setSize(1024, 500);
        ventana.setExtendedState(6);
        ventana.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/Marca/marca2b.png")).getImage());
        ventana.add(this);
        ventana.setVisible(true);
        ventana.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    public void configureGame(){
        
        marca = new Marca();        
         
        planeta.setSprite("/Imagenes/planeta-agua.png");
        fondo.setSprite("/Imagenes/space.jpg");
        planeta.setX(80.0);
        planeta.setY(80.0);
        spritesDinamicos.add(myShip);
        spritesStaticos.add(fondo);
        spritesStaticos.add(planeta);
        spritesDinamicos.add(marca);
        
        
        addMouseListener(new MouseEvent());        
        setClear();
    }
        
    public void setClear(){
        //set enemies
        enemies.clear();
        for (int i = 0; i<dificult; i++){
            enemies.add((Enemy) new Enemy().setX(Lib.getRandomWidth(this)).setY(Lib.getRandomHeight(this)).setSprite("/Imagenes/Nave/splitter.png"));
        }
    }
    
    public void dibuja(Graphics grafico) { 
        try{
            pantalla = new BufferedImage(getWidth(), getHeight(), 1);
            spritesStaticos.stream().forEach((Sprite spriteStatico)->{
                if(spriteStatico!= null)spriteStatico.putSprite(pantalla.getGraphics());
            });

            spritesDinamicos.stream().forEach((Sprite spritesDinamico)->{
                if(spritesDinamico!= null)spritesDinamico.putSprite(pantalla.getGraphics());
            });

            proyectilesPropios.stream().forEach((Sprite proyectilPropio)->{
                if(proyectilPropio!= null) proyectilPropio.putSprite(pantalla.getGraphics());
            });

            proyectilesEnemigos.stream().forEach(proyectil->{
                if(proyectil!= null)(proyectil).putSprite(pantalla.getGraphics());
            });

            enemies.stream().forEach(proyectil->{
                ((Sprite)proyectil).putSprite(pantalla.getGraphics());
            });
            
        }catch(Exception e){
            //Logger.getLogger(Juego.class.getName()).log(Level.SEVERE,"Test time "+ (System.currentTimeMillis() - tiempo),e);
            dibuja(grafico);
        }
        //FPS MANAGEMENT
        fps++;
        if(System.currentTimeMillis()-fpsTime>1000){
            fpsTime=System.currentTimeMillis();
            fpsActual = fps;
            fps=0;
        }
        //END FPS MANAGEMENT
        pantalla.getGraphics().drawString(fpsActual+" fps", 25, 25);
        

        grafico.drawImage(pantalla, 0, 0, this);
    }    
    
    public void runGame(){
            //synchronized(this){
            proyectilesPropios.removeIf(proyectilPropio->(proyectilPropio.mustBeDestroy()));
            
           /* Iterator<Sprite> iter = proyectilesPropios.iterator();
            while (iter.hasNext()) {
                Sprite proyectilPropio = iter.next();
                if (proyectilPropio.mustBeDestroy())
                    iter.remove();
            }*/
            
            spritesDinamicos.removeIf(spriteDinamico->(spriteDinamico.mustBeDestroy()));
            proyectilesEnemigos.removeIf(proyectil->(proyectil.mustBeDestroy()));
            enemies.removeIf(enemy->(enemy.mustBeDestroy()));
            //} 
            
            if (marca.isVisible() && marca.intecerpta(myShip)) {
                marca.setVisible(false);
            }
            
            
            proyectilesPropios.stream().forEach(proyectilPropio->{
                proyectilPropio.move();
                enemies.stream().forEach(enemy->{
                    if(enemy.intecerpta(proyectilPropio)){
                        enemy.receiveDamage(((Laser)proyectilPropio).hit());
                    }
                });
            });            
            
            /*if (enemies.size() == 0) {
                dificult++;
                clear = true;
                setClear();
            }*/
            
            spritesDinamicos.stream().forEach(spriteDinamico->{
                //spriteDinamico.move();                
            });
            
            
            proyectilesEnemigos.stream().forEach(proyectilEnemigo->{
                proyectilEnemigo.move();
                if (myShip.intecerpta(proyectilEnemigo))
                myShip.receiveDamage(((Laser)proyectilEnemigo).hit());
            });            
          
            
            enemies.stream().forEach(enemy->{
                enemy.move();
            });      
            
            enemies.stream().forEach(enemy->{
                if(myShip.intecerpta(enemy)){
                    myShip.receiveDamage(COLLISION_DAMAGE);
                    enemy.receiveDamage(COLLISION_DAMAGE);
                }
                if ((tiempo % 13 == 0 || enemy.getRestanteX() == 0.0 && enemy.getRestanteY() == 0.0) && !enemy.isDestroing()){
                    enemy.moveTo(Lib.getRandomWidth(this), Lib.getRandomHeight(this));
                    if(proyectilesEnemigos.size()<maxEnemyLasers && System.currentTimeMillis()-enemy.getLastShotTime() > enemy.getMinShotInterval()){ 
                        enemy.setLastShotTime(System.currentTimeMillis());
                        proyectilesEnemigos.add(new Laser(enemy,myShip));
                    }
                } 
                if (!(tiempo % (Math.random() * 80.0 + 1) != 0 || enemy.isDestroing())){
                    proyectilesEnemigos.add(new Laser(enemy,myShip));
                }  
            });            
    }
    
    public void whInit(){
        Wormhole wh = new Wormhole();
        wh.setExit(new Wormhole());
        wh.setX(Lib.getRandomHeight(this)).setY(Lib.getRandomWidth(this));
        wh.getExit().setX(Lib.getRandomWidth(this)).setY(Lib.getRandomHeight(this));
        spritesDinamicos.add(wh);
        while(wh.getX()+wh.getY()-wh.getExit().getX()+wh.getExit().getY()<500){
            wh.getExit().setX(Lib.getRandomWidth(this)).setY(Lib.getRandomHeight(this));
        }
    }
    
    public void whControl(){
        spritesDinamicos.stream().filter(wh->wh.getClass().equals(Wormhole.class)).findFirst().ifPresent(wh->{
            Wormhole whActual = (Wormhole) wh;
            
            (whActual).setRotacion((whActual).getRotacion()+1);
            spritesDinamicos.forEach(sprite->{
                if(whActual.intecerpta(sprite) && !sprite.equals(whActual) && System.currentTimeMillis()-whActual.getLastUsage()>2000){
                    whActual.setLastUsage(System.currentTimeMillis());
                    sprite.setX(whActual.getExit().getX())
                          .setY(whActual.getExit().getY());   
                    marca.setVisible(false);
                    
                }
                if(whActual.getExit().intecerpta(sprite) && !sprite.equals(whActual) && System.currentTimeMillis()-whActual.getLastUsage()>2000){
                    sprite.setX(whActual.getX())
                          .setY(whActual.getY()); 
                    whActual.setLastUsage(System.currentTimeMillis());
                    marca.setVisible(false);
                }
            });
            
                
            
        });
    }
    
    Thread threadGame = new Thread(new Runnable() {
        @Override
        public void run() {
            
            do {
                if (System.currentTimeMillis() - tiempo >= refreshTime) {
                    
                    //Logger.getLogger(Juego.class.getName()).log(Level.INFO,"Test time "+ (System.currentTimeMillis() - tiempo));
                    tiempo = System.currentTimeMillis();
                    runGame();
                    if(tiempo%7==0) whControl();
                    //tiempo = System.currentTimeMillis();
                    /*try {
                        threadGame.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                    
                }else{  
                    try {
                        threadGame.sleep(Math.abs(refreshTime- (System.currentTimeMillis() - tiempo)));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }                
            //} while (!clear);
            } while (true);
           // clear = false;
            //runGame();
        }
    });
    
    Thread threadDibuja = new Thread(new Runnable() {
        @Override
        public void run() {
            do {
                
               // if (System.currentTimeMillis() - tiempo >= (long) refreshTime) {
                dibuja(getGraphics());
                
                    //tiempo = System.currentTimeMillis();

                    /*try {
                        threadDibuja.sleep(25);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                    
               // } else {
                    //Logger.getLogger(Juego.class.getName()).log(Level.INFO, "Dibuja at "+System.currentTimeMillis() );
                    
                //}
            } while (true);
            //JOptionPane.showMessageDialog(ventana, "You Lose", "Alerta", 1);
        }
    });
    
    Thread threadWormholes = new Thread(new Runnable() {
        @Override
        public void run() {
            do {
                if (System.currentTimeMillis() - tiempo >= refreshTime*2) 
                    whControl();
            } while (true);
        }
    });
    
    public static void main(String[] arg) {
        Juego game = new Juego();
    }
    
    
    
    private class MouseEvent
    implements MouseListener {
        private MouseEvent() {
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (!myShip.isIn(new Double(e.getX()), new Double(e.getY())) || e.getModifiers() == 16 || e.getModifiers() == 4 || e.getModifiers() == 8) {
                // empty if block
            }
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {           
            switch (e.getModifiers()) {
                case 16:
                    myShip.rotar(new Double(e.getX()), new Double(e.getY()));
                    
                    proyectilesPropios.add(new Laser(myShip, e.getX(), e.getY()));
                    break;
            // }
                case 4:
                    int x = e.getX();
                    int y = e.getY();
                    int minimoX = myShip.getWidth() / 2;
                    int maximoX = pantalla.getWidth() - myShip.getWidth() / 2;
                    int minimoY = myShip.getHeight() / 2;
                    int maximoY = pantalla.getHeight() - myShip.getHeight() / 2 ;
                    if (x < minimoX) {
                        x = minimoX;
                    } else if (x > maximoX) {
                        x = maximoX;
                    }
                    if (y < minimoY) {
                        y = minimoY;
                    } else if (y > maximoY) {
                        y = maximoY;
                    }
                    myShip.moveTo(new Double(x), new Double(y));
                    marca.setVisible(true);
                    marca.moveTo(new Double(x), new Double(y));
                    break;            
                case 8:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
    }

}

