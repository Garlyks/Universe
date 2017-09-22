package game;

import game.sprites.Laser;
import game.sprites.Sprite;
import game.sprites.Marca;
import game.sprites.Nave;
import game.sprites.Wormhole;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import util.Lib;

public final class Juego extends Canvas {
    private static int fps = 0;
    private static int actualFps = 0;
    private static long fpsTime = 0;
    private final CopyOnWriteArrayList<Sprite> spritesStaticos = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Sprite> spritesDinamicos = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Nave> enemies = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Sprite> proyectilesPropios = new CopyOnWriteArrayList();
    //private final ArrayList<Sprite> proyectilesPropios = new ArrayList();
    private final CopyOnWriteArrayList<Sprite> proyectilesEnemigos = new CopyOnWriteArrayList();
    private Frame ventana;
    private final Sprite planeta = new Sprite();
    private final Sprite fondo = new Sprite();
    private final Nave myShip = new Nave();
    private Marca marca;
    BufferedImage pantalla;
    int refreshTime = 25;
    
    static long tiempo = System.currentTimeMillis();
    
    JPanel controles;
    private int dificult = 0;
    
    //private boolean clear = false;
    
    static final double COLLISION_DAMAGE = 2L;
    private final int MAX_ENEMY_LASER = 30;
    
    public Juego(){
        //cheats
        myShip.setMaxArmor(5000d);
        myShip.setDamageAmplifier(25);
        
        //configurar y setear cantidad enemigos
        configureScreen();   
        whInit();
        configureGame(); 
        threadGame.start();
        threadPaint.start();
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
        
        KeyEvent kem = new KeyEvent();
        addMouseListener(new MouseEvent());        
        ventana.addKeyListener(kem);        
        addKeyListener(kem);        
        setClear();
    }
        
    public void setClear(){
        //set enemies
        enemies.clear();
        for (int i = 0; i<dificult; i++){
            enemies.add((Nave) new Nave().setX(Lib.getRandomWidth(this)).setY(Lib.getRandomHeight(this)).setSprite("/Imagenes/Nave/splitter.png"));
        }
    }
    
    public void paintGraphics(Graphics grafico){
        try{
            proyectilesPropios.removeIf(proyectilPropio->(proyectilPropio.mustBeDestroy()));
            spritesDinamicos.removeIf(spriteDinamico->(spriteDinamico.mustBeDestroy()));
            proyectilesEnemigos.removeIf(proyectil->(proyectil.mustBeDestroy()));
            enemies.removeIf(enemy->(enemy.mustBeDestroy()));
            
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
            paintGraphics(grafico);
        }
        //FPS MANAGEMENT
        fps++;
        if(System.currentTimeMillis()-fpsTime>1000){
            fpsTime=System.currentTimeMillis();
            actualFps = fps;
            fps=0;
        }
        pantalla.getGraphics().drawString(actualFps+" fps", 25, 25);
        //END FPS MANAGEMENT
        
        //DIFICULT MESSAGE MANAGEMENT
        pantalla.getGraphics().drawString("Dificult "+dificult, 25, 50);
        //END DIFICULT FPS MANAGEMENT

        grafico.drawImage(pantalla, 0, 0, this);
    }    
    
    public void runGame(){
                        
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
            
            if (enemies.isEmpty()) {
                dificult++;
                //clear = true;
                setClear();
            }
            
            proyectilesEnemigos.stream().forEach(proyectilEnemigo->{
                if (myShip.intecerpta(proyectilEnemigo))
                myShip.receiveDamage(((Laser)proyectilEnemigo).hit());
            });            
            
            enemies.stream().forEach(enemy->{
                if(myShip.intecerpta(enemy)){
                    myShip.receiveDamage(COLLISION_DAMAGE);
                    enemy.receiveDamage(COLLISION_DAMAGE);
                }                
                if(proyectilesEnemigos.size()<MAX_ENEMY_LASER ){                    
                    Laser l = enemy.shot(myShip);
                    if (l!=null) proyectilesEnemigos.add(l);  
                }
                if ((tiempo % 13 == 0 || enemy.getRestanteX() == 0.0 && enemy.getRestanteY() == 0.0) && !enemy.isDestroing()){
                    enemy.moveTo(Lib.getRandomWidth(this), Lib.getRandomHeight(this));                    
                }
            });
    }
    
    public void whInit(){
        Wormhole wh = new Wormhole();
        wh.setExit(new Wormhole());
        wh.setX(Math.abs(Lib.getRandomWidth(this)-wh.getWidth()))
          .setY(Math.abs(Lib.getRandomHeight(this)-wh.getHeight()));
        wh.getExit().setX(Math.abs(Lib.getRandomWidth(this)-wh.getWidth()))
          .setY(Math.abs(Lib.getRandomHeight(this)-wh.getHeight()));
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
    
    Thread threadPaint = new Thread(new Runnable() {
        @Override
        public void run() {
            do {
                
               // if (System.currentTimeMillis() - tiempo >= (long) refreshTime) {
                paintGraphics(getGraphics());
                
                    //tiempo = System.currentTimeMillis();

                    /*try {
                        threadDibuja.sleep(25);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                    
               // } else {
                    //Logger.getLogger(Juego.class.getName()).log(Level.INFO, "Paint at "+System.currentTimeMillis() );
                    
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
                   // myShip.rotar(new Double(e.getX()), new Double(e.getY()));
                    
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
    
    private class KeyEvent implements KeyListener{
        private final Set<Integer> PRESSED = new HashSet<>();
        
        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public synchronized void keyReleased(java.awt.event.KeyEvent e) {
            PRESSED.remove(e.getKeyCode());            
            //String print ="";
            //print = PRESSED.stream().map((p) -> " "+p).reduce(print, String::concat);
            //System.out.println(print);
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public synchronized void keyPressed(java.awt.event.KeyEvent e) {
            PRESSED.add(e.getKeyCode());
           // System.out.println(e.getKeyCode());
           myShip.setImpulsoX(0);
           myShip.setImpulsoY(0);
            if(PRESSED.contains(java.awt.event.KeyEvent.VK_UP)){
                System.out.println("UP");
                myShip.setImpulsoY(-50);
            }
            if(PRESSED.contains(java.awt.event.KeyEvent.VK_DOWN)){
                System.out.println("DOWN");
                myShip.setImpulsoY(50);
            }
            if(PRESSED.contains(java.awt.event.KeyEvent.VK_LEFT)){
                System.out.println("LEFT");
                myShip.setImpulsoX(-50);
            }
            if(PRESSED.contains(java.awt.event.KeyEvent.VK_RIGHT)){
                System.out.println("RIGHT");
                myShip.setImpulsoX(50);
            }
            
            myShip.rotar(myShip.getX()+myShip.getImpulsoX(), myShip.getX()+myShip.getImpulsoY());
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

       
        
    }
}

