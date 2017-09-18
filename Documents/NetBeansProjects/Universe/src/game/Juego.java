package game;

import game.sprites.Laser;
import game.sprites.Sprite;
import game.sprites.Enemy;
import game.sprites.Marca;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    private final ArrayList<Sprite> spritesStaticos = new ArrayList();
    private final ArrayList<Sprite> spritesDinamicos = new ArrayList();
    private final ArrayList<Enemy> enemies = new ArrayList();
    private final ArrayList<Sprite> proyectilesPropios = new ArrayList();
    private final ArrayList<Sprite> proyectilesEnemigos = new ArrayList();
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
        configure();       
        threadGame.start();
        threadDibuja.start();
    }    
    
    
    public void configure(){
        
        marca = new Marca();
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
         
        planeta.setSprite("/Imagenes/planeta-agua.png");
        fondo.setSprite("/Imagenes/space.jpg");
        planeta.setX(80.0);
        planeta.setY(80.0);
        spritesStaticos.add(fondo);
        spritesStaticos.add(planeta);
        spritesDinamicos.add(marca);
        spritesDinamicos.add(myShip);
        
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
        ArrayList<Sprite> spritesStaticosT = new ArrayList<>(spritesStaticos);
        ArrayList<Sprite> spritesDinamicosT = new ArrayList<>(spritesDinamicos);
        ArrayList<Enemy> enemiesT = new ArrayList<>(enemies);
        ArrayList<Sprite> proyectilesPropiosT = new ArrayList<>(proyectilesPropios);
        ArrayList<Sprite> proyectilesEnemigosT = new ArrayList<>(proyectilesEnemigos);
        try{
            pantalla = new BufferedImage(getWidth(), getHeight(), 1);
            spritesStaticosT.stream().forEach((Sprite spriteStatico)->{
                spriteStatico.putSprite(pantalla.getGraphics());
            });

            spritesDinamicosT.stream().forEach((Sprite spritesDinamico)->{
                spritesDinamico.putSprite(pantalla.getGraphics());
            });

            proyectilesPropiosT.stream().forEach((Sprite proyectilPropio)->{
                proyectilPropio.putSprite(pantalla.getGraphics());
            });

            proyectilesEnemigosT.stream().forEach(proyectil->{
                ((Sprite)proyectil).putSprite(pantalla.getGraphics());
            });

            enemiesT.stream().forEach(proyectil->{
                ((Sprite)proyectil).putSprite(pantalla.getGraphics());
            });

            //FPS MANAGEMENT
            fps++;
            if(System.currentTimeMillis()-fpsTime>1000){
                fpsTime=System.currentTimeMillis();
                fpsActual = fps;
                fps=0;

            }
            pantalla.getGraphics().drawString(fpsActual+" fps", 25, 25);
            //END FPS MANAGEMENT

            grafico.drawImage(pantalla, 0, 0, this);
        }catch(Exception e){
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE,"Test time "+ (System.currentTimeMillis() - tiempo),e);
            dibuja(grafico);
        }
    }    
    
    public void runGame(){
            //synchronized(this){
            proyectilesPropios.removeIf(proyectilPropio->(proyectilPropio.mustBeDestroy()));
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
            
            if (enemies.size() == 0) {
                dificult++;
                clear = true;
                setClear();
            }
            
            spritesDinamicos.stream().forEach(spriteDinamico->{
                spriteDinamico.move();                
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
                if ((tiempo % 103 == 0 || enemy.getRestanteX() == 0.0 && enemy.getRestanteY() == 0.0) && !enemy.isDestroing()){
                    enemy.moveTo(Lib.getRandomWidth(this), Lib.getRandomHeight(this));
                    if(proyectilesEnemigos.size()<maxEnemyLasers){                        
                        proyectilesEnemigos.add(new Laser(enemy,myShip));
                    }
                } 
                if (!(tiempo % (Math.random() * 80.0 + 1) != 0 || enemy.isDestroing())){
                    proyectilesEnemigos.add(new Laser(enemy,myShip));
                }  
            });            
    }

    public void whControl(){
        
    }
    
    Thread threadGame = new Thread(new Runnable() {
        @Override
        public void run() {
            long gameTime = System.currentTimeMillis();
            do {
                if (System.currentTimeMillis() - tiempo >= refreshTime) {
                    
                    //Logger.getLogger(Juego.class.getName()).log(Level.INFO,"Test time "+ (System.currentTimeMillis() - tiempo));
                    tiempo = System.currentTimeMillis();
                    runGame(); 
                    //tiempo = System.currentTimeMillis();
                    /*try {
                        threadGame.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                    
                }else{  
                    try {
                        threadGame.sleep(refreshTime- (System.currentTimeMillis() - tiempo));
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

